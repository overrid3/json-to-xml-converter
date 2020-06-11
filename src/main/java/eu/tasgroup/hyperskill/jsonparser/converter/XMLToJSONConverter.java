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
			jsonElement.setValue(e.getText());
		} else {
			jsonElement.setValue("");
			e.getAttributes().forEach((key, value) -> jsonElement.addChild(new JSONElement(constructAttributeKey(key), value)));

			if(!XMLUtils.hasNoChildren(e)) {
				JSONElement c = new JSONElement("\"#"+e.getTagName() + "\"","");
				jsonElement.addChild(c);
				e.getChildren().forEach(child -> c.addChild(convertXML(child)));
			}else {
				jsonElement.addChild(new JSONElement(constructTagString(e.getTagName()), e.getText()));
			}
		}
		if (!XMLUtils.hasNoChildren(e)) {
			e.getChildren().forEach(el -> jsonElement.addChild(convertXML(el)));
		}
		return jsonElement;
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
