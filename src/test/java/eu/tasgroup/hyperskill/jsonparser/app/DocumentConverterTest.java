package eu.tasgroup.hyperskill.jsonparser.app;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;

class DocumentConverterTest {

	DocumentConverter sut = new DocumentConverter();
	
	@DisplayName("from Json to Xml")
	@Test
	void test1() throws IOException {
		String path = "firstExample.json";
		String expected = "<key>" + 
				"	<child_key1>child_key_value</child_key1>" + 
				"	<child_key2>child_key_value</child_key2>" + 
				"	<child_key3 />" + 
				"	<child_key4 />" + 
				"</key>"; 
        String actual = sut.analyzeAndConvert(path);
        XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();

	}

}
