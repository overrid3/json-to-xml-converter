package eu.tasgroup.hyperskill.jsonparser.visitor;

import java.util.Map;
import java.util.Objects;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

public class JSONElementVisitor {

	public static String XMLOBJECT_CANNOT_BE_NULL = "XML Object cannot be null";

	String elementToString = "";
	String path = "";

	public String printElement(XMLElement element){

		Objects.requireNonNull(element, XMLOBJECT_CANNOT_BE_NULL);

		if(element.getTagName()==null){
			element.getChildren().stream()
			.forEach(child -> {elementToString = printElement(child); path="";});
			return elementToString;
		}

		//path da stampare
		insertPath(element);

		//valore se non ci sono figli
		if(element.getChildren().isEmpty())
			insertValue(element);

		//attributi
		if(!element.getAttributes().isEmpty())
			printAttributes(element);

		//ricorsione per i figli
		element.getChildren().stream()
		.forEach(child -> {elementToString = printElement(child); path = path.replaceFirst(", "+child.getTagName(), "");});

		return elementToString;

	}

	//path vuoto => aggiungo path
	//path non vuoto => path = pathPadre, pathFiglio
	public void insertPath(XMLElement element) {
		path += path.isEmpty()? element.getTagName() : ", " + element.getTagName();
		elementToString += "\nElement:\npath = " + path + "\n";
	}

	public void insertValue(XMLElement element){

		elementToString += !element.getText().equals("null") ? "value = \""+element.getText()+"\"\n" : "value = "+element.getText()+"\n";
	}

	public void printAttributes(XMLElement element) {
		elementToString += "attributes:\n";
		for(Map.Entry<String, String> entry : element.getAttributes().entrySet()) {
			elementToString += entry.getKey()  + " = \"" + entry.getValue() + "\"\n";
		}
	}

}
