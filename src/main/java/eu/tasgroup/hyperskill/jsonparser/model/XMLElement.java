package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.List;

public class XMLElement implements TreeElement<XMLElement> {

	private String tagName; // <key></key>
	private String text; // <child_key1>child_key_value</child_key1>

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

	@Override
	public XMLElement getParent() {
		return null;
	}

	@Override
	public List<XMLElement> getChildren() {
		return null;
	}
}
