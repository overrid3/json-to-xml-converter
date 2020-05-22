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
		//non valuta i casi key = roba@roba key=roba#roba (?pattern)
		xmlElement.setTagName(JSONUtils.getNormalizedKey(jsonElement.getKey()));

		// FIXME usare l'Optional per togliere questo if
		if (!Objects.isNull(jsonElement.getValue())) {
			xmlElement.setText(jsonElement.getValue().toString());
			return xmlElement;
		}

		//se ha figli guardo i figli tranne se #keypadre
	//	jsonElement.getChildren().stream().forEach(child -> xmlElement.addChild(convert(child)));
		
		boolean atLeastOneNormalChild = jsonElement.getChildren().stream()
				.anyMatch(jsonChild -> isNormalChild(jsonChild, jsonElement));
		if(atLeastOneNormalChild) {
			jsonElement.getChildren().stream().forEach(child -> xmlElement.addChild(convert(child)));
			return xmlElement;
		}

		// torna true se tutti i figli sono anormali
		jsonElement.getChildren().stream().filter(child -> child.getKey().startsWith("@"))
				.forEach(child -> {
					xmlElement.insertAttributeEntry(JSONUtils.getNormalizedKey(child.getKey()), child.getValue().toString());
		});
		jsonElement.getChildren().stream()
				.filter(child -> (child.getKey().startsWith("#") && Objects.equals(JSONUtils.getNormalizedKey(child.getKey()), jsonElement.getKey()))).forEach(child -> {
					if(child.getChildren().isEmpty())
						xmlElement.setText(child.getValue().toString());
					else {
						child.getChildren().stream().forEach(childChild -> xmlElement.addChild(convert(childChild)));
					}
		});

		return xmlElement;
	}

	/**
	 *  torna true se uno dei figli � normale
     * 	- non ha il carattere @ iniziale
	 * 	- se ha il # la chiave deve essere diversa da quella del padre
	 * @param child
	 * @param parent
	 * @return
	 */
	private boolean isNormalChild(JSONElement child, JSONElement parent){
		return !child.getKey().startsWith("@") && !child.getKey().startsWith("#") ||
				(child.getKey().startsWith("#") && !Objects.equals(JSONUtils.getNormalizedKey(child.getKey()), parent.getKey()));
	}

}
