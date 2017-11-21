package com.divae.firstspirit;

import com.divae.firstspirit.access.LanguageMock.LanguageBuilder;
import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.forms.FormData;
import org.junit.Test;

import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.agency.SpecialistsBrokerMock.specialistsBrokerWith;
import static com.divae.firstspirit.forms.FormDataMock.formDataWith;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class FormFieldMapperTest {

	private final FormFieldMapper formFieldMapper = new FormFieldMapper();

	@SuppressWarnings("unchecked")
	@Test
	public void mapTFormData() throws Exception {
		TestClass fromTest = new TestClass();
		fromTest.string = "fromString";
		fromTest.setSecondString("fromSecondString");
		fromTest.number = 1234L;
		fromTest.setSecondNumber(123456L);

		LanguageBuilder languageBuilder = languageWith("DE");
		FormData formData = build(formDataWith().aForm(() ->
				gomEditorProviderWith("test").values(() ->
						asList(gomFormElementWith("tt_string"), gomFormElementWith("tt_second_string"), gomFormElementWith("tt_number"), gomFormElementWith("tt_second_number"))))
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_string")
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_second_string")
				.aValue(() ->
						FormFieldMock.<Long>formFieldWith().aType(Long.class), languageBuilder, "tt_number")
				.aValue(() ->
						FormFieldMock.<Long>formFieldWith().aType(Long.class), languageBuilder, "tt_second_number")
		);
		Language language = build(languageBuilder);
        SpecialistsBroker specialistsBroker = build(specialistsBrokerWith());

        formFieldMapper.map(fromTest, formData, language, specialistsBroker);
        assertThat(((de.espirit.firstspirit.forms.FormField<String>) formData.get(language, "tt_string")).get(), is("fromString"));
		assertThat(((de.espirit.firstspirit.forms.FormField<String>) formData.get(language, "tt_second_string")).get(), is("fromSecondString"));
		assertThat(((de.espirit.firstspirit.forms.FormField<Long>) formData.get(language, "tt_number")).get(), is(1234L));
		assertThat(((de.espirit.firstspirit.forms.FormField<Long>) formData.get(language, "tt_second_number")).get(), is(123456L));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void mapTFormDataWithInvalidTestClass() throws Exception {
		InvalidTestClass fromTest = new InvalidTestClass();
		fromTest.string = "fromString";
		fromTest.number = 1234L;

		LanguageBuilder languageBuilder = languageWith("DE");
		FormData formData = build(formDataWith().aForm(() ->
				gomEditorProviderWith("test").values(() ->
						asList(gomFormElementWith("tt_string"), gomFormElementWith("tt_second_string"), gomFormElementWith("tt_number"), gomFormElementWith("tt_second_number"))))
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_string")
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_second_string")
				.aValue(() ->
						FormFieldMock.<Long>formFieldWith().aType(Long.class), languageBuilder, "tt_number")
				.aValue(() -> FormFieldMock.<Long>formFieldWith().aType(Long.class), languageBuilder, "tt_second_number")
		);
		Language language = build(languageBuilder);
        SpecialistsBroker specialistsBroker = build(specialistsBrokerWith());

        formFieldMapper.map(fromTest, formData, language, specialistsBroker);
        assertThat(((de.espirit.firstspirit.forms.FormField<String>) formData.get(language, "tt_string")).get(), is(nullValue()));
		assertThat(((de.espirit.firstspirit.forms.FormField<String>) formData.get(language, "tt_second_string")).get(), is(nullValue()));
		assertThat(((de.espirit.firstspirit.forms.FormField<Long>) formData.get(language, "tt_number")).get(), is(nullValue()));
		assertThat(((de.espirit.firstspirit.forms.FormField<Long>) formData.get(language, "tt_second_number")).get(), is(nullValue()));
	}

	@Test
	public void mapFormDataLanguageT() throws Exception {
		TestClass toTest = new TestClass();

		LanguageBuilder languageBuilder = languageWith("DE");
        SpecialistsBroker specialistsBroker = build(specialistsBrokerWith());
        FormData formData = build(formDataWith().aForm(() ->
				gomEditorProviderWith("test").values(() ->
						asList(gomFormElementWith("tt_string"), gomFormElementWith("tt_second_string"), gomFormElementWith("tt_number"), gomFormElementWith("tt_second_number"))))
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aValue("fromString"), languageBuilder, "tt_string")
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aValue("fromSecondString"), languageBuilder, "tt_second_string")
				.aValue(() ->
						FormFieldMock.<Long>formFieldWith().aValue(1234L), languageBuilder, "tt_number")
				.aValue(() ->
						FormFieldMock.<Long>formFieldWith().aValue(123456L), languageBuilder, "tt_second_number")
		);

        formFieldMapper.map(formData, build(languageBuilder), specialistsBroker, toTest);
        assertThat(toTest.string, is("fromString"));
		assertThat(toTest.getSecondString(), is("fromSecondString"));
		assertThat(toTest.number, is(1234L));
		assertThat(toTest.getSecondNumber(), is(123456L));
	}

	@Test
	public void mapFormDataLanguageTWithInvalidTestClass() throws Exception {
		InvalidTestClass toTest = new InvalidTestClass();

		LanguageBuilder languageBuilder = languageWith("DE");
        SpecialistsBroker specialistsBroker = build(specialistsBrokerWith());
        FormData formData = build(formDataWith().aForm(() ->
				gomEditorProviderWith("test").values(() ->
						asList(gomFormElementWith("tt_string"), gomFormElementWith("tt_second_string"), gomFormElementWith("tt_number"), gomFormElementWith("tt_second_number"))))
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aValue("fromString"), languageBuilder, "tt_string")
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aValue("fromSecondString"), languageBuilder, "tt_second_string")
				.aValue(() ->
						FormFieldMock.<Long>formFieldWith().aValue(1234L), languageBuilder, "tt_number")
				.aValue(() ->
						FormFieldMock.<Long>formFieldWith().aValue(123456L), languageBuilder, "tt_second_number")
		);

        formFieldMapper.map(formData, build(languageBuilder), specialistsBroker, toTest);
        assertThat(toTest.string, is(nullValue()));
		assertThat(toTest.getSecondString(), is(nullValue()));
		assertThat(toTest.number, is(nullValue()));
		assertThat(toTest.getSecondNumber(), is(nullValue()));
	}

	public static final class TestClass {

		@FormField("tt_string")
		public String string;

		@FormField("tt_number")
		public Long number;

		private String secondString;
		private Long secondNumber;

		@FormField("tt_second_string")
		public String getSecondString() {
			return secondString;
		}

		@FormField("tt_second_string")
		public void setSecondString(String secondString) {
			this.secondString = secondString;
		}

		@FormField("tt_second_number")
		public Long getSecondNumber() {
			return secondNumber;
		}

		@FormField("tt_second_number")
		public void setSecondNumber(Long secondNumber) {
			this.secondNumber = secondNumber;
		}
	}

	public static final class InvalidTestClass {

		@FormField("tt_string2")
		public String string;

		public Long number;

		private String secondString;
		private Long secondNumber;

		@FormField("tt_second_string2")
		public String getSecondString() {
			return secondString;
		}

		@FormField("tt_second_string2")
		public void setSecondString(String secondString) {
			this.secondString = secondString;
		}

		public Long getSecondNumber() {
			return secondNumber;
		}

		public void setSecondNumber(Long secondNumber) {
			this.secondNumber = secondNumber;
		}

	}

}