package eu.tasgroup.hyperskill.jsonparser.traverse;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

class XMLTraverserTest {

	XMLTraverser sut = new XMLTraverser();
	
	@DisplayName("first test")
	@Test
	public void XMLTraverser1Test() {
		String xml = "<transaction><id>6753322</id></transaction>";
		XMLElement e = sut.traverse(xml);
		assertThat(e).extracting("tagName","Text","parent").containsExactly("transaction","",null);
		assertThat(e.getChildren()).extracting("tagName","Text","parent").containsExactly(tuple("id","6753322",e));
		
	}
	
	@DisplayName("attributes test")
	@Test
	public void XMLTraverser2Test() {
		String xml = "<transaction><id>6753322</id><number region=\"Russia\">8-900-000-00-00</number></transaction>";
		XMLElement e = sut.traverse(xml);
		assertThat(e).extracting("tagName","Text","parent").containsExactly("transaction","",null);
		assertThat(e.getChildren()).extracting("tagName","Text","parent").containsExactly(tuple("id","6753322",e),tuple("number","8-900-000-00-00",e));
		assertThat(e.getChildren().get(1).getAttributes()).containsEntry("region", "Russia");
		
	}
	
	@DisplayName("null value test")
	@Test
	public void XMLTraverser3Test() {
		String xml = "<transaction>\n\t<id>6753322</id><number region=\"Russia\">8-900-000-00-00</number><nullValue /></transaction>";
		XMLElement e = sut.traverse(xml);
		assertThat(e).extracting("tagName","Text","parent").containsExactly("transaction","",null);
		assertThat(e.getChildren()).extracting("tagName","Text","parent").containsExactly(tuple("id","6753322",e),tuple("number","8-900-000-00-00",e),tuple("nullValue",null,e));
		assertThat(e.getChildren().get(1).getAttributes()).containsEntry("region", "Russia");
		
	}
}
