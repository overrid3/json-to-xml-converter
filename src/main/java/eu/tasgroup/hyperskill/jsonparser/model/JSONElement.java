package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.ArrayList;
import java.util.List;

public class JSONElement implements TreeElement<JSONElement> {

	private String key;
	private Object value;
	private List<JSONElement> children;

	public JSONElement(String key, Object value){
		this.key=key;
		this.value=value;
		this.children = new ArrayList<>();
	}

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
	public List<JSONElement> getChildren() {
		return children;
	}

	public void setChildren(List<JSONElement> children) {
		this.children = children;
	}

	public void addChild(JSONElement element){
		element.setParent(this);
		this.children.add(element);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
