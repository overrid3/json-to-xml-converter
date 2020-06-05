package eu.tasgroup.hyperskill.jsonparser.converter;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;

import java.util.*;

public class JSONToXMLConverter {

    public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

    public XMLElement convert(JSONElement jsonElement) {

        Objects.requireNonNull(jsonElement, JSON_ELEMENT_CAN_T_BE_NULL);

        XMLElement xmlElement = new XMLElement();

        xmlElement.setTagName(JSONUtils.getNormalizedKey(jsonElement.getKey())); //SETTO TAG NAME ---> L'HO MESSO QUI PERCHE' LO FA SEMPRE!!!!!

        if (JSONUtils.hasNoChildren(jsonElement)) {
            xmlElement.setText(jsonElement.getValue());
            return xmlElement;
        } else if (isCorretlyFormattedXMLElement(jsonElement)) {
            checkForText(xmlElement, jsonElement); //CONTROLLO SUL TEXT
            checkForAttributesToInsert(xmlElement, jsonElement); //CONTROLLO SUGLI ATTRIBUTI (VIENE ESEGUITO ANCHE SE NON CI SONO
        } else {
            Set<JSONElement> set = checkForDuplicates(jsonElement);
            if (!set.isEmpty()) {
                jsonElement.getChildren().removeAll(set); //RIMUOVE GLI EVENTUALI DUPLICATI
            }

            jsonElement.getChildren().stream()
                    .filter(child -> !JSONUtils.illegalInitialAndOnlyCharacter(child.getKey()))
                    .forEach(child -> xmlElement.addChild(convert(child)));
        }
        return xmlElement;
    }

    //CONTROLLO SE E' EFFETTIVAMENTE E' UN ELEMENTO JSON "CORRETTAMENTE FORMATTATO", OVVERO RISPETTA LA STRUTTURA xyz:{@attr:"attr",#xyz:hur}
    private boolean isCorretlyFormattedXMLElement(JSONElement e) {
        return JSONUtils.hasExactlyOneChildWithHashKey(e)
                && !JSONUtils.hasChildWithoutSpecialCharacter(e)
                && !JSONUtils.hasEmptyOrIllegalKey(e)
                && !JSONUtils.hasNotEmptyChildren(e);
    }

    //METODO PER VEDERE SE L'ELEMENTO CON CHIAVE #KEY NON HA VALORE NULLO
    private void checkForText(XMLElement xe, JSONElement je) {

        Optional<JSONElement> optional = je.getChildren().stream().filter(child -> child.getKey().startsWith("#")).findFirst();

        if (optional.get().getValue() != null) {
            xe.setText(optional.get().getValue().toString());
        }
    }

    private void checkForAttributesToInsert(XMLElement xmlElement, JSONElement jsonElement) { //VIENE ESEGUITO ANCHE SE I FIGLI CON LA CHIOCCIOLA NON CI SONO
        jsonElement.getChildren().stream()
                .filter(child -> child.getKey().startsWith("@"))
                .forEach(child -> xmlElement.insertAttributeEntry(JSONUtils.getNormalizedKey(child.getKey()), setAttributeValueToXML(child)));
    }

    //SE IL VALORE DELL'ATTRIBUTO E' NULL, ALLORA ASSEGNA COME VALORE DELL'ATTRIBUTO UNA STRINGA VUOTA
    private String setAttributeValueToXML(JSONElement e) {
        Optional<Object> nullableValue = Optional.ofNullable(e.getValue());
        return nullableValue.isPresent() ? e.getValue().toString() : "";
    }

    //CONTROLLO TRA CHIAVI CON LO STESSO VALORE (TRA NORMALIZZATE E NORMALI)
    private Set<JSONElement> checkForDuplicates(JSONElement jsonElement) {

        Set<JSONElement> set = new HashSet<>();

        jsonElement.getChildren().stream().filter(el -> (el.getKey().startsWith("@") || el.getKey().startsWith("#")))
                .forEach(el -> {
                    for (JSONElement j : jsonElement.getChildren()) {
                        if (JSONUtils.getNormalizedKey(el.getKey()).equals(j.getKey())) {
                            set.add(el);
                        }
                    }
                });
        return set;
    }
}
