package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.List;

public interface TreeElement <T> {
	T getParent();
	List<T> getChildren();
}
