package eu.tasgroup.hyperskill.jsonparser.traverse;

import eu.tasgroup.hyperskill.jsonparser.file.FileManager;
import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class TraverserTest {
	Traverser sut = new Traverser();
	@Test
	public void traverse() throws IOException {
		FileManager fileManager = new FileManager();
		String json = fileManager.load("firstExample.json");

		JSONElement jsonElement = sut.traverse(json);

		assertThat(jsonElement).isNotNull();
		assertThat(jsonElement.getChildren())
				.extracting(JSONElement::getKey, JSONElement::getValue, JSONElement::getParent)
				.containsExactly(
						tuple("key", null, jsonElement),
						tuple("dasd", null, jsonElement));
		assertThat(jsonElement.getChildren().get(0).getChildren())
				.extracting(JSONElement::getKey, JSONElement::getValue, JSONElement::getParent)
				.containsExactly(
						tuple("child_key1", "\"child_key_value\"", jsonElement.getChildren().get(0)),
						tuple("child_key2", "\"child_key_value\"", jsonElement.getChildren().get(0)),
						tuple("child_key3", null, jsonElement.getChildren().get(0)),
						tuple("child_key4", null, jsonElement.getChildren().get(0)));

	}

}