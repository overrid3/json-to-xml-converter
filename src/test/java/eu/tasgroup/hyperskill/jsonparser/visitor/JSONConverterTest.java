package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

class JSONConverterTest {

	private JSONConverter sut;

	@BeforeEach
	public void initSut() {
		sut = new JSONConverter();
	}

	@DisplayName("Testing conversion from JSON to XML with one key-one value element")
	@Test
	public void convertTest1() {

		JSONElement e = new JSONElement();
		e.setKey("wanagana");
		e.setValue("utrecht");

		XMLElement xmlE = sut.convert(e);

		assertThat(xmlE.getTagName()).isEqualTo("wanagana");
		assertThat(xmlE.getText()).isEqualTo("utrecht");
		assertThat(xmlE.getChildren()).isEmpty();
		assertThat(xmlE.getParent()).isNull();
	}

	@DisplayName("Testing conversion from JSON to XML of a complex element")
	@Test
	public void convertTest2() {

		JSONElement e = new JSONElement("wanagana", null);
		JSONElement e1 = new JSONElement("uri", 123);
		JSONElement e2 = new JSONElement("iut", true);

		e1.setParent(e);
		e2.setParent(e);

		e.addChild(e1);
		e.addChild(e2);

		XMLElement xmlE = sut.convert(e);

		assertThat(xmlE.getTagName()).isEqualTo("wanagana");
		assertThat(xmlE.getChildren()).isNotEmpty();
		assertThat(xmlE.getParent()).isNull();

		assertThat(xmlE.getChildren().get(0).getTagName()).isEqualTo("uri");
		assertThat(xmlE.getChildren().get(0).getText()).isEqualTo("123");
		assertThat(xmlE.getChildren().get(0).getParent().getTagName()).isEqualTo("wanagana");

		assertThat(xmlE.getChildren().get(1).getTagName()).isEqualTo("iut");
		assertThat(xmlE.getChildren().get(1).getText()).isEqualTo("true");
		assertThat(xmlE.getChildren().get(1).getParent().getTagName()).isEqualTo("wanagana");
	}

	@DisplayName("testing conversion with null JSON Element")
	@Test
	public void convertTest3() {
		assertThatThrownBy(() -> sut.convert(null))
		.isInstanceOf(NullPointerException.class)
		.hasMessage(JSONConverter.JSON_ELEMENT_CAN_T_BE_NULL);
	}

	@Test
	public void convertAttributesAndValue(){

	}

	@DisplayName("Testing conversion from JSON to XML of a complex element with fake attributes for xml")
	@Test
	public void convertTest4() {
		//"key" : { "@asdsa" : "asdsad", "#key" : "kkkkk", "asdad": "val" } 
		//-> "<key> <asdsa>asdsad</asdsa> <key>kkkkk</key> <asdad>val</asdaad> </key>"

		JSONElement e = new JSONElement("key", null);
		JSONElement e1 = new JSONElement("@asdsa", "asdsad");
		JSONElement e2 = new JSONElement("#key", "kkkkk");
		JSONElement e3 = new JSONElement("asdad", "val");

		e1.setParent(e);
		e2.setParent(e);
		e3.setParent(e);

		e.addChild(e1);
		e.addChild(e2);
		e.addChild(e3);

		XMLElement xmlE = sut.convert(e);

		assertThat(xmlE)
		.extracting("tagName", "text", "parent")
		.containsExactly("key",null,null);

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(
				tuple("asdsa", "asdsad", xmlE),
				tuple("key", "kkkkk", xmlE),
				tuple("asdad","val", xmlE));

	}

	@DisplayName("Testing conversion from JSON to XML of a complex element with fake attributes for xml second type")
	@Test
	public void convertTest5() {
		//"key" : { "@asdsa" : "asdsad", "#key" : "kkkkk" } 
		//-> "<key asdsa="asdsad">kkkkk</key>"

		JSONElement e = new JSONElement("key", null);
		JSONElement e1 = new JSONElement("@asdsa", "asdsad");
		JSONElement e2 = new JSONElement("#key", "kkkkk");

		e1.setParent(e);
		e2.setParent(e);

		e.addChild(e1);
		e.addChild(e2);

		XMLElement xmlE = sut.convert(e);

		assertThat(xmlE)
		.extracting("tagName", "text", "parent")
		.containsExactly("key", "kkkkk", null);

		assertThat(xmlE.getAttributes()).containsEntry( "asdsa", "asdsad");
	}

	@DisplayName("Testing conversion from JSON to XML of a complex element with attributes and one child")
	@Test
	public void convertTest6() {
		//"key" : { "@asdsa" : "asdsad", "#key" : {"figlioincomodo": 6} }
		//-> "<key asdsa="asdsad"> <figlioincomodo>6</figlioincomodo> </key>"

		JSONElement e = new JSONElement("key", null);
		JSONElement e1 = new JSONElement("@asdsa", "asdsad");
		JSONElement e2 = new JSONElement("#key", null);
		JSONElement e21 = new JSONElement("figlioincomodo", 6);

		e1.setParent(e);
		e2.setParent(e);
		e21.setParent(e2);

		e.addChild(e1);
		e.addChild(e2);
		e2.addChild(e21);

		XMLElement xmlE = sut.convert(e);

		assertThat(xmlE)
		.extracting("tagName", "text", "parent")
		.containsExactly("key",null, null);

		assertThat(xmlE.getAttributes()).containsEntry( "asdsa", "asdsad");

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text",  "parent")
		.containsExactly(tuple("figlioincomodo", "6",xmlE));
	}

	@DisplayName("Testing conversion from JSON to XML of a complex element with attributes and children")
	@Test
	public void convertTest7() {
		//"key" : { "@asdsa" : "asdsad", "#key" : {"figlioincomodo": 6, "ciao" : "mondo"} }
		//-> "<key asdsa="asdsad"> <figlioincomodo>6</figlioincomodo> </key>"

		JSONElement e = new JSONElement("key", null);
		JSONElement e1 = new JSONElement("@asdsa", "asdsad");
		JSONElement e2 = new JSONElement("#key", null);
		JSONElement e21 = new JSONElement("figlioincomodo", 6);
		JSONElement e22 = new JSONElement("ciao", "mondo");

		e1.setParent(e);
		e2.setParent(e);
		e21.setParent(e2);
		e22.setParent(e2);

		e.addChild(e1);
		e.addChild(e2);
		e2.addChild(e21);
		e2.addChild(e22);

		XMLElement xmlE = sut.convert(e);

		assertThat(xmlE)
		.extracting("tagName", "text", "parent")
		.containsExactly("key",null, null);

		assertThat(xmlE.getAttributes()).containsEntry( "asdsa", "asdsad");

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text",  "parent")
		.containsExactly(tuple("figlioincomodo", "6",xmlE),tuple("ciao", "mondo",xmlE));
	}

	@DisplayName("Testing conversion from JSON to XML of a complex element with  two attributes and children")
	@Test
	public void convertTest8() {
		//"key" : { "@asdsa" : "asdsad","@buon" : "weekend", "#key" : {"figlioincomodo": 6, "ciao" : "mondo"} }
		//-> "<key asdsa="asdsad"> <figlioincomodo>6</figlioincomodo> </key>"

		JSONElement e = new JSONElement("key", null);
		JSONElement e1 = new JSONElement("@asdsa", "asdsad");
		JSONElement e2 = new JSONElement("@buon", "weekend");
		JSONElement e3 = new JSONElement("#key", null);
		JSONElement e11 = new JSONElement("figlioincomodo", 6);
		JSONElement e12 = new JSONElement("ciao", "mondo");

		e1.setParent(e);
		e2.setParent(e);
		e3.setParent(e);
		e11.setParent(e3);
		e12.setParent(e3);

		e.addChild(e1);
		e.addChild(e2);
		e.addChild(e3);
		e3.addChild(e11);
		e3.addChild(e12);

		XMLElement xmlE = sut.convert(e);

		assertThat(xmlE)
		.extracting("tagName", "text", "parent")
		.containsExactly("key",null, null);

		assertThat(xmlE.getAttributes()).containsEntry( "asdsa", "asdsad");
		assertThat(xmlE.getAttributes()).containsEntry("buon", "weekend");

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text",  "parent")
		.containsExactly(tuple("figlioincomodo", "6",xmlE),tuple("ciao", "mondo",xmlE));
	}


}