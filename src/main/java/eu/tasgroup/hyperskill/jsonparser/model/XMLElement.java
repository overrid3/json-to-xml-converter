package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.ArrayList;
import java.util.List;

public class XMLElement implements TreeElement<XMLElement> {

	private String tagName; // <key></key>
	private String text; // <child_key1>child_key_value</child_key1>
	private List<XMLElement> children;
	private XMLElement parent;

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
