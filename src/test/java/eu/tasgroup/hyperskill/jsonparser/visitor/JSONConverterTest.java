package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.traverse.Traverser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

}