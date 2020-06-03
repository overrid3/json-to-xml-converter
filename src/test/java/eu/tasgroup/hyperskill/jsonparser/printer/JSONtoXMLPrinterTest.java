package eu.tasgroup.hyperskill.jsonparser.printer;
import static eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor.XMLOBJECT_CANNOT_BE_NULL;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JSONtoXMLPrinterTest {

	JSONtoXMLPrinter sut = new JSONtoXMLPrinter();

	@Test
	@DisplayName("prova stampa")
	public void printTry(){

		XMLElement root=new XMLElement();
		XMLElement transaction=new XMLElement("transaction","");
		XMLElement id=new XMLElement("id","385938");
		XMLElement region = new XMLElement("region","");
		XMLElement e4=new XMLElement("mannaggia", "");
		XMLElement e5= new XMLElement("port","pol");
		XMLElement nullElement=new XMLElement("fre",null);

		id.insertAttributeEntry("mannaggia","\"mannaggia\"");
		id.insertAttributeEntry("\"ciao","\"ciao");

		root.addChild(transaction);
		transaction.addChild(id);
		transaction.addChild(nullElement);
		transaction.addChild(region);
		region.addChild(e4);
		e4.addChild(e5);

		sut.printToXMLFormat(root);
	}

	/*@Test
	@DisplayName("null single attribute")
	public void nullTest() {

		assertThatThrownBy(() -> sut.printToXMLFormat(null))
		.isInstanceOf(NullPointerException.class)
		.hasMessage(XMLOBJECT_CANNOT_BE_NULL);
	}

	@DisplayName("test with one simple child")
	@Test
	public void printer1Test() {
		XMLElement root = new XMLElement();
		XMLElement e = new XMLElement();
		XMLElement e1 = new XMLElement();
		XMLElement e2 = new XMLElement();
		
		e.setTagName("tag1");
		e1.setTagName("child");
		e1.setText("childValue");
		e2.setTagName("child");
		e2.setText("childValue");
		root.addChild(e);
		e.addChild(e1);
		e.addChild(e2);
		
		String actual = sut.printToXMLFormat(e);
		String expected = "<tag1><child>childValue</child></tag1>";
		
	//	assertThat(actual).isEqualTo(expected);
		System.out.println(actual);

	}*/

}
