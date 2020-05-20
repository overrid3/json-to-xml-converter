package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

public class JSONConverter {

	public XMLElement convert(JSONElement jsonElement){

		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName(jsonElement.getKey());
		xmlElement.setText(jsonElement.getValue().toString());

		jsonElement.getChildren().stream().forEach(this::convert);

		return xmlElement;
	}

}
