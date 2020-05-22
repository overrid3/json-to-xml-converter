package eu.tasgroup.hyperskill.jsonparser.utils;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.print.Doc;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JSONUtilsTest {

	@Test
	@DisplayName("convert string value")
	public void convertValueString(){
		// method under test
		Object actual = JSONUtils.convertValue("\"string\"");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(String.class).isEqualTo("string");
	}
	@Test
	@DisplayName("convert empty string value")
	public void convertValueStringEmpty(){
		// method under test
		Object actual = JSONUtils.convertValue("\"\"");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(String.class).isEqualTo("");
	}

	@Test
	@DisplayName("convert int value")
	public void convertValueInt(){
		// method under test
		Object actual = JSONUtils.convertValue("2");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(Integer.class).isEqualTo(2);
	}

	@Test
	@DisplayName("convert int more digit value")
	public void convertValueInt2(){
		// method under test
		Object actual = JSONUtils.convertValue("2023423");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(Integer.class).isEqualTo(2023423);
	}


	@Test
	@DisplayName("convert double value")
	public void convertValueDouble(){
		// method under test
		Object actual = JSONUtils.convertValue("2.5");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(Double.class).isEqualTo(2.5);
	}

	@Test
	@DisplayName("convert double more digit value")
	public void convertValueDouble2(){
		// method under test
		Object actual = JSONUtils.convertValue("2023423.567");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(Double.class).isEqualTo(2023423.567);
	}

	@Test
	@DisplayName("convert boolean true value")
	public void convertBooleanTrue(){
		// method under test
		Object actual = JSONUtils.convertValue("true");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(Boolean.class).isEqualTo(true);
	}

	@Test
	@DisplayName("convert boolean true value")
	public void convertBooleanFalse(){
		// method under test
		Object actual = JSONUtils.convertValue("false");
		// verify
		assertThat(actual).isNotNull().isInstanceOf(Boolean.class).isEqualTo(false);
	}

	@Test
	@DisplayName("convert \"null\" value")
	public void convertValueStringNull(){
		// method under test
		Object actual = JSONUtils.convertValue("null");
		// verify
		assertThat(actual).isNull();
	}

	@Test
	@DisplayName("convert null value")
	public void convertValueNull(){
		// verify
		assertThatThrownBy(() -> JSONUtils.convertValue(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage(JSONUtils.JSON_STRING_CANNOT_BE_NULL);

	}

	@Test
	@DisplayName("convert invalid value")
	public void convertWeirdString(){
		// verify
		assertThatThrownBy(() -> JSONUtils.convertValue("nullz"))
				.isInstanceOf(InvalidParameterException.class)
				.hasMessage(String.format(JSONUtils.INVALID_JSON_VALUE_MESSAGE, "nullz"));
	}

	@Test
	public void getNormalizedKey_At_Ok(){
		String actual = JSONUtils.getNormalizedKey("@asd");
		assertThat(actual).isEqualTo("asd");
	}
	@Test
	public void getNormalizedKey_Hash_Ok(){
		String actual = JSONUtils.getNormalizedKey("#asd");
		assertThat(actual).isEqualTo("asd");
	}
	@Test
	public void getNormalizedKey_NoSpecial_Ok(){
		String actual = JSONUtils.getNormalizedKey("asd");
		assertThat(actual).isEqualTo("asd");
	}
	@Test
	public void getNormalizedKey_InnerAt_Ok(){
		String actual = JSONUtils.getNormalizedKey("asd@qwe");
		assertThat(actual).isEqualTo("asd@qwe");
	}
	@Test
	public void getNormalizedKey_InnerHash_Ok(){
		String actual = JSONUtils.getNormalizedKey("asd#qwe");
		assertThat(actual).isEqualTo("asd#qwe");
	}
	@Test
	public void getNormalizedKey_Null_Ok(){
		String actual = JSONUtils.getNormalizedKey(null);
		assertThat(actual).isEqualTo(null);
	}

}