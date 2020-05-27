package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

public class JSONElementVisitor {

	String elementToString = "";
	String path = "";

	public String printElement(XMLElement element){
		
	//	Objects.requireNonNull(element, Constants.XMLOBJECT_CANNOT_BE_NULL.getConstant());

		if(element.getTagName().equals("root")){
				for(XMLElement elem: element.getChildren()){
					elementToString = printElement(elem);
					path="";
				}
		}
		else{
			//path da stampare
			if(path.equals(""))
				path=element.getTagName();
			else
				path += ", " + element.getTagName();
			elementToString += "\nElement:\npath = " + path + "\n";

			//l'elemento ha figli
			if(!element.getChildren().isEmpty()){
				//l'elemento ha attributi
			//	if(element.checkAttributes())
			//		printAttributes(element);
				for(XMLElement elementSon : element.getChildren()){
					elementToString = printElement(elementSon);
					path = path.replaceFirst(", "+elementSon.getTagName(), "");
				}
			}
			else{
				//stampa valore
				printValue(element);
				//stampa attributi
//				if(element.checkAttributes()){
//					printAttributes(element);
//				}
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


}
