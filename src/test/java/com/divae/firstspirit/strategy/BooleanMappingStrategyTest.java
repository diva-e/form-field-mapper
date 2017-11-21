package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import org.junit.Test;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.agency.SpecialistsBrokerMock.specialistsBrokerWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class BooleanMappingStrategyTest {

	private final BooleanMappingStrategy booleanMappingStrategy = new BooleanMappingStrategy();

	@Test
	public void matches() throws Exception {
		assertThat(booleanMappingStrategy.matches(Boolean.class, Boolean.class), is(true));
	}

	@Test
	public void mapFieldOFormField() throws Exception {
		TestClass test = new TestClass();
		test.bool = true;

		de.espirit.firstspirit.forms.FormField<Boolean> formField = build(FormFieldMock.<Boolean>formFieldWith().aType(Boolean.class));
        booleanMappingStrategy.map(getInstance(test.getClass().getField("bool")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")), build(specialistsBrokerWith()));

		assertThat(formField.get(), is(true));
	}

	@Test
	public void mapMethodOFormField() throws Exception {
		TestClass test = new TestClass();
		test.setPrivateBoolean(true);

		de.espirit.firstspirit.forms.FormField<Boolean> formField = build(FormFieldMock.<Boolean>formFieldWith().aType(Boolean.class));
        booleanMappingStrategy.map(getInstance(test.getClass().getMethod("getPrivateBoolean")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")), build(specialistsBrokerWith()));

		assertThat(formField.get(), is(true));
	}

	@Test
	public void mapFormFieldFieldO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<Boolean> formField = build(FormFieldMock.<Boolean>formFieldWith().aValue(true));
        booleanMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), build(specialistsBrokerWith()), getInstance(test.getClass().getField("bool")), test);

		assertThat(test.bool, is(true));
	}

	@Test
	public void mapFormFieldMethodO() throws Exception {
		TestClass test = new TestClass();

		de.espirit.firstspirit.forms.FormField<Boolean> formField = build(FormFieldMock.<Boolean>formFieldWith().aValue(true));
        booleanMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), build(specialistsBrokerWith()), getInstance(test.getClass().getMethod("setPrivateBoolean", Boolean.class)), test);

		assertThat(test.getPrivateBoolean(), is(true));
	}

	public static final class TestClass {
		@FormField("tt_boolean")
		public Boolean bool;

		private Boolean privateBoolean;

		@FormField("tt_boolean")
		public Boolean getPrivateBoolean() {
			return privateBoolean;
		}

		@FormField("tt_boolean")
		public void setPrivateBoolean(Boolean privateBoolean) {
			this.privateBoolean = privateBoolean;
		}
	}

}