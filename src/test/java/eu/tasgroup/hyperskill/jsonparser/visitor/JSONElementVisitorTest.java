package eu.tasgroup.hyperskill.jsonparser.visitor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

class JSONElementVisitorTest {
	JSONElementVisitor sut = new JSONElementVisitor();

	//null al metodo
	@Test
	@DisplayName("visit null element")
	public void xmlElementNullTest(){

		assertThatThrownBy(() -> sut.createString(null))
		.isInstanceOf(NullPointerException.class)
		.hasMessage(JSONElementVisitor.XMLOBJECT_CANNOT_BE_NULL);

	}

	@Test
	@DisplayName("print only one tag")
	public void elementVisitorOneTagNoValueNoAttributesTest() {
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("id");
		xmlElement.setText("");
	
		String stringExpected =  "\nElement:\npath = id\nvalue = \"\"\n";
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print one tag one attribute")
	public void elementVisitorOneTagNoValueOneAttributeTest() {
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("id");
		xmlElement.setText("");
		xmlElement.insertAttributeEntry("region", "Russia");
	
		String stringExpected =  "\nElement:\npath = id" 
				+ "\nvalue = \"\"\n" 
				+ "attributes:\nregion = \"Russia\"\n"; 		
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print one tag two attributes")
	public void elementVisitorOneTagNoValueMultipleAttributesTest() {
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("id");
		xmlElement.setText("");
		xmlElement.insertAttributeEntry("region", "Russia");
		xmlElement.insertAttributeEntry("region1", "Russia1");
		
		String stringExpected =  "\nElement:\npath = id"
				+ "\nvalue = \"\"\n" 
				+ "attributes:\nregion = \"Russia\"\n"
				+ "region1 = \"Russia1\"\n";
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print one tag with value")
	public void elementVisitorOneTagOneValueNoAttributesTest() {
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("id");
		xmlElement.setText("6753322");
		
		String stringExpected =  "\nElement:\npath = id\nvalue = \"6753322\"\n";
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print one tag with value and one attribute")
	public void elementVisitorOneTagOneValueOneAttributeTest() {
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("id");
		xmlElement.setText("6753322");
		xmlElement.insertAttributeEntry("region", "Russia");
		
		String stringExpected =  "\nElement:\npath = id" 
				+ "\nvalue = \"6753322\"\n" 
				+ "attributes:\nregion = \"Russia\"\n"; 		
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}
	
	@Test
	@DisplayName("no print root")
	public void elementVisitorDocumentElementTest() {
		XMLElement xmlElement = new XMLElement();
		XMLElement xmlElementChild = new XMLElement();
		xmlElementChild.setTagName("id");
		xmlElementChild.setText("6753322");
		xmlElementChild.insertAttributeEntry("region", "Russia");
		xmlElement.addChild(xmlElementChild);
		
		String stringExpected =  "\nElement:\npath = id" 
				+ "\nvalue = \"6753322\"\n" 
				+ "attributes:\nregion = \"Russia\"\n"; 		
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print one tag with value and two attributes")
	public void elementVisitorOneTagOneValueMultipleAttributesTest() {
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("id");
		xmlElement.setText("123");
		xmlElement.insertAttributeEntry("region", "Russia");
		xmlElement.insertAttributeEntry("region1", "Russia1");

		String stringExpected =  "\nElement:\npath = id" 
				+ "\nvalue = \"123\"\n" 
				+ "attributes:\nregion = \"Russia\"\n"
				+ "region1 = \"Russia1\"\n";
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print one tag with null value")
	public void elementVisitorOneTagNullValueNoAttributesTest() {
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("id");
		xmlElement.setText(null);
		
		String stringExpected =  "\nElement:\npath = id\nvalue = null\n";
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print innested tags with value")
	public void elementVisitorInnestedTagsNoAttributesTest() {
		XMLElement xmlElementChild = new XMLElement();
		xmlElementChild.setTagName("to");
		xmlElementChild.setText("to_example@gmail.com");
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("email");
		xmlElement.addChild(xmlElementChild);
		
		String stringPath = "\nElement:\npath = email\n\nElement:\npath = email, to"; 
		String stringValue = "\nvalue = \"to_example@gmail.com\"\n";

		String stringExpected = stringPath + stringValue;
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print innested tags with value and attribute")
	public void elementVisitorInnestedTagsOneAttributeTest() {
		XMLElement xmlElementChild = new XMLElement();
		xmlElementChild.setTagName("to");
		xmlElementChild.setText("to_example@gmail.com");
		xmlElementChild.insertAttributeEntry("region", "Russia");
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("email");
		xmlElement.addChild(xmlElementChild);

		String stringPath = "\nElement:\npath = email\n\nElement:\npath = email, to"; 
		String stringValue = "\nvalue = \"to_example@gmail.com\"\n";
		String stringAttributes = "attributes:\nregion = \"Russia\"\n";

		String stringExpected = stringPath + stringValue + stringAttributes;
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print innested tags with value and multiple attributes")
	public void elementVisitorInnestedTagsMultipleAttributesTest() {
		XMLElement xmlElementChild = new XMLElement();
		xmlElementChild.setTagName("to");
		xmlElementChild.setText("to_example@gmail.com");
		xmlElementChild.insertAttributeEntry("region", "Russia");
		xmlElementChild.insertAttributeEntry("region1", "Russia1");
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("email");
		xmlElement.addChild(xmlElementChild);

		String stringPath = "\nElement:\npath = email\n\nElement:\npath = email, to"; 
		String stringValue = "\nvalue = \"to_example@gmail.com\"\n";
		String stringAttributes = "attributes:\nregion = \"Russia\"\nregion1 = \"Russia1\"\n";

		String stringExpected = stringPath + stringValue + stringAttributes;
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print innested tags with father with attribute")
	public void elementVisitorTagFatherOneAttributeTest() {
		XMLElement xmlElementChild = new XMLElement();
		xmlElementChild.setTagName("to");
		xmlElementChild.setText("to_example@gmail.com");
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("email");
		xmlElement.addChild(xmlElementChild);
		xmlElement.insertAttributeEntry("region", "Russia");
		
		String stringPathFather = "\nElement:\npath = email\n";
		String attributeFather = "attributes:\nregion = \"Russia\"\n";
		String stringPathSon = "\nElement:\npath = email, to";
		String stringValue = "\nvalue = \"to_example@gmail.com\"\n";

		String stringExpected = stringPathFather + attributeFather + stringPathSon + stringValue;
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print innested tags with father with multiple attributes")
	public void elementVisitorTagFatherMultipleAttributesTest() {
		XMLElement xmlElementChild = new XMLElement();
		xmlElementChild.setTagName("to");
		xmlElementChild.setText("to_example@gmail.com");
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("email");
		xmlElement.addChild(xmlElementChild);
		xmlElement.insertAttributeEntry("region", "Russia");
		xmlElement.insertAttributeEntry("region1", "Russia1");

		String stringPathFather = "\nElement:\npath = email\n";
		String attributeFather = "attributes:\nregion = \"Russia\"\nregion1 = \"Russia1\"\n";
		String stringPathSon = "\nElement:\npath = email, to";
		String stringValue = "\nvalue = \"to_example@gmail.com\"\n";

		String stringExpected = stringPathFather + attributeFather + stringPathSon + stringValue;
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

	@Test
	@DisplayName("print innested tags with father and child with attribute")
	public void elementVisitorTagFatherTagSonOneAttributeTest() {
		XMLElement xmlElementChild = new XMLElement();
		xmlElementChild.setTagName("to");
		xmlElementChild.setText("to_example@gmail.com");
		xmlElementChild.insertAttributeEntry("region1", "Russia1");
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("email");
		xmlElement.addChild(xmlElementChild);
		xmlElement.insertAttributeEntry("region", "Russia");
		
		String stringPathFather = "\nElement:\npath = email\n";
		String attributeFather = "attributes:\nregion = \"Russia\"\n";
		String stringPathSon = "\nElement:\npath = email, to";
		String stringValue = "\nvalue = \"to_example@gmail.com\"\n";
		String attributeSon = "attributes:\nregion1 = \"Russia1\"\n";

		String stringExpected = stringPathFather + attributeFather + stringPathSon + stringValue + attributeSon;
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}
	
	@Test
	@DisplayName("print innested tags with father with two children")
	public void elementVisitorTagFatherAndChildrenTest() {
		XMLElement xmlElementChild1 = new XMLElement();
		xmlElementChild1.setTagName("to");
		xmlElementChild1.setText("to_example@gmail.com");
		XMLElement xmlElementChild2 = new XMLElement();
		xmlElementChild2.setTagName("to1");
		xmlElementChild2.setText("to_example@gmail");
		
		XMLElement xmlElement = new XMLElement();
		xmlElement.setTagName("email");
		xmlElement.addChild(xmlElementChild1);
		xmlElement.addChild(xmlElementChild2);
		
		String stringPathFather = "\nElement:\npath = email\n";
		String stringPathSon1 = "\nElement:\npath = email, to";
		String stringValue1 = "\nvalue = \"to_example@gmail.com\"\n";
		String stringPathSon2 = "\nElement:\npath = email, to1";
		String stringValue2 = "\nvalue = \"to_example@gmail\"\n";

		String stringExpected = stringPathFather + stringPathSon1 + stringValue1 + stringPathSon2 + stringValue2;
		String stringVisitor = sut.createString(xmlElement);

		assertThat(stringVisitor).isEqualToNormalizingNewlines(stringExpected);
	}

}
