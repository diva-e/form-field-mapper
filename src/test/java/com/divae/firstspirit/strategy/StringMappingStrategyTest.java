package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import org.junit.Test;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StringMappingStrategyTest {

	private final StringMappingStrategy stringMappingStrategy = new StringMappingStrategy();

	@Test
	public void matches() throws Exception {
		assertThat(stringMappingStrategy.matches(String.class, String.class), is(true));
	}

	@Test
	public void mapFieldOFormField() throws Exception {
		TestClass test = new TestClass();
		test.string = "string";

		de.espirit.firstspirit.forms.FormField<String> formField = build(FormFieldMock.<String>formFieldWith().aType(String.class));
		stringMappingStrategy.map(getInstance(test.getClass().getField("string")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get(), is("string"));
	}

	@Test
	public void mapMethodOFormField() throws Exception {
		TestClass test = new TestClass();
		test.setPrivateString("string");

		de.espirit.firstspirit.forms.FormField<String> formField = build(FormFieldMock.<String>formFieldWith().aType(String.class));
		stringMappingStrategy.map(getInstance(test.getClass().getMethod("getPrivateString")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get(), is("string"));
	}

	@Test
	public void mapFormFieldFieldO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<String> formField = build(FormFieldMock.<String>formFieldWith().aValue("string"));
		stringMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getField("string")), test);

		assertThat(test.string, is("string"));
	}

	@Test
	public void mapFormFieldMethodO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<String> formField = build(FormFieldMock.<String>formFieldWith().aValue("string"));
		stringMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getMethod("setPrivateString", String.class)), test);

		assertThat(test.getPrivateString(), is("string"));
	}

	public static final class TestClass {
		@FormField("tt_string")
		public String string;

		private String privateString;

		@FormField("tt_string")
		public String getPrivateString() {
			return privateString;
		}

		@FormField("tt_string")
		public void setPrivateString(String privateString) {
			this.privateString = privateString;
		}
	}

}