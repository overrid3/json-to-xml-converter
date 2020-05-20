package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.ArrayList;
import java.util.List;

public class JSONElement implements TreeElement {
	private String key;
	private Object value;
	private List<TreeElement> children;

	@Override
	public JSONElement getParent() {
		return parent;
	}

	public void setParent(JSONElement parent) {
		this.parent = parent;
	}

	private JSONElement parent;

	public JSONElement(){
		children = new ArrayList<>();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public List<TreeElement> getChildren() {
		return children;
	}

	public void setChildren(List<TreeElement> children) {
		this.children = children;
	}

	public void addChild(JSONElement element){
		this.children.add(element);
	}
}
