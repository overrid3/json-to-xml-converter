package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.List;

public interface TreeElement <T> {
	public T getParent();
	public List<T> getChildren();
}
