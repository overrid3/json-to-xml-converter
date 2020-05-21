package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;

import java.util.Objects;

public class JSONConverter {

    public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

    public XMLElement convert(JSONElement jsonElement) {

        Objects.requireNonNull(jsonElement, JSON_ELEMENT_CAN_T_BE_NULL);

        XMLElement xmlElement = new XMLElement();
        xmlElement.setTagName(jsonElement.getKey());

        if (jsonElement.getChildren().isEmpty()) {
            xmlElement.setText(jsonElement.getValue().toString());
        } else {
            for (JSONElement j: jsonElement.getChildren()){
                xmlElement.addChild(convert(j));
            }
        }
        return xmlElement;
    }

}
