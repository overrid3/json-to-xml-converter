package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.List;

public interface TreeElement {
	public TreeElement getParent();
	public List<TreeElement> getChildren();
	public String getKey(); //DUBBIO
	public Object getValue();
}
