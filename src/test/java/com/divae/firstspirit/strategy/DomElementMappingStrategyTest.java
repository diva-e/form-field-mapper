package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.editor.value.DomElement;
import org.junit.Test;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.Creator.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.editor.value.DomElementMock.domElementWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DomElementMappingStrategyTest {

	private final DomElementMappingStrategy domElementMappingStrategy = new DomElementMappingStrategy();

	@Test
	public void matches() throws Exception {
		assertThat(domElementMappingStrategy.matches(String.class, DomElement.class), is(true));
		assertThat(domElementMappingStrategy.matches(DomElement.class, String.class), is(true));
	}

	@Test
	public void mapFieldOFormField() throws Exception {
		TestClass test = new TestClass();
		test.string = "string";

		de.espirit.firstspirit.forms.FormField<DomElement> formField = build(FormFieldMock.<DomElement>formFieldWith().aValue(build(domElementWith())));
		domElementMappingStrategy.map(getInstance(test.getClass().getField("string")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get().toText(true), is("string"));
	}

	@Test
	public void mapMethodOFormField() throws Exception {
		TestClass test = new TestClass();
		test.setPrivateString("string");

		de.espirit.firstspirit.forms.FormField<DomElement> formField = build(FormFieldMock.<DomElement>formFieldWith().aValue(build(domElementWith())));
		domElementMappingStrategy.map(getInstance(test.getClass().getMethod("getPrivateString")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get().toText(true), is("string"));
	}

	@Test
	public void mapFormFieldFieldO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<DomElement> formField = build(FormFieldMock.<DomElement>formFieldWith().aValue(build(domElementWith().toText(true, "string"))));
		domElementMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getField("string")), test);

		assertThat(test.string, is("string"));
	}

	@Test
	public void mapFormFieldMethodO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<DomElement> formField = build(FormFieldMock.<DomElement>formFieldWith().aValue(build(domElementWith().toText(true, "string"))));
		domElementMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getMethod("setPrivateString", String.class)), test);

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

		@FormField("tt_number")
		public void setPrivateString(String privateString) {
			this.privateString = privateString;
		}
	}

}