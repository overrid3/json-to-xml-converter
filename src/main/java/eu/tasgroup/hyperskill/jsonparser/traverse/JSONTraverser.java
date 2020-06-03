package eu.tasgroup.hyperskill.jsonparser.traverse;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONTraverser {

    public static final String JSON_STRING_CAN_T_BE_NULL = "JSON string can't be null";
    public static final String INVALID_JSON = "Invalid JSON";
    Pattern elementPattern = Pattern.compile("^,?\\s*\"([@?\\#?\\w\\.]*)\"\\s*:\\s*([\\w\\.\\s-'\"]*),?((}?>}?|.|\\r|\\n)+)\\s*");
    Pattern closingElementPattern = Pattern.compile("^\\s*\\}\\s*,?((?>}|.|\\r|\\n)*)");

    public JSONElement traverse(String json) {

        if (json == null) {
            throw new IllegalArgumentException(JSON_STRING_CAN_T_BE_NULL);
        }

        JSONElement rootElement = new JSONElement();
        traverseItAll(json.replaceFirst("\\{", ""), rootElement);
        return rootElement;
    }

    private void traverseItAll(String json, JSONElement parentElement) {
        if (json.trim().isEmpty())
            return;


        Matcher elementWithValueMatcher = elementPattern.matcher(json);
        if (elementWithValueMatcher.find()) {
            JSONElement element = new JSONElement();
            String elementKey = elementWithValueMatcher.group(1);
            String elementValue = elementWithValueMatcher.group(2);
            String remainingString = elementWithValueMatcher.group(3);
            element.setKey(elementKey);

            if (parentElement != null)
                parentElement.addChild(element);
            element.setParent(parentElement);

            // se stiamo valutando un elemento complesso
            if (elementValue.trim().isEmpty()) {
                if (remainingString.trim().startsWith("{}")) {
                    element.setValue("");
                    traverseItAll(remainingString.replaceFirst("\\{\\s*\\}", ""), parentElement);
                }
                else if (remainingString.trim().startsWith("{")){
                    traverseItAll(remainingString.replaceFirst("\\{", ""), element);
                }
            } else { // stiamo valutando un elemento foglia (con un solo valore)
                Object convertedValue = JSONUtils.convertValue(elementValue.trim());
                element.setValue(convertedValue);
                traverseItAll(remainingString, parentElement);
            }
            return;
        }
        // se siamo in presenza di una chiusura di un elemento complesso (})
        Matcher closingElementMatcher = this.closingElementPattern.matcher(json);
        if (closingElementMatcher.find()) {
            String remainingString = closingElementMatcher.group(1);
            traverseItAll(remainingString, parentElement.getParent());
            return;
        }

        throw new IllegalArgumentException(INVALID_JSON);
    }


}
