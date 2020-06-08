package eu.tasgroup.hyperskill.jsonparser.converter;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class XMLToJSONConverterTest {

    private XMLToJSONConverter sut = new XMLToJSONConverter();

    @DisplayName("Simple test on conversion from XML to JSON")
    @Test
    public void test1() {

        /*<root>
            <id>6753322</id>
          <root>*/

        XMLElement root = new XMLElement("root", "");
        XMLElement id = new XMLElement("id", "4564322");

        root.addChild(id);

        JSONElement je = sut.convert(root);

        assertThat(je.getKey()).isEqualTo("\"root\"");
        assertThat(je.getValue().toString()).isEmpty();
        assertThat(je.getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"id\"", "\"4564322\"", je));
    }

    @DisplayName("Testing conversion of an XML element with attributes to JSON")
    @Test
    public void test2() {

       /* <root>
            <id>6753322</id>
            <number region="Russia">8-900-000-00-00</number>
        <root>*/

        XMLElement root = new XMLElement("root", "");
        XMLElement id = new XMLElement("id", "4564322");
        XMLElement number = new XMLElement("number", "8-900-000-00-00");
        number.insertAttributeEntry("region", "\"Russia\"");

        root.addChild(id);
        root.addChild(number);

        JSONElement je = sut.convert(root);

        assertThat(je.getKey()).isEqualTo("\"root\"");
        assertThat(je.getValue().toString()).isEmpty();
        assertThat(je.getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"id\"", "\"4564322\"", je),
                        tuple("\"number\"", "", je));
        assertThat(je.getChildren().get(1).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@region\"", "\"Russia\"", je.getChildren().get(1)),
                        tuple("\"#number\"", "\"8-900-000-00-00\"", je.getChildren().get(1)));
    }

    @DisplayName("Testing conversion of an XML element with attributes and null tag to JSON")
    @Test
    public void test3() {

       /* <root>
            <id>6753322</id>
            <number region="Russia">8-900-000-00-00</number>
            <empty1 />
        <root>*/

        XMLElement root = new XMLElement("root", "");
        XMLElement id = new XMLElement("id", "4564322");
        XMLElement number = new XMLElement("number", "8-900-000-00-00");
        number.insertAttributeEntry("region", "\"Russia\"");
        XMLElement empty1 = new XMLElement("empty1", null);

        root.addChild(id);
        root.addChild(number);
        root.addChild(empty1);

        JSONElement je = sut.convert(root);

        assertThat(je.getKey()).isEqualTo("\"root\"");
        assertThat(je.getValue().toString()).isEmpty();
        assertThat(je.getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"id\"", "\"4564322\"", je),
                        tuple("\"number\"", "", je),
                        tuple("\"empty1\"", null, je));
        assertThat(je.getChildren().get(1).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@region\"", "\"Russia\"", je.getChildren().get(1)),
                        tuple("\"#number\"", "\"8-900-000-00-00\"", je.getChildren().get(1)));
    }

    @DisplayName("Testing a more complex XML element")
    @Test
    public void test4() {

        /* <root>
            <id>6753322</id>
            <number region="Russia">8-900-000-00-00</number>
            <empty1 />
            <nonattr2></nonattr2>
            <nonattr3>text</nonattr3>
         */

        XMLElement root = new XMLElement("root", "");
        XMLElement id = new XMLElement("id", "4564322");
        XMLElement number = new XMLElement("number", "8-900-000-00-00");
        number.insertAttributeEntry("region", "\"Russia\"");
        XMLElement empty1 = new XMLElement("empty1", null);
        XMLElement empty2 = new XMLElement("empty2", "");
        XMLElement empty3 = new XMLElement("empty3", "text");

        root.addChild(id);
        root.addChild(number);
        root.addChild(empty1);
        root.addChild(empty2);
        root.addChild(empty3);

        JSONElement je = sut.convert(root);

        assertThat(je.getKey()).isEqualTo("\"root\"");
        assertThat(je.getValue().toString()).isEmpty();
        assertThat(je.getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"id\"", "\"4564322\"", je),
                        tuple("\"number\"", "", je),
                        tuple("\"empty1\"", null, je),
                        tuple("\"empty2\"", "", je),
                        tuple("\"empty3\"", "\"text\"", je));
        assertThat(je.getChildren().get(1).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@region\"", "\"Russia\"", je.getChildren().get(1)),
                        tuple("\"#number\"", "\"8-900-000-00-00\"", je.getChildren().get(1)));
    }

    @DisplayName("Conversion test of an XML element with nested children to JSON")
    @Test
    public void test5() {

        /* <root>
            <id>6753322</id>
            <number region="Russia">8-900-000-00-00</number>
            <empty1 />
            <nonattr2></nonattr2>
            <nonattr3>text</nonattr3>
            <attr1 id="1" />
            <attr2 id="2"></attr2>
            <attr3 id="3">text</attr3>
         */

        XMLElement root = new XMLElement("root", "");
        XMLElement id = new XMLElement("id", "4564322");
        XMLElement number = new XMLElement("number", "8-900-000-00-00");
        number.insertAttributeEntry("region", "\"Russia\"");
        XMLElement empty1 = new XMLElement("empty1", null);
        XMLElement empty2 = new XMLElement("empty2", "");
        XMLElement empty3 = new XMLElement("empty3", "text");
        XMLElement attr1 = new XMLElement("attr1", null);
        attr1.insertAttributeEntry("id", "\"1\"");
        XMLElement attr2 = new XMLElement("attr2", "");
        attr2.insertAttributeEntry("id", "\"2\"");
        XMLElement attr3 = new XMLElement("attr3", "text");
        attr3.insertAttributeEntry("id", "\"3\"");

        root.addChild(id);
        root.addChild(number);
        root.addChild(empty1);
        root.addChild(empty2);
        root.addChild(empty3);
        root.addChild(attr1);
        root.addChild(attr2);
        root.addChild(attr3);

        JSONElement je = sut.convert(root);

        assertThat(je.getKey()).isEqualTo("\"root\"");
        assertThat(je.getValue().toString()).isEmpty();
        assertThat(je.getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"id\"", "\"4564322\"", je),
                        tuple("\"number\"", "", je),
                        tuple("\"empty1\"", null, je),
                        tuple("\"empty2\"", "", je),
                        tuple("\"empty3\"", "\"text\"", je),
                        tuple("\"attr1\"", "", je),
                        tuple("\"attr2\"", "", je),
                        tuple("\"attr3\"", "", je));
        assertThat(je.getChildren().get(1).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@region\"", "\"Russia\"", je.getChildren().get(1)),
                        tuple("\"#number\"", "\"8-900-000-00-00\"", je.getChildren().get(1)));
        assertThat(je.getChildren().get(5).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@id\"", "\"1\"", je.getChildren().get(5)),
                        tuple("\"#attr1\"", null, je.getChildren().get(5)));
    }

    @DisplayName("Testing conversion of a complex XML object to JSON")
    @Test
    public void test6() {
        /*
         <root>
            <id>6753322</id>
            <number region="Russia">8-900-000-00-00</number>
            <nonattr1 />
            <nonattr2></nonattr2>
            <nonattr3>text</nonattr3>
            <attr1 id="1" />
            <attr2 id="2"></attr2>
            <attr3 id="3">text</attr3>
            <email>
                <to>to_example@gmail.com</to>
                <from>from_example@gmail.com</from>
                <subject>Project discussion</subject>
                <body font="Verdana">Body message</body>
                <date day="12" month="12" year="2018"/>
            </email>
         </root>
     */

        XMLElement root = new XMLElement("root", "");
        XMLElement id = new XMLElement("id", "4564322");
        XMLElement number = new XMLElement("number", "8-900-000-00-00");
        number.insertAttributeEntry("region", "\"Russia\"");
        XMLElement empty1 = new XMLElement("empty1", null);
        XMLElement empty2 = new XMLElement("empty2", "");
        XMLElement empty3 = new XMLElement("empty3", "text");
        XMLElement attr1 = new XMLElement("attr1", null);
        attr1.insertAttributeEntry("id", "\"1\"");
        XMLElement attr2 = new XMLElement("attr2", "");
        attr2.insertAttributeEntry("id", "\"2\"");
        XMLElement attr3 = new XMLElement("attr3", "text");
        attr3.insertAttributeEntry("id", "\"3\"");
        XMLElement mail=new XMLElement("email","");
        XMLElement to=new XMLElement("to","to_example@gmail.com");
        XMLElement from=new XMLElement("from","from_example@gmail.com");
        XMLElement subject=new XMLElement("subject","project discussion");
        XMLElement body=new XMLElement("body","body message");
        body.insertAttributeEntry("font","\"Verdana\"");
        XMLElement date=new XMLElement("date",null);
        date.insertAttributeEntry("day","\"12\"");
        date.insertAttributeEntry("month","\"12\"");
        date.insertAttributeEntry("year","\"2018\"");


        root.addChild(id);
        root.addChild(number);
        root.addChild(empty1);
        root.addChild(empty2);
        root.addChild(empty3);
        root.addChild(attr1);
        root.addChild(attr2);
        root.addChild(attr3);
        root.addChild(mail);
        mail.addChild(to);
        mail.addChild(from);
        mail.addChild(subject);
        mail.addChild(body);
        mail.addChild(date);

        JSONElement je = sut.convert(root);

        assertThat(je.getKey()).isEqualTo("\"root\"");
        assertThat(je.getValue().toString()).isEmpty();
        assertThat(je.getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"id\"", "\"4564322\"", je),
                        tuple("\"number\"", "", je),
                        tuple("\"empty1\"", null, je),
                        tuple("\"empty2\"", "", je),
                        tuple("\"empty3\"", "\"text\"", je),
                        tuple("\"attr1\"", "", je),
                        tuple("\"attr2\"", "", je),
                        tuple("\"attr3\"", "", je),
                        tuple("\"email\"","",je));
        assertThat(je.getChildren().get(1).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@region\"", "\"Russia\"", je.getChildren().get(1)),
                        tuple("\"#number\"", "\"8-900-000-00-00\"", je.getChildren().get(1)));
        assertThat(je.getChildren().get(5).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@id\"", "\"1\"", je.getChildren().get(5)),
                        tuple("\"#attr1\"", null, je.getChildren().get(5)));
        assertThat(je.getChildren().get(6).getChildren())
                .extracting("key", "value", "parent")
                    .containsExactly(tuple("\"@id\"", "\"2\"", je.getChildren().get(6)),
                        tuple("\"#attr2\"","\"\"",je.getChildren().get(6)));
        assertThat(je.getChildren().get(7).getChildren())
                .extracting("key", "value", "parent")
                    .containsExactly(tuple("\"@id\"", "\"3\"", je.getChildren().get(7)),
                            tuple("\"#attr3\"","\"text\"",je.getChildren().get(7)));

        assertThat(je.getChildren().get(8).getChildren())
                .extracting("key", "value", "parent")
                    .containsExactly(tuple("\"to\"", "\"to_example@gmail.com\"", je.getChildren().get(8)),
                            tuple("\"from\"", "\"from_example@gmail.com\"", je.getChildren().get(8)),
                            tuple("\"subject\"", "\"project discussion\"", je.getChildren().get(8)),
                            tuple("\"body\"", "", je.getChildren().get(8)),
                            tuple("\"date\"", "",je.getChildren().get(8)));

        assertThat(je.getChildren().get(8).getChildren().get(3).getChildren())
                .extracting("key", "value", "parent")
                .containsExactly(tuple("\"@font\"","\"Verdana\"",je.getChildren().get(8).getChildren().get(3)),
                        tuple("\"#body\"", "\"body message\"",je.getChildren().get(8).getChildren().get(3)));

        assertThat(je.getChildren().get(8).getChildren().get(4).getChildren())
                .extracting("key", "value", "parent")
                    .containsExactly(tuple("\"@day\"","\"12\"",je.getChildren().get(8).getChildren().get(4)),
                            tuple("\"@month\"","\"12\"",je.getChildren().get(8).getChildren().get(4)),
                            tuple("\"@year\"","\"2018\"",je.getChildren().get(8).getChildren().get(4)),
                            tuple("\"#date\"",null,je.getChildren().get(8).getChildren().get(4)));
    }
}