package eu.tasgroup.hyperskill.jsonparser.printer;
import static eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor.XMLOBJECT_CANNOT_BE_NULL;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XMLPrinterTest {

	XMLPrinter sut = new XMLPrinter();

	@Test
	@DisplayName("null element")
	public void nullTest() {

		assertThatThrownBy(() -> sut.printToXMLFormat(null))
		.isInstanceOf(NullPointerException.class)
		.hasMessage(XMLOBJECT_CANNOT_BE_NULL);
	}

	@Test
	@DisplayName("prova stampa un elemento con figli")
	public void printTry(){

		XMLElement root=new XMLElement();
		XMLElement transaction=new XMLElement("transaction","");
		XMLElement id=new XMLElement("id","385938");
		XMLElement region = new XMLElement("region","");
		XMLElement e4=new XMLElement("mannaggia", "");
		XMLElement e5= new XMLElement("port","pol");
		XMLElement nullElement=new XMLElement("fre",null);

		id.insertAttributeEntry("mannaggia","mannaggia");
		id.insertAttributeEntry("ciao","ciao");

		root.addChild(transaction);
		transaction.addChild(id);
		transaction.addChild(nullElement);
		transaction.addChild(region);
		region.addChild(e4);
		e4.addChild(e5);
		String expected = "<transaction>" + 
				"<id mannaggia=\"mannaggia\" ciao=\"ciao\">385938</id>" + 
				"<fre />" + 
				"<region>" + 
				"<mannaggia>" + 
				"<port>pol</port>" + 
				"</mannaggia>" + 
				"</region>" + 
				"</transaction>";
		String actual = sut.printToXMLFormat(root);
		XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();
	}

	@Test
	@DisplayName("prova stampa due elementi")
	public void printTry2(){

		XMLElement root=new XMLElement();
		XMLElement transaction=new XMLElement("transaction","");
		XMLElement id=new XMLElement("id","385938");
		XMLElement meta = new XMLElement("meta","");
		XMLElement version = new XMLElement("version", "0.01");

		root.addChild(transaction);
		root.addChild(meta);
		transaction.addChild(id);
		meta.addChild(version);

		String expected = "<root><transaction>" + 
				"<id>385938</id>" + 
				"</transaction>" + 
				"<meta>" + 
				"<version>0.01</version>" + 
				"</meta>" + 
				"</root>";
		String actual = sut.printToXMLFormat(root);
		XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();
	}

}
