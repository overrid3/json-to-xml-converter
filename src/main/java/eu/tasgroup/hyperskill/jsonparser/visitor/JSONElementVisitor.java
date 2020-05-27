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
			for(XMLElement elem: element.getChildren()){
				elementToString = printElement(elem);
				path="";
			}
		}
		else{
			//path da stampare
			if(path.isEmpty())
				path=element.getTagName();
			else
				path += ", " + element.getTagName();
			elementToString += "\nElement:\npath = " + path + "\n";

			//l'elemento ha figli
			if(!element.getChildren().isEmpty()){
				//l'elemento ha attributi
				if(!element.getAttributes().isEmpty())
					printAttributes(element);
				for(XMLElement elementSon : element.getChildren()){
					elementToString = printElement(elementSon);
					path = path.replaceFirst(", "+elementSon.getTagName(), "");
				}
			}
			else{
				//stampa valore
				printValue(element);
				//stampa attributi
				if(!element.getAttributes().isEmpty()){
					printAttributes(element);
				}
			}
		}
		return elementToString;
	}

	public void printValue(XMLElement element){
		if(element.getText().equals("null"))
			elementToString += "value = "+element.getText()+"\n";
		else{
			elementToString += "value = \""+element.getText()+"\"\n";
		}
	}

	public void printAttributes(XMLElement element) {
		elementToString += "attributes:\n";
		for(Map.Entry<String, String> entry : element.getAttributes().entrySet()) {
			elementToString += entry.getKey()  + " = \"" + entry.getValue() + "\"\n";
		}
	}

}
