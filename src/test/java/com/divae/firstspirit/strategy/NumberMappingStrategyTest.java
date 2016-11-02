package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import org.junit.Test;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.Creator.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NumberMappingStrategyTest {

	private final NumberMappingStrategy numberMappingStrategy = new NumberMappingStrategy();

	@Test
	public void matches() throws Exception {
		assertThat(numberMappingStrategy.matches(Number.class, Number.class), is(true));
		assertThat(numberMappingStrategy.matches(Number.class, Long.class), is(true));
		assertThat(numberMappingStrategy.matches(Number.class, Double.class), is(true));
		assertThat(numberMappingStrategy.matches(Double.class, Number.class), is(true));
		assertThat(numberMappingStrategy.matches(Long.class, Number.class), is(true));
		assertThat(numberMappingStrategy.matches(Long.class, Long.class), is(true));
		assertThat(numberMappingStrategy.matches(Double.class, Double.class), is(true));
	}

	@Test
	public void mapFieldOFormField() throws Exception {
		TestClass test = new TestClass();
		test.number = 123.0;

		de.espirit.firstspirit.forms.FormField<Double> formField = build(FormFieldMock.<Double>formFieldWith().aType(Double.class));
		numberMappingStrategy.map(getInstance(test.getClass().getField("number")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get(), is(123.0));
	}

	@Test
	public void mapMethodOFormField() throws Exception {
		TestClass test = new TestClass();
		test.setPrivateNumber(123.0);

		de.espirit.firstspirit.forms.FormField<Double> formField = build(FormFieldMock.<Double>formFieldWith().aType(Double.class));
		numberMappingStrategy.map(getInstance(test.getClass().getMethod("getPrivateNumber")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get(), is(123.0));
	}

	@Test
	public void mapFormFieldFieldO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<Double> formField = build(FormFieldMock.<Double>formFieldWith().aValue(123.0));
		numberMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getField("number")), test);

		assertThat(test.number, is(123.0));
	}

	@Test
	public void mapFormFieldMethodO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<Double> formField = build(FormFieldMock.<Double>formFieldWith().aValue(123.0));
		numberMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getMethod("setPrivateNumber", Double.class)), test);

		assertThat(test.getPrivateNumber(), is(123.0));
	}

	public static final class TestClass {
		@FormField("tt_number")
		public Double number;

		private Double privateNumber;

		@FormField("tt_number")
		public Double getPrivateNumber() {
			return privateNumber;
		}

		@FormField("tt_number")
		public void setPrivateNumber(Double privateNumber) {
			this.privateNumber = privateNumber;
		}
	}

}