package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.converter.JSONConverter2;
import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class JSONConverter2Test {

    private JSONConverter2 sut;

    @BeforeEach
    public void setUp() {
        sut = new JSONConverter2();
    }

    @DisplayName("Testing conversion of a correctly formatted XML element")
    @Test
    public void convertTest1() {

        //"key":{"#key":"ok"}

        JSONElement e1 = new JSONElement("key", null);
        JSONElement e2 = new JSONElement("#key", "ok");

        e1.addChild(e2);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("key");
        assertThat(e.getText()).isEqualTo("ok");
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren()).isEmpty();
    }

    @DisplayName("Testing conversion of a correctly formatted XML Element with attribute in addition")
    @Test
    public void convertTest2() {

        //"key":{"#key":"ok", "@kao":"hur"}

        JSONElement e1 = new JSONElement("key", null);
        JSONElement e2 = new JSONElement("#key", "ok");
        JSONElement e3 = new JSONElement("@kao", "hur");

        e1.addChild(e2);
        e1.addChild(e3);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("key");
        assertThat(e.getText()).isEqualTo("ok");
        assertThat(e.getAttributes()).containsEntry("kao", "hur");
        assertThat(e.getChildren()).isEmpty();
    }

    @DisplayName("Conversion test of a JSON element with complex hash key child")
    @Test
    public void convertTest3() {

        //"key":{"#key":{"@e1":orgodo,"#e2":"mae"}, "@kao":"hur"}

        JSONElement e1 = new JSONElement("key", null);
        JSONElement e2 = new JSONElement("#key", null);
        JSONElement e3 = new JSONElement("@kao", "hur");
        JSONElement e4 = new JSONElement("@e1", "orgoda");
        JSONElement e5 = new JSONElement("@e2", "mae");

        e1.addChild(e2);
        e1.addChild(e3);
        e2.addChild(e4);
        e2.addChild(e5);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("key");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("key", null, e),
                        tuple("kao", "hur", e));
        assertThat(e.getChildren().get(0).getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("e1", "orgoda", e.getChildren().get(0)),
                        tuple("e2", "mae", e.getChildren().get(0)));
    }

    @DisplayName("Testing conversion of nested JSON objects")
    @Test
    public void convertTest4() {
        //inner1:{inner2:{inner3:{key1:bla,key2:jack}}}

        JSONElement e1 = new JSONElement("inner1", null);
        JSONElement e2 = new JSONElement("inner2", null);
        JSONElement e3 = new JSONElement("inner3", null);
        JSONElement e4 = new JSONElement("key1", "bla");
        JSONElement e5 = new JSONElement("key2", "jack");

        e1.addChild(e2);
        e2.addChild(e3);
        e3.addChild(e4);
        e3.addChild(e5);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("inner1");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("inner2", null, e));
        assertThat(e.getChildren().get(0).getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("inner3", null, e.getChildren().get(0)));
        assertThat(e.getChildren().get(0).getChildren().get(0).getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("key1", "bla", e.getChildren().get(0).getChildren().get(0)),
                        tuple("key2", "jack", e.getChildren().get(0).getChildren().get(0)));
    }

    @DisplayName("Conversion test of a not correctly formatted JSON element with children")
    @Test
    public void convertTest5() {

        //inner4:{@:123,#inner4:"cuzzupe"}

        JSONElement e1 = new JSONElement("inner4", null);
        JSONElement e2 = new JSONElement("@", 123);
        JSONElement e3 = new JSONElement("#inner4", "cuzzupe");

        e1.addChild(e2);
        e1.addChild(e3);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("inner4");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("inner4", "cuzzupe", e));
    }

    @DisplayName("Conversion test of a correctly formatted JSON object, but with not correct keys")
    @Test
    public void convertTest6() {

        JSONElement e1 = new JSONElement("inner5", null);
        JSONElement e2 = new JSONElement("@her", 123);
        JSONElement e3 = new JSONElement("#inner4", "cuzzupe");

        e1.addChild(e2);
        e1.addChild(e3);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("inner5");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("her", "123", e),
                        tuple("inner4", "cuzzupe", e));

    }

    @DisplayName("Conversion test with not correctly formatted JSON object (only one @key defined,no one other)")
    @Test
    public void convertTest7() {

        /*"inner8": {
            "@attr3": "value7"
        }*/

        JSONElement e1 = new JSONElement("inner8", null);
        JSONElement e2 = new JSONElement("@attr3", "value7");

        e1.addChild(e2);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("inner8");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("attr3", "value7", e));
    }

    @DisplayName("Conversion test of a not correctly formatted JSON object (one @key, one #key, one normal key)")
    @Test
    public void convertTest8() {

        /*"inner9": {
            "@attr4": "value8",
            "#inner9": "value9",
            "something": "value10"
        }*/

        JSONElement e1 = new JSONElement("inner9", null);
        JSONElement e2 = new JSONElement("@attr4", "value8");
        JSONElement e3 = new JSONElement("#inner9", "value9");
        JSONElement e4 = new JSONElement("something", "value10");

        e1.addChild(e2);
        e1.addChild(e3);
        e1.addChild(e4);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("inner9");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("attr4", "value8", e),
                        tuple("inner9", "value9", e),
                        tuple("something", "value10", e));
    }

    @DisplayName("Conversion test of a corretly formatted JSON object but with null values for its child")
    @Test
    public void convertTest9() {

        /*"inner10": {
            "@attr5": null,
                    "#inner10": null
        }*/

        JSONElement e1 = new JSONElement("inner10", null);
        JSONElement e2 = new JSONElement("@attr5", null);
        JSONElement e3 = new JSONElement("#inner10", null);

        e1.addChild(e2);
        e1.addChild(e3);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("inner10");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).containsEntry("attr5", "");
    }

    @DisplayName("Conversion test of a correctly formatted JSON Element, but with empty normalized strings")
    @Test
    public void convertTest10() {

        //inner10{inner11:{"@":"value1","#":"value2"},inner12:{"#inner12":"value3"}}

        JSONElement e1 = new JSONElement("inner10", null);
        JSONElement e2 = new JSONElement("inner11", null);
        JSONElement e3 = new JSONElement("#", null);
        JSONElement e4 = new JSONElement("@", null);
        JSONElement e5 = new JSONElement("inner12", null);
        JSONElement e6 = new JSONElement("#inner12", "value3");

        e1.addChild(e2);
        e1.addChild(e5);

        e2.addChild(e3);
        e2.addChild(e4);

        e5.addChild(e6);

        XMLElement xmlE = sut.convert(e1);

        assertThat(xmlE.getTagName()).isEqualTo("inner10");
        assertThat(xmlE.getText()).isNull();
        assertThat(xmlE.getAttributes()).isEmpty();
        assertThat(xmlE.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("inner11", "", xmlE),
                        tuple("inner12", "value3", xmlE)); //AL MOMENTO E' NULL MA DOBBIAMO MODIFICARE LA CONDIZIONE
        assertThat(xmlE.getChildren().get(0).getChildren()).isEmpty();
        assertThat(xmlE.getAttributes()).isEmpty();
    }

    @DisplayName("conversion test of a JSON element with duplicate keys")
    @Test
    public void convertTest11() {

        /*"inner12": {
            "@somekey": "attrvalue",
                    "#inner12": null,
                    "somekey": "keyvalue",
                    "inner12": "notnull"
        }*/

        JSONElement e1 = new JSONElement("inner12", null);
        JSONElement e2 = new JSONElement("@somekey", "attrvalue");
        JSONElement e3 = new JSONElement("#inner12", null);
        JSONElement e4 = new JSONElement("somekey", "keyvalue");
        JSONElement e5 = new JSONElement("inner12", "notnull");

        e1.addChild(e2);
        e1.addChild(e3);
        e1.addChild(e4);
        e1.addChild(e5);

        XMLElement xmlE = sut.convert(e1);

        assertThat(xmlE.getTagName()).isEqualTo("inner12");
        assertThat(xmlE.getText()).isNull();
        assertThat(xmlE.getAttributes()).isEmpty();
        assertThat(xmlE.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("somekey", "keyvalue", xmlE),
                        tuple("inner12", "notnull", xmlE));
    }

    @DisplayName("Conversion test of a JSON element with complex #key and @key elements")
    @Test
    public void convertTest12() {

        /*"inner13": {
            "@invalid_attr": {
                "some_key": "some value"
            },
            "#inner13": {
                "key": "value"
            }
        }*/

        JSONElement e1 = new JSONElement("inner13", null);
        JSONElement e2 = new JSONElement("@invalid_attr", null);
        JSONElement e3 = new JSONElement("some_key", "some value");
        JSONElement e4 = new JSONElement("#inner13", null);
        JSONElement e5 = new JSONElement("key", "value");

        e1.addChild(e2);
        e1.addChild(e4);

        e2.addChild(e3);

        e4.addChild(e5);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("inner13");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("invalid_attr", null, e),
                        tuple("inner13", null, e));
        assertThat(e.getChildren().get(0).getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("some_key", "some value", e.getChildren().get(0)));
        assertThat(e.getChildren().get(1).getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("key", "value", e.getChildren().get(1)));
    }

    @DisplayName("Conversion test of a JSON object with empty key")
    @Test
    public void converTest13() {

        /*"": {
            "#": null,
             "secret": "this won't be converted"
        }*/

    }

    @DisplayName("Conversion test of a large JSON object")
    @Test
    public void convertTest14() {

        //may:{"@march":"april","#may":{"ciao":"ciao","ciao1":"ciao1"},"maestro":"sniff"}

        JSONElement e1 = new JSONElement("may", null);
        JSONElement e2 = new JSONElement("@march", "april");
        JSONElement e3 = new JSONElement("#may", null);
        JSONElement e4 = new JSONElement("ciao", "ciao");
        JSONElement e5 = new JSONElement("ciao1", "ciao1");
        JSONElement e6 = new JSONElement("maestro", "sniff");

        e1.addChild(e2);
        e1.addChild(e3);
        e1.addChild(e6);
        e3.addChild(e4);
        e3.addChild(e5);

        XMLElement e = sut.convert(e1);

        assertThat(e.getTagName()).isEqualTo("may");
        assertThat(e.getText()).isNull();
        assertThat(e.getAttributes()).isEmpty();
        assertThat(e.getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("march", "april", e),
                        tuple("may", null, e),
                        tuple("maestro", "sniff", e));
        assertThat(e.getChildren().get(1).getChildren())
                .extracting("tagName", "text", "parent")
                .containsExactly(tuple("ciao", "ciao", e.getChildren().get(1)),
                        tuple("ciao1", "ciao1", e.getChildren().get(1)));
    }

}