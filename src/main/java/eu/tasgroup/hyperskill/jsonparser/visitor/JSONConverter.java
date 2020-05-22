package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;

import java.util.Objects;
import java.util.Optional;

public class JSONConverter {

    public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

    public XMLElement convert(JSONElement jsonElement) {

        Objects.requireNonNull(jsonElement, JSON_ELEMENT_CAN_T_BE_NULL);

        XMLElement xmlElement = new XMLElement();
        xmlElement.setTagName(jsonElement.getKey());

        /*
         * "key" : { "@asdsa" : "asdsad", "#key" : "kkkkk", "asdad": "val" } -> "<key></key>"
         */
        // FIXME usare l'Optional per togliere questo if
        if (!Objects.isNull(jsonElement.getValue())) {
            xmlElement.setText(jsonElement.getValue().toString());
        }

        // nel caso abbia figli li aggiungo anche all'xml element dopo averli convertiti
        for (JSONElement j: jsonElement.getChildren()){
            xmlElement.addChild(convert(j));
        }

        return xmlElement;
    }

}
