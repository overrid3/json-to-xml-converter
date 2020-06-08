package eu.tasgroup.hyperskill.jsonparser.converter;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.utils.XMLUtils;

import java.util.Objects;

public class XMLToJSONConverter {

    public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

    public JSONElement convert(XMLElement e) {

        Objects.requireNonNull(e, JSON_ELEMENT_CAN_T_BE_NULL);

        JSONElement jsonElement = new JSONElement();

        jsonElement.setKey("\"" + e.getTagName() + "\"");

        /*e.getAttributes().entrySet().stream().forEach(el -> jsonElement.addChild(new JSONElement("\"@"+el.getKey()+"\"", "\"" + el.getValue() + "\"")));

        if (e.getText() == null) {
            jsonElement.addChild(new JSONElement("\"#"+e.getTagName()+"\"",null));
        } else {
            if (e.getChildren().isEmpty()){
                jsonElement.setValue("\""+e.getText()+"\"");
            } else {
                jsonElement.setValue("");
                e.getChildren().stream().forEach(el->jsonElement.addChild(convert(el)));
            }
        }*/

        if (e.getAttributes().isEmpty()) {
            checkAndAssignValue(e, jsonElement);
        } else {
            jsonElement.setValue("");
            e.getAttributes().entrySet().stream().forEach(el -> jsonElement.addChild(new JSONElement(constructAttributeKey(el.getKey()), el.getValue())));

            if (e.getText() == null) {
                jsonElement.addChild(new JSONElement(constructTagString(e.getTagName()), null));
            } else {
                jsonElement.addChild(new JSONElement(constructTagString(e.getTagName()), constructText(e.getText())));
            }
        }

        if (!XMLUtils.hasNoChildren(e)) {
            e.getChildren().stream().forEach(el -> jsonElement.addChild(convert(el)));
        }

        return jsonElement;
    }

    private void checkAndAssignValue(XMLElement e, JSONElement j) {

        if (e.getText() == null) {
            j.setValue(null);
        } else if (!e.getText().isEmpty()) {
            j.setValue(constructText(e.getText()));
        } else {
            j.setValue("");
        }
    }

    public String constructAttributeKey(String key){
        StringBuilder stringBuilder=new StringBuilder(key);

        stringBuilder.insert(0,'@');

        return "\""+stringBuilder.toString()+"\"";
    }

    public String constructTagString(String value){

        StringBuilder stringBuilder=new StringBuilder(value);

        stringBuilder.insert(0,'#');

        return "\"" + stringBuilder.toString() + "\"";
    }

    public String constructText(String text){
        return "\""+text+"\"";
    }

}
