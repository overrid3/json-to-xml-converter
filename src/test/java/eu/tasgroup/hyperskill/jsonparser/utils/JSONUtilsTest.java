package eu.tasgroup.hyperskill.jsonparser.utils;


import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JSONUtilsTest {

    @Test
    @DisplayName("convert string value")
    public void convertValueString() {
        // method under test
        Object actual = JSONUtils.convertValue("\"string\"");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(String.class).isEqualTo("string");
    }

    @Test
    @DisplayName("convert empty string value")
    public void convertValueStringEmpty() {
        // method under test
        Object actual = JSONUtils.convertValue("\"\"");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(String.class).isEqualTo("");
    }

    @Test
    @DisplayName("convert int value")
    public void convertValueInt() {
        // method under test
        Object actual = JSONUtils.convertValue("2");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(Integer.class).isEqualTo(2);
    }

    @Test
    @DisplayName("convert int more digit value")
    public void convertValueInt2() {
        // method under test
        Object actual = JSONUtils.convertValue("2023423");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(Integer.class).isEqualTo(2023423);
    }


    @Test
    @DisplayName("convert double value")
    public void convertValueDouble() {
        // method under test
        Object actual = JSONUtils.convertValue("2.5");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(Double.class).isEqualTo(2.5);
    }

    @Test
    @DisplayName("convert double more digit value")
    public void convertValueDouble2() {
        // method under test
        Object actual = JSONUtils.convertValue("2023423.567");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(Double.class).isEqualTo(2023423.567);
    }

    @Test
    @DisplayName("convert boolean true value")
    public void convertBooleanTrue() {
        // method under test
        Object actual = JSONUtils.convertValue("true");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(Boolean.class).isEqualTo(true);
    }

    @Test
    @DisplayName("convert boolean true value")
    public void convertBooleanFalse() {
        // method under test
        Object actual = JSONUtils.convertValue("false");
        // verify
        assertThat(actual).isNotNull().isInstanceOf(Boolean.class).isEqualTo(false);
    }

    @Test
    @DisplayName("convert \"null\" value")
    public void convertValueStringNull() {
        // method under test
        Object actual = JSONUtils.convertValue("null");
        // verify
        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("convert null value")
    public void convertValueNull() {
        // verify
        assertThatThrownBy(() -> JSONUtils.convertValue(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(JSONUtils.JSON_STRING_CANNOT_BE_NULL);

    }

    @Test
    @DisplayName("convert invalid value")
    public void convertWeirdString() {
        // verify
        assertThatThrownBy(() -> JSONUtils.convertValue("nullz"))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage(String.format(JSONUtils.INVALID_JSON_VALUE_MESSAGE, "nullz"));
    }

    @Test
    public void getNormalizedKey_At_Ok() {
        String actual = JSONUtils.getNormalizedKey("@asd");
        assertThat(actual).isEqualTo("asd");
    }

    @Test
    public void getNormalizedKey_Hash_Ok() {
        String actual = JSONUtils.getNormalizedKey("#asd");
        assertThat(actual).isEqualTo("asd");
    }

    @Test
    public void getNormalizedKey_NoSpecial_Ok() {
        String actual = JSONUtils.getNormalizedKey("asd");
        assertThat(actual).isEqualTo("asd");
    }

    @Test
    public void getNormalizedKey_InnerAt_Ok() {
        String actual = JSONUtils.getNormalizedKey("asd@qwe");
        assertThat(actual).isEqualTo("asd@qwe");
    }

    @Test
    public void getNormalizedKey_InnerHash_Ok() {
        String actual = JSONUtils.getNormalizedKey("asd#qwe");
        assertThat(actual).isEqualTo("asd#qwe");
    }

    @Test
    public void getNormalizedKey_Null_Ok() {
        String actual = JSONUtils.getNormalizedKey(null);
        assertThat(actual).isEqualTo(null);
    }

    @Test
    @DisplayName("null valid key")
    public void isValidKey1() {
        assertThatThrownBy(() -> JSONUtils.isValidKey(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(JSONUtils.JSON_STRING_CANNOT_BE_NULL);

    }

    @Test
    @DisplayName("invalid key (at sign)")
    public void isValidKey2() {
        boolean actual = JSONUtils.isValidKey("@");
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("invalid key (hash)")
    public void isValidKey3() {
        boolean actual = JSONUtils.isValidKey("#");
        assertThat(actual).isEqualTo(false);
    }


    @Test
    @DisplayName("invalid key (empty)")
    public void isValidKey4() {
        boolean actual = JSONUtils.isValidKey("");
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("valid key")
    public void isValidKey5() {
        boolean actual = JSONUtils.isValidKey("@ciao:mondo");
        assertThat(actual).isEqualTo(true);
    }

    @DisplayName("Testing key normalizing with a valid key")
    @Test
    public void getNormalizedKeyTest1() {
        String s = JSONUtils.getNormalizedKey("#maeeee");
        assertThat(s).isEqualTo("maeeee");
    }

    @DisplayName("Testing key normalizing with a null key")
    @Test
    public void getNormalizedKeyTest2() {
        String s = JSONUtils.getNormalizedKey(null);
        assertThat(s).isNull();
    }

    @DisplayName("Testing the presence of a single correctly written hash key child")
    @Test
    public void hasExactlyOneChildWithHashKeyTest1() {

        //mae:{@lor:ciao,#mae:nur}

        JSONElement e = new JSONElement("mae", null);
        JSONElement e1 = new JSONElement("@lor", "ciao");
        JSONElement e2 = new JSONElement("#mae", "nur");

        e.addChild(e1);
        e.addChild(e2);

        boolean s = JSONUtils.hasExactlyOneChildWithHashKey(e);

        assertThat(s).isTrue();
    }

    @DisplayName("Testing the presence of more correctly written hash key children")
    @Test
    public void hasExactlyOneChildWithHashKeyTest2() {

        //mae:{@lor:ciao,#mae:nur,#mjs:mei}

        JSONElement e = new JSONElement("mae", null);
        JSONElement e1 = new JSONElement("@lor", "ciao");
        JSONElement e2 = new JSONElement("#mae", "nur");
        JSONElement e3 = new JSONElement("#mjs", "mei");

        e.addChild(e1);
        e.addChild(e2);
        e.addChild(e3);

        boolean s = JSONUtils.hasExactlyOneChildWithHashKey(e);

        assertThat(s).isFalse();
    }

    @DisplayName("Testing the absence of one correctly written hash key child")
    @Test
    public void hasExactlyOneChildWithHashKeyTest3() {

        //mae:{@lor:ciao,#mae:nur,#mjs:mei}

        JSONElement e = new JSONElement("mae", null);
        JSONElement e1 = new JSONElement("@lor", "ciao");

        e.addChild(e1);

        boolean s = JSONUtils.hasExactlyOneChildWithHashKey(e);

        assertThat(s).isFalse();
    }

    @DisplayName("Testing the presence of a normal child")
    @Test
    public void hasChildWithoutSpecialCharacterTest1() {

        JSONElement e = new JSONElement("mae", null);
        JSONElement e1 = new JSONElement("@lor", "ciao");
        JSONElement e2= new JSONElement("#mae", "nue");
        JSONElement e3= new JSONElement("m","m");

        e.addChild(e1);
        e.addChild(e2);
        e.addChild(e3);

        boolean s= JSONUtils.hasChildWithoutSpecialCharacter(e);

        assertThat(s).isTrue();
    }

    @DisplayName("Testing the presence of a normal child")
    @Test
    public void hasChildWithoutSpecialCharacterTest2() {

        JSONElement e = new JSONElement("mae", null);
        JSONElement e1 = new JSONElement("@lor", "ciao");
        JSONElement e2= new JSONElement("#mae", "nue");

        e.addChild(e1);
        e.addChild(e2);

        boolean s= JSONUtils.hasChildWithoutSpecialCharacter(e);

        assertThat(s).isFalse();
    }



}