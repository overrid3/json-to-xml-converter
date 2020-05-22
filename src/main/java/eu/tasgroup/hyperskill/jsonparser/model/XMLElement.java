package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.ArrayList;
import java.util.List;

public class XMLElement implements TreeElement<XMLElement> {

	private String tagName; // <key></key>
	private String text; // <child_key1>child_key_value</child_key1>
	private List<XMLElement> children;
	private XMLElement parent;
	
	//attributi intanto caso con uno solo
	private String attributeName;
	private String attributeValue;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public XMLElement(){
		children= new ArrayList<>();
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setParent(XMLElement e){
		this.parent=e;
	}

	public void addChild(XMLElement e){
		e.setParent(this);
		children.add(e);
	}

	@Override
	public XMLElement getParent() {
		return parent;
	}

	@Override
	public List<XMLElement> getChildren() {
		return children;
	}
}
