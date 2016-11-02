package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.annotation.Template;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.store.templatestore.SectionTemplate;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormDataList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.Creator.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.editor.fslist.IdProvidingFormDataMock.idProvidingFormDataWith;
import static com.divae.firstspirit.access.editor.value.SectionFormsProducerMock.sectionFormsProducerWith;
import static com.divae.firstspirit.access.project.ProjectMock.projectWith;
import static com.divae.firstspirit.access.store.templatestore.SectionTemplateMock.sectionTemplateWith;
import static com.divae.firstspirit.access.store.templatestore.TemplateStoreRootMock.templateStoreRootWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.forms.FormDataListMock.formDataListWith;
import static com.divae.firstspirit.forms.FormFieldMock.formFieldWith;
import static de.espirit.firstspirit.access.store.IDProvider.UidType.TEMPLATESTORE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FormDataListMappingStrategyTest {

	private final FormDataListMappingStrategy formDataListMappingStrategy = new FormDataListMappingStrategy();

	@Test
	public void matches() throws Exception {
		assertThat(formDataListMappingStrategy.matches(FormDataList.class, Collection.class), is(true));
		assertThat(formDataListMappingStrategy.matches(Collection.class, FormDataList.class), is(true));
		assertThat(formDataListMappingStrategy.matches(FormDataList.class, SecondTestClass.class), is(true));
		assertThat(formDataListMappingStrategy.matches(SecondTestClass.class, FormDataList.class), is(true));
	}

	@Test
	public void mapFieldOFormFieldGomFormElementLanguage() throws Exception {
		TestClass testClass = new TestClass();
		SecondTestClass secondTest = new SecondTestClass();
		secondTest.string = "test";
		testClass.singleObject = secondTest;

		Project project = build(projectWith("test", 0, "DE"));
		Language language = build(languageWith("DE"));

		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(FormFieldMock.<String>formFieldWith().aType(String.class))).aValue(language, "st_private_string",
				build(FormFieldMock.<String>formFieldWith().aType(String.class))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));

		GomFormElement gomFormElement = build(gomFormElementWith("tt_single_object"));
		SectionTemplate sectionTemplate = build(sectionTemplateWith("st_template", 2, TEMPLATESTORE, templateStoreRootWith(1, project)));
		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
				build(formDataListWith().values(
						build(idProvidingFormDataWith(1L).aValue(language, "tt_single_object",
								build(FormFieldMock.<String>formFieldWith().aType(String.class))).aForm(
								build(gomEditorProviderWith("test").values(gomFormElement))))).aProducer(
						build(sectionFormsProducerWith().allowedTemplates(
								sectionTemplate
						).creates(idProvidingFormData, sectionTemplate))
				))));

		formDataListMappingStrategy.map(getInstance(testClass.getClass().getField("singleObject")), testClass, formField, gomFormElement, language);
		FormDataList formDataList = formField.get();
		assertThat(formDataList.size(), is(1));
		assertThat((String)formDataList.get(0).get(language, "st_string").get(), is("test"));
	}

	@Test
	public void mapMethodOFormFieldGomFormElementLanguage() throws Exception {
		TestClass testClass = new TestClass();
		SecondTestClass secondTest = new SecondTestClass();
		secondTest.string = "test";
		testClass.setPrivateSingleObject(secondTest);

		Project project = build(projectWith("test", 0, "DE"));
		Language language = build(languageWith("DE"));

		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(FormFieldMock.<String>formFieldWith().aType(String.class))).aValue(language, "st_private_string",
				build(FormFieldMock.<String>formFieldWith().aType(String.class))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));

		GomFormElement gomFormElement = build(gomFormElementWith("tt_private_single_object"));
		SectionTemplate sectionTemplate = build(sectionTemplateWith("st_template", 2, TEMPLATESTORE, templateStoreRootWith(1, project)));
		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
				build(formDataListWith().values(
						build(idProvidingFormDataWith(1L).aValue(language, "tt_private_single_object",
								build(FormFieldMock.<String>formFieldWith().aType(String.class))).aForm(
								build(gomEditorProviderWith("test").values(gomFormElement))))).aProducer(
						build(sectionFormsProducerWith().allowedTemplates(
								sectionTemplate
						).creates(idProvidingFormData, sectionTemplate))
				))));

		formDataListMappingStrategy.map(getInstance(testClass.getClass().getMethod("getPrivateSingleObject")), testClass, formField, gomFormElement, language);
		FormDataList formDataList = formField.get();
		assertThat(formDataList.size(), is(1));
		assertThat((String)formDataList.get(0).get(language, "st_string").get(), is("test"));
	}

	@Test
	public void mapFormFieldGomFormElementLanguageFieldO() throws Exception {
		Language language = build(languageWith("DE"));


		GomFormElement gomFormElement = build(gomFormElementWith("st_string"));

		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
				build(formDataListWith().values(
						build(idProvidingFormDataWith(1L).aValue(language, "st_string",
								build(formFieldWith().aValue("test"))).aForm(
								build(gomEditorProviderWith("test").values(gomFormElement))))))));

		TestClass testClass = new TestClass();
		formDataListMappingStrategy.map(formField, gomFormElement, language, getInstance(testClass.getClass().getField("singleObject")), testClass);
		assertThat(testClass.singleObject.string, is("test"));
	}

	@Test
	public void mapFormFieldGomFormElementLanguageMethodO() throws Exception {
		Language language = build(languageWith("DE"));

		GomFormElement gomFormElement = build(gomFormElementWith("st_string"));

		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
				build(formDataListWith().values(
						build(idProvidingFormDataWith(1L).aValue(language, "st_string",
								build(formFieldWith().aValue("test"))).aForm(
								build(gomEditorProviderWith("test").values(gomFormElement))))))));

		TestClass testClass = new TestClass();
		formDataListMappingStrategy.map(formField, gomFormElement, language, getInstance(testClass.getClass().getMethod("setPrivateSingleObject", SecondTestClass.class)), testClass);
		assertThat(testClass.getPrivateSingleObject().string, is("test"));
	}

	@Test
	public void mapCollectionFormDataListLanguage() throws Exception {
		Language language = build(languageWith("DE"));

		Collection<SecondTestClass> secondTests = new ArrayList<>();
		SecondTestClass firstSecondTest = new SecondTestClass();
		firstSecondTest.string = "string";
		firstSecondTest.setPrivateString("privateString");
		secondTests.add(firstSecondTest);

		SecondTestClass secondSecondTest = new SecondTestClass();
		secondSecondTest.string = "string";
		secondSecondTest.setPrivateString("privateString");
		secondTests.add(secondSecondTest);

		Project project = build(projectWith("test", 0, "DE"));
		SectionTemplate sectionTemplate = build(sectionTemplateWith("st_template", 2, TEMPLATESTORE, templateStoreRootWith(1, project)));

		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(FormFieldMock.<String>formFieldWith().aType(String.class))).aValue(language, "st_private_string",
				build(FormFieldMock.<String>formFieldWith().aType(String.class))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));

		FormDataList formDataList = build(formDataListWith().aProducer(build(sectionFormsProducerWith().allowedTemplates(sectionTemplate).creates(idProvidingFormData, sectionTemplate))));

		formDataListMappingStrategy.map(secondTests, formDataList, language);

		assertThat(formDataList.size(), is(2));
		assertThat((String)formDataList.get(0).get(language, "st_string").get(), is("string"));
		assertThat((String)formDataList.get(0).get(language, "st_private_string").get(), is("privateString"));
		assertThat((String)formDataList.get(1).get(language, "st_string").get(), is("string"));
		assertThat((String)formDataList.get(1).get(language, "st_private_string").get(), is("privateString"));
	}

	@Test
	public void mapIdProvidingFormDataLanguageConstructor() throws Exception {
		Language language = build(languageWith("DE"));
		IdProvidingFormData providingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(formFieldWith().aValue("string"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));

		SecondTestClass secondTest = formDataListMappingStrategy.map(providingFormData, language, SecondTestClass.class.getDeclaredConstructor());

		assertThat(secondTest.string, is("string"));
		assertThat(secondTest.getPrivateString(), is("privateString"));
	}

	@Test
	public void mapFormDataListLanguageFieldWithObject() throws Exception {
		Language language = build(languageWith("DE"));
		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(formFieldWith().aValue("string"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		IdProvidingFormData idProvidingFormData2 = build(idProvidingFormDataWith(2L).aValue(language, "st_string",
				build(formFieldWith().aValue("string2"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		FormDataList formDataList = build(formDataListWith().values(idProvidingFormData, idProvidingFormData2));

		SecondTestClass secondTest = (SecondTestClass)formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getField("singleObject")));
		assertThat(secondTest.string, is("string"));
		assertThat(secondTest.getPrivateString(), is("privateString"));
	}

	@Test
	public void mapFormDataListLanguageFieldWithObjects() throws Exception {
		Language language = build(languageWith("DE"));
		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(formFieldWith().aValue("string"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		IdProvidingFormData idProvidingFormData2 = build(idProvidingFormDataWith(2L).aValue(language, "st_string",
				build(formFieldWith().aValue("string2"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		FormDataList formDataList = build(formDataListWith().values(idProvidingFormData, idProvidingFormData2));

		@SuppressWarnings("unchecked")
		Collection<Object> objects = (Collection<Object>)formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getField("objects")));

		assertThat(objects.size(), is(2));

		Iterator<Object> iterator = objects.iterator();
		SecondTestClass firstObject = (SecondTestClass) iterator.next();
		assertThat(firstObject.string, is("string"));
		assertThat(firstObject.getPrivateString(), is("privateString"));

		SecondTestClass secondObject = (SecondTestClass) iterator.next();
		assertThat(secondObject.string, is("string2"));
		assertThat(secondObject.getPrivateString(), is("privateString"));
	}

	@Test
	public void mapFormDataListLanguageMethodWithSingleObject() throws Exception {
		Language language = build(languageWith("DE"));
		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(formFieldWith().aValue("string"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		IdProvidingFormData idProvidingFormData2 = build(idProvidingFormDataWith(2L).aValue(language, "st_string",
				build(formFieldWith().aValue("string2"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		FormDataList formDataList = build(formDataListWith().values(idProvidingFormData, idProvidingFormData2));

		SecondTestClass secondTest = (SecondTestClass)formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getMethod("setPrivateSingleObject", SecondTestClass.class)));
		assertThat(secondTest.string, is("string"));
		assertThat(secondTest.getPrivateString(), is("privateString"));

	}

	@Test
	public void mapFormDataListLanguageMethodWithObjects() throws Exception {
		Language language = build(languageWith("DE"));
		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(formFieldWith().aValue("string"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		IdProvidingFormData idProvidingFormData2 = build(idProvidingFormDataWith(2L).aValue(language, "st_string",
				build(formFieldWith().aValue("string2"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		FormDataList formDataList = build(formDataListWith().values(idProvidingFormData, idProvidingFormData2));

		@SuppressWarnings("unchecked")
		Collection<Object> objects = (Collection<Object>)formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getMethod("setPrivateObjects", Collection.class)));

		assertThat(objects.size(), is(2));

		Iterator<Object> iterator = objects.iterator();
		SecondTestClass firstObject = (SecondTestClass) iterator.next();
		assertThat(firstObject.string, is("string"));
		assertThat(firstObject.getPrivateString(), is("privateString"));

		SecondTestClass secondObject = (SecondTestClass) iterator.next();
		assertThat(secondObject.string, is("string2"));
		assertThat(secondObject.getPrivateString(), is("privateString"));
	}

	@Test
	public void mapFormDataListLanguageConstructor() throws Exception {
		Language language = build(languageWith("DE"));
		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(language, "st_string",
				build(formFieldWith().aValue("string"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		IdProvidingFormData idProvidingFormData2 = build(idProvidingFormDataWith(2L).aValue(language, "st_string",
				build(formFieldWith().aValue("string2"))).aValue(language, "st_private_string",
				build(formFieldWith().aValue("privateString"))).aForm(
				build(gomEditorProviderWith("test").values(
						build(gomFormElementWith("st_string")),
						build(gomFormElementWith("st_private_string"))))));
		FormDataList formDataList = build(formDataListWith().values(idProvidingFormData, idProvidingFormData2));

		Collection<SecondTestClass> secondTests = formDataListMappingStrategy.map(formDataList, language, SecondTestClass.class.getDeclaredConstructor());

		assertThat(secondTests.size(), is(2));

		Iterator<SecondTestClass> iterator = secondTests.iterator();
		SecondTestClass firstSecondTest = iterator.next();
		assertThat(firstSecondTest.string, is("string"));
		assertThat(firstSecondTest.getPrivateString(), is("privateString"));

		SecondTestClass secondSecondTest = iterator.next();
		assertThat(secondSecondTest.string, is("string2"));
		assertThat(secondSecondTest.getPrivateString(), is("privateString"));
	}

	public static final class TestClass {
		@FormField("tt_objects")
		public Collection<SecondTestClass> objects;

		@FormField("tt_single_object")
		public SecondTestClass singleObject;

		private SecondTestClass privateSingleObject;

		private Collection<SecondTestClass> privateObjects;

		@FormField("tt_private_single_object")
		public SecondTestClass getPrivateSingleObject() {
			return privateSingleObject;
		}

		@FormField("tt_private_single_object")
		public void setPrivateSingleObject(SecondTestClass privateSingleObject) {
			this.privateSingleObject = privateSingleObject;
		}

		@FormField("tt_private_objects")
		public Collection<SecondTestClass> getPrivateObjects() {
			return privateObjects;
		}

		@FormField("tt_private_objects")
		public void setPrivateObjects(Collection<SecondTestClass> privateObjects) {
			this.privateObjects = privateObjects;
		}
	}

	@Template("st_template")
	public static final class SecondTestClass {

		@FormField("st_string")
		public String string;

		private String privateString;

		@FormField("st_private_string")
		public String getPrivateString() {
			return privateString;
		}

		@FormField("st_private_string")
		public void setPrivateString(String privateString) {
			this.privateString = privateString;
		}
	}

}