package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.Objects;

public class JSONConverter {

	public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

	public XMLElement convert(JSONElement jsonElement) {

		Objects.requireNonNull(jsonElement, JSON_ELEMENT_CAN_T_BE_NULL);

		XMLElement xmlElement = new XMLElement();
		//non valuta i casi key = roba@roba key=roba#roba (?pattern)
		xmlElement.setTagName(jsonElement.getKey().replaceFirst("@", "").replaceFirst("#", ""));

		// FIXME usare l'Optional per togliere questo if
		if (!Objects.isNull(jsonElement.getValue())) {
			xmlElement.setText(jsonElement.getValue().toString());
			
			return xmlElement;
		}

		//"key" : { "@asdsa" : "asdsad", "#key" : "kkkkk", "asdad": "val" } 
		//-> "<key> <asdsa>asdsad</asdsa> <key>kkkkk</key> <asdad>val</asdaad> </key>"
		int countFirst=0;
		int countSecond=0;
		for(JSONElement j : jsonElement.getChildren()) {
			if(!j.getKey().startsWith("@"))
				countFirst++;
			if(!j.getKey().startsWith("#"))
				countSecond++;
		}
		if((countFirst+countSecond)!=jsonElement.getChildren().size()) {
			for(JSONElement j : jsonElement.getChildren()) {
				xmlElement.addChild(convert(j));
			}
			return xmlElement;
		}
		//"key" : { "@asdsa" : "asdsad", "#key" : "kkkkk"} 
		//-> "<key asdsa="asdsad">kkkkk</key>"
		//vale solo per questo caso va rivisto in seguito anche per il caso con più di un attributo (?metodo a parte) o nessun attributo
		//es: "key" : { "#key" : "kkkkk"} => "<key><key>kkkkk</key></key>" non funziona
		//bisogna mettere roba tipo
		//if(jsonElement.getChildren().size()==1) {
		//	xmlElement.setText(jsonElement.getChildren().get(0).getValue().toString());
		//	return xmlElement;
		//}
		//dentro al check
		boolean check = false;
		for(JSONElement j : jsonElement.getChildren()) {
			if(j.getKey().equals("#"+jsonElement.getKey()))
				check = true;
		}
		if(check) {
			for(JSONElement j : jsonElement.getChildren()) {
				if(j.getKey().startsWith("@")){
					xmlElement.setAttributeName(j.getKey().replaceFirst("@",""));
					xmlElement.setAttributeValue(j.getValue().toString());
				}else{
					xmlElement.setText(j.getValue().toString());
				}
			}
		}

		//non serve piu'
//		for (JSONElement j: jsonElement.getChildren()){
//			xmlElement.addChild(convert(j));
//		}

		return xmlElement;
	}

}
