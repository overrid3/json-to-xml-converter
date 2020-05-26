package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class JSONConverter {

	public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

	public XMLElement convert(JSONElement jsonElement) {

		Objects.requireNonNull(jsonElement, JSON_ELEMENT_CAN_T_BE_NULL);

		XMLElement xmlElement = new XMLElement();

		//key
		if (JSONUtils.isValidKey(jsonElement.getKey())) {
			xmlElement.setTagName(JSONUtils.getNormalizedKey(jsonElement.getKey()));
		}

		//text
		if(jsonElement.getChildren().isEmpty()) {
			xmlElement.setText(setValueXML(jsonElement));
			return xmlElement;
		}

		//il json contiene solo figli non validi => l'xml avra' valore stringa vuota 
		if (noValidChildren(jsonElement)) {
			xmlElement.setText("");
			return xmlElement;
		}

		// non metto i duplicati nell' xml
		if(checkDuplicates(jsonElement)) {
			AtomicBoolean found= new AtomicBoolean(false);
			jsonElement.getChildren().stream().filter(el -> (el.getKey().startsWith("@")||el.getKey().startsWith("#"))&& el.getKey().length()>1)
			.forEach(el -> {
				for (JSONElement j :jsonElement.getChildren()) {
					if (JSONUtils.getNormalizedKey(el.getKey()).equals(j.getKey())) {
						xmlElement.addChild(convert(j));
						found.set(true);
					}
				}
				if (found.get()==false){
					xmlElement.addChild(convert(el));
				}
				found.set(false);
			});

			return xmlElement;
		}

		//tolgo elemnti con chiave non valida
		//considero tutti i figli come elementi xml
		//key:{key1:val1,key2:val2} oppure key:{@:val,#key:val} => key:{key:val} 
		if (atLeastOneNormalChild(jsonElement) || singleSpecialCharacterKey(jsonElement.getChildren())) {
			jsonElement.getChildren().stream().filter(child ->
			JSONUtils.isValidKey(child.getKey())).forEach(child -> xmlElement.addChild(convert(child)));
			return xmlElement;

		} 

		//attributi 
		jsonElement.getChildren().stream().filter(child -> child.getKey().startsWith("@") && JSONUtils.isValidKey(child.getKey()))
		.forEach(child -> {
			xmlElement.insertAttributeEntry(JSONUtils.getNormalizedKey(child.getKey()), setValueXML(child));
		});

		//key:{#key:val} => key ha un figlio con chiave key
		jsonElement.getChildren().stream()
		.filter(child -> (child.getKey().startsWith("#") && Objects.equals(JSONUtils.getNormalizedKey(child.getKey()), jsonElement.getKey())))
		.forEach(child -> {
			if (child.getChildren().isEmpty())
				xmlElement.setText(setValueXML(child));
			else {
				child.getChildren().stream().forEach(childChild -> xmlElement.addChild(convert(childChild)));
			}
		});

		return xmlElement;
	}

	/**
	 * torna true se uno dei figli � normale
	 * - non ha il carattere @ iniziale
	 * - se ha il # la chiave deve essere diversa da quella del padre
	 *
	 * @param child
	 * @param parent
	 * @return
	 */
	private boolean isNormalChild(JSONElement child, JSONElement parent) {
		return !child.getKey().startsWith("@") && !child.getKey().startsWith("#") ||
				(child.getKey().startsWith("#") && !Objects.equals(JSONUtils.getNormalizedKey(child.getKey()), parent.getKey()));
	}

	private boolean atLeastOneNormalChild(JSONElement e) {

		return e.getChildren().stream()
				.anyMatch(jsonChild -> isNormalChild(jsonChild, e));
	}


	private String setValueXML(JSONElement e) {

		Optional<Object> nullableValue = Optional.ofNullable(e.getValue());
		return  nullableValue.isPresent() ? e.getValue().toString() : "null";
	}

	/**
	 * torna true se i figli 
	 * - hanno figli
	 * - il primo figlio ha chiave @ o # e va ignorata
	 *
	 * @param child
	 * @return
	 */
	public boolean singleSpecialCharacterKey(List<JSONElement> child) {
		return child.size() > 1 && child.stream().anyMatch(child1 ->
		JSONUtils.getNormalizedKey((child1.getKey())).isEmpty());
	}

	public boolean noValidChildren(JSONElement jsonElement) {

		return jsonElement.getChildren().stream().noneMatch(child -> JSONUtils.isValidKey(child.getKey()));
	}

	public boolean duplicateChildren(JSONElement e1, JSONElement e2) {
		return (JSONUtils.getNormalizedKey(e1.getKey()).equals(e2.getKey()) && !e1.getKey().equals(e2.getKey()));

	}
	public boolean checkDuplicates(JSONElement jsonElement) {
		boolean c = false;
		for(JSONElement j : jsonElement.getChildren())
			c = jsonElement.getChildren().stream().anyMatch( child-> duplicateChildren(child,j));
		return c;
	}
}



