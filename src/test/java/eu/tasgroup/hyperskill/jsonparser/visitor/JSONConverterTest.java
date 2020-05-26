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
	public void convertAttributesAndValue() {

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
		.containsExactly("key", null, null);

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(
				tuple("asdsa", "asdsad", xmlE),
				tuple("key", "kkkkk", xmlE),
				tuple("asdad", "val", xmlE));

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

		assertThat(xmlE.getAttributes()).containsEntry("asdsa", "asdsad");
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
		.containsExactly("key", null, null);

		assertThat(xmlE.getAttributes()).containsEntry("asdsa", "asdsad");

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(tuple("figlioincomodo", "6", xmlE));
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
		.containsExactly("key", null, null);

		assertThat(xmlE.getAttributes()).containsEntry("asdsa", "asdsad");

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(tuple("figlioincomodo", "6", xmlE), tuple("ciao", "mondo", xmlE));
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
		.containsExactly("key", null, null);

		assertThat(xmlE.getAttributes()).containsEntry("asdsa", "asdsad");
		assertThat(xmlE.getAttributes()).containsEntry("buon", "weekend");

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(tuple("figlioincomodo", "6", xmlE), tuple("ciao", "mondo", xmlE));
	}

	@DisplayName("Testing conversion of invalid keys")
	@Test
	public void convertTest9() {
		//{"key" : { "@asdsa" : "asdsad", "#key" : "burton"}, "junior" : {"@":"lkjdf","#":"deoijdio"}}
		//-> "<key asdsa="asdsad"> <figlioincomodo>6</figlioincomodo> </key>"

		JSONElement root = new JSONElement("root", null);
		JSONElement e1 = new JSONElement("key", null);
		JSONElement e2 = new JSONElement("@asdsa", "asdsad");
		JSONElement e3 = new JSONElement("#key", "burton");
		JSONElement e4 = new JSONElement("junior", null);
		JSONElement e5 = new JSONElement("@", "oeowh");
		JSONElement e6 = new JSONElement("#", "oiadfhdi");

		//AGGIUNTA FIGLI AI SINGOLI NODI
		root.addChild(e1);
		root.addChild(e4);
		e1.addChild(e2);
		e1.addChild(e3);
		e4.addChild(e5);
		e4.addChild(e6);

		XMLElement xmlE = sut.convert(root);

		assertThat(xmlE)
		.extracting("tagName", "text", "parent")
		.containsExactly("root", null, null);

		assertThat(xmlE.getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(tuple("key", "burton", xmlE), tuple("junior", "", xmlE));

		assertThat(xmlE.getChildren().get(0).getTagName()).isEqualTo("key");
		assertThat(xmlE.getChildren().get(0).getText()).isEqualTo("burton");
		assertThat(xmlE.getChildren().get(0).getAttributes())
		.containsEntry("asdsa", "asdsad");

		assertThat(xmlE.getChildren().get(1).getAttributes()).isEmpty();
		assertThat(xmlE.getChildren().get(1).getChildren()).isEmpty();
	}

	@DisplayName("Testing conversion with empty key")
	@Test
	public void convertTest10() {

		//{"":{"@":JFK, "#": "iouoee"},"example":{"@dre":"dre","#example":"fre"}}

		JSONElement root = new JSONElement("root", null);
		JSONElement e1 = new JSONElement("", null);
		JSONElement e2 = new JSONElement("@", "JFKF");
		JSONElement e3 = new JSONElement("#", "iouoee");
		JSONElement e4 = new JSONElement("example", null);
		JSONElement e5 = new JSONElement("@dre", "dre");
		JSONElement e6 = new JSONElement("#example", "fre");

		root.addChild(e1);
		root.addChild(e4);
		e1.addChild(e2);
		e1.addChild(e3);
		e4.addChild(e5);
		e4.addChild(e6);


		XMLElement xmlE = sut.convert(root);

		assertThat(xmlE.getTagName()).isEqualTo("root");
		assertThat(xmlE.getText()).isEqualTo(null);
		assertThat(xmlE.getAttributes()).isEmpty();
		assertThat(xmlE.getChildren().size()).isEqualTo(1);
		assertThat(xmlE.getChildren().get(0).getTagName()).isEqualTo("example");
		assertThat(xmlE.getChildren().get(0).getText()).isEqualTo("fre");
		assertThat(xmlE.getChildren().get(0).getAttributes()).containsEntry("dre","dre");
		assertThat(xmlE.getChildren().get(0).getChildren()).isEmpty();
	}

	@DisplayName("Testing conversion with strange entries")
	@Test
	public void convertTest11(){

		/*"inner4": {
            "@": 123,
                    "#inner4": "value3"
        },
        "inner4.2": {
            "": 123,
                    "#inner4.2": "value3"
        }*/

		JSONElement root=new JSONElement("root",null);
		JSONElement e1 = new JSONElement("inner4", null);
		JSONElement e2 = new JSONElement("@", 123);
		JSONElement e3 = new JSONElement("#inner4", "value3");
		JSONElement e4 = new JSONElement("inner4.2", null);
		JSONElement e5 = new JSONElement("@", 123);
		JSONElement e6 = new JSONElement("#inner4.2", "value3");

		root.addChild(e1);
		root.addChild(e4);

		e1.addChild(e2);
		e1.addChild(e3);

		e4.addChild(e5);
		e4.addChild(e6);

		XMLElement e=sut.convert(root);

		assertThat(e.getTagName()).isEqualTo("root");
		assertThat(e.getText()).isEqualTo(null);
		assertThat(e.getAttributes()).isEmpty();
		assertThat(e.getChildren().size()).isEqualTo(2);
		assertThat(e.getChildren().get(0).getTagName()).isEqualTo("inner4");
		assertThat(e.getChildren().get(0).getChildren().size()).isEqualTo(1);
		assertThat(e.getChildren().get(0).getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(tuple("inner4","value3",e.getChildren().get(0)));
		assertThat(e.getChildren().get(1).getChildren().size()).isEqualTo(1);
		assertThat(e.getChildren().get(1).getChildren())
		.extracting("tagName", "text", "parent")
		.containsExactly(tuple("inner4.2","value3",e.getChildren().get(1)));
	}

	@DisplayName("Converting test with with only hash key")
	@Test
	public void convertTest12(){

		JSONElement root=new JSONElement("root",null);
		JSONElement e1 = new JSONElement("inner7", null);
		JSONElement e2 = new JSONElement("#inner7", "eeeeee duuuuuuaaaajeeeeee");

		root.addChild(e1);
		e1.addChild(e2);

		XMLElement e=sut.convert(root);

		assertThat(e.getTagName()).isEqualTo("root");
		assertThat(e.getText()).isEqualTo(null);
		assertThat(e.getChildren())
		.extracting("tagName", "text", "parent")
		.contains(tuple("inner7","eeeeee duuuuuuaaaajeeeeee",e));
		assertThat(e.getChildren().get(0).getAttributes()).isEmpty();
	}

	@DisplayName("Testing conversion of JSON objects with null values")
	@Test
	public void convertTest13(){

		JSONElement root=new JSONElement("root",null);
		JSONElement e1 = new JSONElement("inner7", null);
		JSONElement e2 = new JSONElement("@key1", null);
		JSONElement e3 = new JSONElement("#inner7", null);

		root.addChild(e1);
		e1.addChild(e2);
		e1.addChild(e3);

		XMLElement e=sut.convert(root);

		assertThat(e.getTagName()).isEqualTo("root");
		assertThat(e.getText()).isNull();
		assertThat(e.getChildren().size()).isEqualTo(1);
		assertThat(e.getAttributes()).isEmpty();

		assertThat(e.getChildren().get(0).getTagName()).isEqualTo("inner7");
		assertThat(e.getChildren().get(0).getText()).isEqualTo("null");
		assertThat(e.getChildren().get(0).getChildren()).isEmpty();
		assertThat(e.getChildren().get(0).getAttributes()).containsEntry("key1","null");
	}

	@DisplayName("Testing conversion of JSON object keys for attributes and values with same name")
	@Test
	public void convertTest14() {
		//key:{@child:ciao,#key1:cc,key1:aa} => key:{@child:ciao,key1:aa}

		JSONElement e = new JSONElement("key",null);
		JSONElement e1 = new JSONElement("@child","ciao");
		JSONElement e2 = new JSONElement("#key1","cc");
		JSONElement e3 = new JSONElement("key1","aa");


		e.addChild(e1);
		e.addChild(e2);
		e.addChild(e3);


		XMLElement el=sut.convert(e);

		assertThat(el)
		.extracting("tagName", "text", "parent")
		.containsExactly("key", null, null);
		
		assertThat(el.getChildren().size()).isEqualTo(2);
		assertThat(el.getChildren())
		.extracting("tagName", "text", "parent")
		.contains(tuple("child","ciao",el),tuple("key1","aa",el));
	}

}