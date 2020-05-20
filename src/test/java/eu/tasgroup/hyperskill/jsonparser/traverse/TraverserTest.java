package eu.tasgroup.hyperskill.jsonparser.traverse;

import eu.tasgroup.hyperskill.jsonparser.file.FileManager;
import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TraverserTest {
	Traverser sut = new Traverser();

	@DisplayName("Testing traverse with example 1")
	@Test
	public void traverse() throws IOException {
		FileManager fileManager = new FileManager();
		String json = fileManager.load("firstExample.json");

		JSONElement jsonElement = sut.traverse(json);

		assertThat(jsonElement).isNotNull();

		assertThat(jsonElement.getChildren())
				.extracting("key", "value", "parent")
				.containsExactly(
						tuple("key", null, jsonElement));

		assertThat(jsonElement.getChildren().get(0).getChildren())
				.extracting("key", "value", "parent")
				.containsExactly(
						tuple("child_key1", "child_key_value", jsonElement.getChildren().get(0)),
						tuple("child_key2", "child_key_value", jsonElement.getChildren().get(0)),
						tuple("child_key3", null, jsonElement.getChildren().get(0)),
						tuple("child_key4", null, jsonElement.getChildren().get(0)));
	}

	@DisplayName("Testing traverse with example 2")
	@Test
	public void traverse2() throws IOException {
		FileManager fileManager = new FileManager();
		String json = fileManager.load("secondExample.json");

		JSONElement jsonElement = sut.traverse(json);

		assertThat(jsonElement).isNotNull();
		assertThat(jsonElement.getChildren())
				.extracting("key", "value", "parent")
				.containsExactly(
						tuple("key", null, jsonElement));


		assertThat(jsonElement.getChildren().get(0).getChildren())
				.extracting("key", "value", "parent")
				.containsExactly(
						tuple("child_key1", null, jsonElement.getChildren().get(0)),
						tuple("child_key2", "child_key_value", jsonElement.getChildren().get(0)),
						tuple("child_key3", null, jsonElement.getChildren().get(0)),
						tuple("child_key4", null, jsonElement.getChildren().get(0)));


		assertThat(jsonElement.getChildren().get(0).getChildren().get(0).getChildren())
				.extracting("key", "value", "parent")
				.containsExactly(
						tuple("@attribute1", "value1", jsonElement.getChildren().get(0).getChildren().get(0)),
						tuple("@attribute2", "value2", jsonElement.getChildren().get(0).getChildren().get(0)),
						tuple("#child_key1", "value3", jsonElement.getChildren().get(0).getChildren().get(0)));

		assertThat(jsonElement.getChildren().get(0).getChildren().get(1).getKey()).isEqualTo("child_key2");
		assertThat(jsonElement.getChildren().get(0).getChildren().get(1).getValue()).isEqualTo("child_key_value");
		assertThat(jsonElement.getChildren().get(0).getChildren().get(1).getParent().getKey()).isEqualTo("key");
		assertThat(jsonElement.getChildren().get(0).getChildren().get(1).getChildren()).isEmpty();

		assertThat(jsonElement.getChildren().get(0).getChildren().get(2).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(
                        tuple("@attribute1", "value4", jsonElement.getChildren().get(0).getChildren().get(2)),
                        tuple("@attribute2", "value5", jsonElement.getChildren().get(0).getChildren().get(2)),
                        tuple("#child_key3", null, jsonElement.getChildren().get(0).getChildren().get(2)));


		assertThat(jsonElement.getChildren().get(0).getChildren().get(3).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(
                        tuple("child_child_key1", "value1", jsonElement.getChildren().get(0).getChildren().get(3)),
                        tuple("child_child_key2", "value2", jsonElement.getChildren().get(0).getChildren().get(3)));

	}

	@DisplayName("testing traverse with null JSON")
	@Test
	public void traverse3() {
		assertThatThrownBy(() -> sut.traverse(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(Traverser.JSON_STRING_CAN_T_BE_NULL);
	}

	@DisplayName("testing traverse with invalid JSON")
	@Test
	public void traverse4() {

		assertThatThrownBy(() -> sut.traverse("{\"ciao:123}"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(Traverser.INVALID_JSON);
	}

}