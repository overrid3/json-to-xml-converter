package eu.tasgroup.hyperskill.jsonparser.converter;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.utils.XMLUtils;

import java.util.Objects;

public class XMLToJSONConverter {

	public static final String JSON_ELEMENT_CAN_T_BE_NULL = "JSON element can't be null";

	public JSONElement convert(XMLElement e) {
		Objects.requireNonNull(e, JSON_ELEMENT_CAN_T_BE_NULL);

		JSONElement jsonElement = new JSONElement();
		jsonElement.addChild(convertXML(e));

		return jsonElement;

	}

	private JSONElement convertXML(XMLElement e) {

		Objects.requireNonNull(e, JSON_ELEMENT_CAN_T_BE_NULL);

		JSONElement jsonElement = new JSONElement();

		jsonElement.setKey("\"" + e.getTagName() + "\"");

		if (e.getAttributes().isEmpty()) {
			
			jsonElement.setValue(checkValue(e));
		} else {
			jsonElement.setValue("");
			e.getAttributes().entrySet().stream().forEach(el -> jsonElement.addChild(new JSONElement(constructAttributeKey(el.getKey()), el.getValue())));

			if(!XMLUtils.hasNoChildren(e)) {
				JSONElement c = new JSONElement("\"#"+e.getTagName() + "\"","");
				jsonElement.addChild(c);
				e.getChildren().stream().forEach(child -> c.addChild(convertXML(child)));
			}else {
				jsonElement.addChild(new JSONElement(constructTagString(e.getTagName()), checkValue(e)));
			}
			
		}
		if (!XMLUtils.hasNoChildren(e)) {
			e.getChildren().stream().forEach(el -> jsonElement.addChild(convertXML(el)));
		}
		return jsonElement;
	}

	private String checkValue(XMLElement e) {

		if (e.getText() == null) {
			 return null;
		} else if (!e.getText().isEmpty()) {
			return e.getText();
		} 
		return "";
		
	}

	public String constructAttributeKey(String key){
		StringBuilder stringBuilder=new StringBuilder(key);

		stringBuilder.insert(0,'@');

		return "\""+stringBuilder.toString()+"\"";
	}

	public String constructTagString(String value){

		StringBuilder stringBuilder=new StringBuilder(value);

		stringBuilder.insert(0,'#');

		return "\"" + stringBuilder.toString() + "\"";
	}
}
