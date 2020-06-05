package eu.tasgroup.hyperskill.jsonparser.converter;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.Objects;

public class XMLToJSONConverter {

    public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

    public JSONElement convert(XMLElement e) {

        Objects.requireNonNull(e, JSON_ELEMENT_CAN_T_BE_NULL);

        JSONElement jsonElement = new JSONElement();

        jsonElement.setKey("\"" + e.getTagName() + "\"");

        e.getAttributes().entrySet().stream().forEach(el -> jsonElement.addChild(new JSONElement("\"@"+el.getKey()+"\"", "\"" + el.getValue() + "\"")));

        if (e.getText() == null) {
            jsonElement.addChild(new JSONElement("\"#"+e.getTagName()+"\"",null));
        } else {
            if (e.getChildren().isEmpty()){
                jsonElement.addChild(new JSONElement("\"#"+e.getTagName()+"\"","\"" + e.getText() + "\""));
            } else {
                e.getChildren().stream().forEach(el->jsonElement.addChild(convert(el)));
            }
        }
        return jsonElement;
    }

}
