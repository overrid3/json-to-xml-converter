package eu.tasgroup.hyperskill.jsonparser.app;

import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
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

    @DisplayName("second conversion example of a JSON object into XML")
    @Test
    void test2() throws IOException {

        String path = "second.json";

        String expected = "<transaction>" +
                "    <id>6753322</id>" +
                "    <number region=\"Russia\">8-900-000-00-00</number>" +
                "    <amount />" +
                "</transaction>";

        String actual = sut.analyzeAndConvert(path);

        XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();
    }

    @DisplayName("third conversion example from JSON to XML")
    @Test
    void test3() throws IOException {

        String path = "third.json";

        String expected = "<root>" +
                "    <transaction>" +
                "        <id>6753322</id>" +
                "        <number region=\"Russia\">8-900-000-00-00</number>" +
                "        <amount />" +
                "    </transaction>" +
                "    <meta>" +
                "        <version>0.01</version>" +
                "    </meta>" +
                "</root>";

        String actual = sut.analyzeAndConvert(path);

        XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();

    }

    @DisplayName("fourth conversion example from JSON to XML")
    @Test
    void test4() throws IOException {

        String path = "fourth.json";

        String expected = "<root>" +
                "    <transaction>" +
                "        <id>6753322</id>" +
                "        <number region=\"Russia\">8-900-000-000</number>" +
                "        <empty1 />" +
                "        <empty2></empty2>" +
                "        <empty3></empty3>" +
                "        <inner1>" +
                "            <inner2>" +
                "                <inner3>" +
                "                    <key1>value1</key1>" +
                "                    <key2>value2</key2>" +
                "                </inner3>" +
                "            </inner2>" +
                "        </inner1>" +
                "        <inner4>" +
                "            <inner4>value3</inner4>" +
                "        </inner4>" +
                "        <inner5>" +
                "            <attr1>123.456</attr1>" +
                "            <inner4>value4</inner4>" +
                "        </inner5>" +
                "        <inner6 attr2=\"789.321\">value5</inner6>" +
                "        <inner7>value6</inner7>" +
                "        <inner8>" +
                "            <attr3>value7</attr3>" +
                "        </inner8>" +
                "        <inner9>" +
                "            <attr4>value8</attr4>" +
                "            <inner9>value9</inner9>" +
                "            <something>value10</something>" +
                "        </inner9>" +
                "        <inner10 attr5=\"\" />" +
                "        <inner11></inner11>" +
                "        <inner12>" +
                "            <somekey>keyvalue</somekey>" +
                "            <inner12>notnull</inner12>" +
                "        </inner12>" +
                "    </transaction>" +
                "    <meta>" +
                "        <version>0.01</version>" +
                "    </meta>" +
                "</root>";

        String actual = sut.analyzeAndConvert(path);

        XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();
    }

    @DisplayName("Particular case: print XML object from a JSON element with not childless hash key") //TEST NON FUNZIONA
    @Test
    void test5() throws IOException {

        String path="fifth.json";

        String expected="<elem1 attr1=\"val1\" attr2=\"val2\">" +
                "    <elem2 attr3=\"val3\" attr4=\"val4\">Value1</elem2>" +
                "    <elem3 attr5=\"val5\" attr6=\"val6\">Value2</elem3>" +
                "</elem1>";

        String actual = sut.analyzeAndConvert(path);

        XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();
    }

    @DisplayName("First conversion test from XML to JSON")
    @Test
    void test6() throws JSONException, IOException {

        String path="first.xml";

        String expected="{" +
                "    \"root\": {" +
                "        \"id\": \"6753322\"," +
                "        \"number\": {" +
                "            \"@region\": \"Russia\"," +
                "            \"#number\": \"8-900-000-00-00\"" +
                "        }," +
                "        \"nonattr1\": null," +
                "        \"nonattr2\": \"\"," +
                "        \"nonattr3\": \"text\"," +
                "        \"attr1\": {" +
                "            \"@id\": \"1\"," +
                "            \"#attr1\": null" +
                "        }," +
                "        \"attr2\": {" +
                "            \"@id\": \"2\"," +
                "            \"#attr2\": \"\"" +
                "        }," +
                "        \"attr3\": {" +
                "            \"@id\": \"3\"," +
                "            \"#attr3\": \"text\"" +
                "        }," +
                "        \"email\": {" +
                "            \"to\": \"to_example@gmail.com\"," +
                "            \"from\": \"from_example@gmail.com\"," +
                "            \"subject\": \"Project discussion\"," +
                "            \"body\": {" +
                "                \"@font\": \"Verdana\"," +
                "                \"#body\": \"Body message\"" +
                "            }," +
                "            \"date\": {" +
                "                \"@day\": \"12\"," +
                "                \"@month\": \"12\"," +
                "                \"@year\": \"2018\"," +
                "                \"#date\": null" +
                "            }" +
                "        }" +
                "    }" +
                "}";

        String actual = sut.analyzeAndConvert(path);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @DisplayName("Second conversion test from XML to JSON")
    @Test
    void test7() throws JSONException, IOException {

        String path="second.xml";

        String expected="{" + 
        		"    \"elem1\": {" + 
        		"        \"@attr1\": \"val1\"," + 
        		"        \"@attr2\": \"val2\"," + 
        		"        \"#elem1\": {" + 
        		"            \"elem2\": {" + 
        		"                \"@attr3\": \"val3\"," + 
        		"                \"@attr4\": \"val4\"," + 
        		"                \"#elem2\": \"Value1\"" + 
        		"            }," + 
        		"            \"elem3\": {" + 
        		"                \"@attr5\": \"val5\"," + 
        		"                \"@attr6\": \"val6\"," + 
        		"                \"#elem3\": \"Value2\"" + 
        		"            }" + 
        		"        }" + 
        		"    }" + 
        		"}";

        String actual = sut.analyzeAndConvert(path);


        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @DisplayName("fourth conversion example from JSON to XML one line")
    @Test
    void test8() throws IOException {

        String path = "first.json";

        String expected = "<root>" +
                "    <transaction>" +
                "        <id>6753322</id>" +
                "        <number region=\"Russia\">8-900-000-000</number>" +
                "        <empty1 />" +
                "        <empty2></empty2>" +
                "        <empty3></empty3>" +
                "        <inner1>" +
                "            <inner2>" +
                "                <inner3>" +
                "                    <key1>value1</key1>" +
                "                    <key2>value2</key2>" +
                "                </inner3>" +
                "            </inner2>" +
                "        </inner1>" +
                "        <inner4>" +
                "            <inner4>value3</inner4>" +
                "        </inner4>" +
                "        <inner5>" +
                "            <attr1>123.456</attr1>" +
                "            <inner4>value4</inner4>" +
                "        </inner5>" +
                "        <inner6 attr2=\"789.321\">value5</inner6>" +
                "        <inner7>value6</inner7>" +
                "        <inner8>" +
                "            <attr3>value7</attr3>" +
                "        </inner8>" +
                "        <inner9>" +
                "            <attr4>value8</attr4>" +
                "            <inner9>value9</inner9>" +
                "            <something>value10</something>" +
                "        </inner9>" +
                "        <inner10 attr5=\"\" />" +
                "        <inner11></inner11>" +
                "        <inner12>" +
                "            <somekey>keyvalue</somekey>" +
                "            <inner12>notnull</inner12>" +
                "        </inner12>" +
                "    </transaction>" +
                "    <meta>" +
                "        <version>0.01</version>" +
                "    </meta>" +
                "</root>";

        String actual = sut.analyzeAndConvert(path);

        XmlAssert.assertThat(actual).and(expected).normalizeWhitespace().areIdentical();
    }
    
    @DisplayName("Conversion test from XML to JSON one line")
    @Test
    void test9() throws JSONException, IOException {

        String path="third.xml";

        String expected="{" +
                "    \"root\": {" +
                "        \"id\": \"6753322\"," +
                "        \"number\": {" +
                "            \"@region\": \"Russia\"," +
                "            \"#number\": \"8-900-000-00-00\"" +
                "        }," +
                "        \"nonattr1\": null," +
                "        \"nonattr2\": \"\"," +
                "        \"nonattr3\": \"text\"," +
                "        \"attr1\": {" +
                "            \"@id\": \"1\"," +
                "            \"#attr1\": null" +
                "        }," +
                "        \"attr2\": {" +
                "            \"@id\": \"2\"," +
                "            \"#attr2\": \"\"" +
                "        }," +
                "        \"attr3\": {" +
                "            \"@id\": \"3\"," +
                "            \"#attr3\": \"text\"" +
                "        }," +
                "        \"email\": {" +
                "            \"to\": \"to_example@gmail.com\"," +
                "            \"from\": \"from_example@gmail.com\"," +
                "            \"subject\": \"Project discussion\"," +
                "            \"body\": {" +
                "                \"@font\": \"Verdana\"," +
                "                \"#body\": \"Body message\"" +
                "            }," +
                "            \"date\": {" +
                "                \"@day\": \"12\"," +
                "                \"@month\": \"12\"," +
                "                \"@year\": \"2018\"," +
                "                \"#date\": null" +
                "            }" +
                "        }" +
                "    }" +
                "}";

        String actual = sut.analyzeAndConvert(path);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

}
