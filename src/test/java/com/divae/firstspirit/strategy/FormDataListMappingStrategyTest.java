package com.divae.firstspirit.strategy;

import com.divae.firstspirit.access.LanguageMock.LanguageBuilder;
import com.divae.firstspirit.access.store.templatestore.SectionTemplateMock.SectionTemplateBuilder;
import com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.GomFormElementBuilder;
import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.annotation.Template;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
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
import static com.divae.firstspirit.access.store.templatestore.SectionTemplatesMock.sectionTemplatesWith;
import static com.divae.firstspirit.access.store.templatestore.TemplateStoreRootMock.templateStoreRootWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.forms.FormDataListMock.formDataListWith;
import static com.divae.firstspirit.forms.FormFieldMock.formFieldWith;
import static de.espirit.firstspirit.access.store.IDProvider.UidType.TEMPLATESTORE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
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

		LanguageBuilder languageBuilder = languageWith("DE");

		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(() ->
				FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_string")
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_private_string")
				.aForm(() ->
						gomEditorProviderWith("test").values(() ->
								asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))
		);

		GomFormElementBuilder gomFormElementBuilder = gomFormElementWith("tt_single_object");
		SectionTemplateBuilder sectionTemplateBuilder = sectionTemplateWith("st_template", 3, TEMPLATESTORE, sectionTemplatesWith("test", 2, templateStoreRootWith(1, projectWith("test", 0, languageBuilder))));
		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
				build(formDataListWith().values(() ->
						singletonList(idProvidingFormDataWith(1L).aValue(() ->
								FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_single_object")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												singletonList(gomFormElementBuilder)))))
						.aProducer(() ->
								sectionFormsProducerWith().allowedTemplates(singletonList(sectionTemplateBuilder)).creates(idProvidingFormData, build(sectionTemplateBuilder)))))
		);
		Language language = build(languageBuilder);
		GomFormElement gomFormElement = build(gomFormElementBuilder);

		formDataListMappingStrategy.map(getInstance(testClass.getClass().getField("singleObject")), testClass, formField, gomFormElement, language);
		FormDataList formDataList = formField.get();
		assertThat(formDataList.size(), is(1));
		assertThat(formDataList.get(0).get(language, "st_string").get(), is("test"));
	}

	@Test
	public void mapMethodOFormFieldGomFormElementLanguage() throws Exception {
		TestClass testClass = new TestClass();
		SecondTestClass secondTest = new SecondTestClass();
		secondTest.string = "test";
		testClass.setPrivateSingleObject(secondTest);

		LanguageBuilder languageBuilder = languageWith("DE");

		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(() ->
				FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_string")
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_private_string")
				.aForm(() ->
						gomEditorProviderWith("test").values(() ->
								asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))
		);

		GomFormElementBuilder gomFormElementBuilder = gomFormElementWith("tt_private_single_object");
		SectionTemplateBuilder sectionTemplateBuilder = sectionTemplateWith("st_template", 3, TEMPLATESTORE, sectionTemplatesWith("test", 2, templateStoreRootWith(1, projectWith("test", 0, languageBuilder))));
		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
				build(formDataListWith().values(() ->
						singletonList(idProvidingFormDataWith(1L).aValue(() ->
								FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_private_single_object")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												singletonList(gomFormElementBuilder)))))
						.aProducer(() ->
								sectionFormsProducerWith().allowedTemplates(singletonList(sectionTemplateBuilder))
										.creates(idProvidingFormData, build(sectionTemplateBuilder)))))
		);
		Language language = build(languageBuilder);
		GomFormElement gomFormElement = build(gomFormElementBuilder);

		formDataListMappingStrategy.map(getInstance(testClass.getClass().getMethod("getPrivateSingleObject")), testClass, formField, gomFormElement, language);
		FormDataList formDataList = formField.get();
		assertThat(formDataList.size(), is(1));
		assertThat(formDataList.get(0).get(language, "st_string").get(), is("test"));
	}

	@Test
	public void mapFormFieldGomFormElementLanguageFieldO() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");
		GomFormElementBuilder gomFormElementBuilder = gomFormElementWith("st_string");

		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(build(formDataListWith().values(() ->
				singletonList(idProvidingFormDataWith(1L).aValue(() -> formFieldWith().aValue("test"), languageBuilder, "st_string")
						.aForm(() ->
								gomEditorProviderWith("test").values(() ->
										singletonList(gomFormElementBuilder)))))))
		);
		GomFormElement gomFormElement = build(gomFormElementBuilder);
		Language language = build(languageBuilder);
		TestClass testClass = new TestClass();

		formDataListMappingStrategy.map(formField, gomFormElement, language, getInstance(testClass.getClass().getField("singleObject")), testClass);
		assertThat(testClass.singleObject.string, is("test"));
	}

	@Test
	public void mapFormFieldGomFormElementLanguageMethodO() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");

		GomFormElementBuilder gomFormElementBuilder = gomFormElementWith("st_string");

		de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(build(formDataListWith().values(() ->
				singletonList(idProvidingFormDataWith(1L).aValue(() ->
						formFieldWith().aValue("test"), languageBuilder, "st_string")
						.aForm(() ->
								gomEditorProviderWith("test").values(() ->
										singletonList(gomFormElementBuilder)))))))
		);
		GomFormElement gomFormElement = build(gomFormElementBuilder);
		Language language = build(languageBuilder);
		TestClass testClass = new TestClass();

		formDataListMappingStrategy.map(formField, gomFormElement, language, getInstance(testClass.getClass().getMethod("setPrivateSingleObject", SecondTestClass.class)), testClass);
		assertThat(testClass.getPrivateSingleObject().string, is("test"));
	}

	@Test
	public void mapCollectionFormDataListLanguage() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");

		Collection<SecondTestClass> secondTests = new ArrayList<>();
		SecondTestClass firstSecondTest = new SecondTestClass();
		firstSecondTest.string = "string";
		firstSecondTest.setPrivateString("privateString");
		secondTests.add(firstSecondTest);

		SecondTestClass secondSecondTest = new SecondTestClass();
		secondSecondTest.string = "string";
		secondSecondTest.setPrivateString("privateString");
		secondTests.add(secondSecondTest);

		SectionTemplateBuilder sectionTemplateBuilder = sectionTemplateWith("st_template", 3, TEMPLATESTORE, sectionTemplatesWith("test", 2, templateStoreRootWith(1, projectWith("test", 0, languageBuilder))));

		IdProvidingFormData idProvidingFormData = build(idProvidingFormDataWith(1L).aValue(() ->
				FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_string")
				.aValue(() ->
						FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_private_string")
				.aForm(() ->
						gomEditorProviderWith("test").values(() ->
								asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))
		);
		FormDataList formDataList = build(formDataListWith().aProducer(() ->
				sectionFormsProducerWith().allowedTemplates(singletonList(sectionTemplateBuilder)).creates(idProvidingFormData, build(sectionTemplateBuilder)))
		);
		Language language = build(languageBuilder);

		formDataListMappingStrategy.map(secondTests, formDataList, language);

		assertThat(formDataList.size(), is(2));
		assertThat(formDataList.get(0).get(language, "st_string").get(), is("string"));
		assertThat(formDataList.get(0).get(language, "st_private_string").get(), is("privateString"));
		assertThat(formDataList.get(1).get(language, "st_string").get(), is("string"));
		assertThat(formDataList.get(1).get(language, "st_private_string").get(), is("privateString"));
	}

	@Test
	public void mapIdProvidingFormDataLanguageConstructor() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");
		IdProvidingFormData providingFormData = build(idProvidingFormDataWith(1L).aValue(() ->
				formFieldWith().aValue("string"), languageBuilder, "st_string")
				.aValue(() ->
						formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
				.aForm(() ->
						gomEditorProviderWith("test").values(() ->
								asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))
		);
		Language language = build(languageBuilder);

		SecondTestClass secondTest = formDataListMappingStrategy.map(providingFormData, language, SecondTestClass.class.getDeclaredConstructor());

		assertThat(secondTest.string, is("string"));
		assertThat(secondTest.getPrivateString(), is("privateString"));
	}

	@Test
	public void mapFormDataListLanguageFieldWithObject() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");
		FormDataList formDataList = build(formDataListWith().values(() ->
				asList(idProvidingFormDataWith(1L).aValue(() ->
								formFieldWith().aValue("string"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))),
						idProvidingFormDataWith(2L).aValue(() ->
								formFieldWith().aValue("string2"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() -> asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))))
		);
		Language language = build(languageBuilder);

		SecondTestClass secondTest = (SecondTestClass) formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getField("singleObject")));
		assertThat(secondTest.string, is("string"));
		assertThat(secondTest.getPrivateString(), is("privateString"));
	}

	@Test
	public void mapFormDataListLanguageFieldWithObjects() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");
		FormDataList formDataList = build(formDataListWith().values(() ->
				asList(idProvidingFormDataWith(1L).aValue(() ->
								formFieldWith().aValue("string"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))),
						idProvidingFormDataWith(2L).aValue(() ->
								formFieldWith().aValue("string2"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))))));
		Language language = build(languageBuilder);

		@SuppressWarnings("unchecked")
		Collection<Object> objects = (Collection<Object>) formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getField("objects")));

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
		LanguageBuilder languageBuilder = languageWith("DE");
		FormDataList formDataList = build(formDataListWith().values(() ->
				asList(idProvidingFormDataWith(1L).aValue(() ->
								formFieldWith().aValue("string"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))),
						idProvidingFormDataWith(2L).aValue(() ->
								formFieldWith().aValue("string2"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() -> gomEditorProviderWith("test").values(() ->
										asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))))
		);
		Language language = build(languageBuilder);

		SecondTestClass secondTest = (SecondTestClass) formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getMethod("setPrivateSingleObject", SecondTestClass.class)));
		assertThat(secondTest.string, is("string"));
		assertThat(secondTest.getPrivateString(), is("privateString"));

	}

	@Test
	public void mapFormDataListLanguageMethodWithObjects() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");
		FormDataList formDataList = build(formDataListWith().values(() ->
				asList(idProvidingFormDataWith(1L).aValue(() ->
								formFieldWith().aValue("string"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))),
						idProvidingFormDataWith(2L).aValue(() ->
								formFieldWith().aValue("string2"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))))
		);
		Language language = build(languageBuilder);

		@SuppressWarnings("unchecked")
		Collection<Object> objects = (Collection<Object>) formDataListMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getMethod("setPrivateObjects", Collection.class)));

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
		LanguageBuilder languageBuilder = languageWith("DE");
		FormDataList formDataList = build(formDataListWith().values(() ->
				asList(idProvidingFormDataWith(1L).aValue(() ->
								formFieldWith().aValue("string"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))),
						idProvidingFormDataWith(2L).aValue(() ->
								formFieldWith().aValue("string2"), languageBuilder, "st_string")
								.aValue(() ->
										formFieldWith().aValue("privateString"), languageBuilder, "st_private_string")
								.aForm(() ->
										gomEditorProviderWith("test").values(() ->
												asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string"))))))
		);
		Language language = build(languageBuilder);

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