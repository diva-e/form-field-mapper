package com.divae.firstspirit.strategy;

import com.divae.firstspirit.access.LanguageMock.LanguageBuilder;
import com.divae.firstspirit.access.ReferenceEntryMock.ReferenceEntryBuilder;
import com.divae.firstspirit.access.project.ProjectMock.ProjectBuilder;
import com.divae.firstspirit.access.store.templatestore.MappingMock;
import com.divae.firstspirit.access.store.templatestore.TemplateStoreRootMock.TemplateStoreRootBuilder;
import com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.GomFormElementBuilder;
import com.divae.firstspirit.annotation.Database;
import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import com.divae.firstspirit.or.query.SelectMock;
import com.divae.firstspirit.or.schema.EntityMock.EntityBuilder;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormDataList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.ReferenceEntryMock.referenceEntryWith;
import static com.divae.firstspirit.access.editor.fslist.IdProvidingFormDataMock.idProvidingFormDataWith;
import static com.divae.firstspirit.access.editor.value.ContentFormsProducerMock.contentFormsProducerWith;
import static com.divae.firstspirit.access.project.ProjectMock.projectWith;
import static com.divae.firstspirit.access.store.contentstore.Content2Mock.content2With;
import static com.divae.firstspirit.access.store.contentstore.ContentStoreRootMock.contentStoreRootWith;
import static com.divae.firstspirit.access.store.templatestore.MappingMock.mappingWith;
import static com.divae.firstspirit.access.store.templatestore.SchemaMock.schemaWith;
import static com.divae.firstspirit.access.store.templatestore.TableTemplateMock.tableTemplateWith;
import static com.divae.firstspirit.access.store.templatestore.TemplateStoreRootMock.templateStoreRootWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.forms.FormDataListMock.formDataListWith;
import static com.divae.firstspirit.forms.FormFieldMock.formFieldWith;
import static com.divae.firstspirit.or.EntityListMock.entityListWith;
import static com.divae.firstspirit.or.SessionMock.sessionWith;
import static com.divae.firstspirit.or.schema.AttributeMock.attributeWith;
import static com.divae.firstspirit.or.schema.EntityMock.entityWith;
import static de.espirit.firstspirit.access.store.IDProvider.UidType.TEMPLATESTORE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FormDataListDatabaseMappingStrategyTest {

    private final FormDataListDatabaseMappingStrategy formDataListDatabaseMappingStrategy = new FormDataListDatabaseMappingStrategy();

    @Test
    public void matches() throws Exception {
        assertThat(formDataListDatabaseMappingStrategy.matches(FormDataList.class, Collection.class), is(true));
        assertThat(formDataListDatabaseMappingStrategy.matches(Collection.class, FormDataList.class), is(true));
        assertThat(formDataListDatabaseMappingStrategy.matches(FormDataList.class, SecondTestClass.class), is(true));
        assertThat(formDataListDatabaseMappingStrategy.matches(SecondTestClass.class, FormDataList.class), is(true));
    }

    @Test
    public void mapFieldOFormFieldGomFormElementLanguage() throws Exception {
        TestClass testClass = new TestClass();
        SecondTestClass secondTest = new SecondTestClass();
        secondTest.fsId = 1L;
        secondTest.string = "test";
        testClass.singleObject = secondTest;

        LanguageBuilder languageBuilder = languageWith("DE");
        ProjectBuilder projectBuilder = projectWith("test", 0, languageBuilder);

        GomFormElementBuilder gomFormElementBuilder = gomFormElementWith("tt_single_object");
        TemplateStoreRootBuilder templateStoreRootBuilder = templateStoreRootWith(1, projectBuilder);
        EntityBuilder entityBuilder = entityWith(randomUUID()).aValue("fs_id", 1);
        de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
                build(formDataListWith().values(() ->
                        singletonList(idProvidingFormDataWith(6L).aValue(() ->
                                FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_single_object")
                                .aForm(() ->
                                        gomEditorProviderWith("test").values(() ->
                                                singletonList(gomFormElementBuilder)))))
                        .aProducer(() ->
                                contentFormsProducerWith().creates(() ->
                                        idProvidingFormDataWith(5L).aValue(() ->
                                                FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_string")
                                                .aValue(() ->
                                                        FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_private_string")
                                                .aForm(() ->
                                                        gomEditorProviderWith("test").values(() ->
                                                                asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))), entityBuilder)
                                        .aTableTemplate(tableTemplateWith("tableTemplate", 3L, TEMPLATESTORE, templateStoreRootBuilder).mappings(() ->
                                                new MappingMock.MappingBuilder[]{mappingWith().aName("st_string").aDBAttribute(() -> attributeWith().aName("string"), languageBuilder),
                                                        mappingWith().aName("st_private_string").aDBAttribute(() -> attributeWith().aName("private_string"), languageBuilder)}, true)
                                                .incomingReferences(() -> new ReferenceEntryBuilder[]{referenceEntryWith(4L).aReferencedElement(content2With("database", 5L, contentStoreRootWith(2, projectBuilder))
                                                        .anEntityTypeName("database").aSchema(schemaWith("schema", 6L, templateStoreRootBuilder).aSession(() ->
                                                                sessionWith().aSelect(SelectMock::selectWith, "database", build(entityListWith().values(singletonList(build(entityBuilder))))), true)))})))))
        );
        Language language = build(languageBuilder);
        GomFormElement gomFormElement = build(gomFormElementBuilder);

        formDataListDatabaseMappingStrategy.map(getInstance(testClass.getClass().getField("singleObject")), testClass, formField, gomFormElement, language);
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
        ProjectBuilder projectBuilder = projectWith("test", 0, languageBuilder);

        GomFormElementBuilder gomFormElementBuilder = gomFormElementWith("tt_private_single_object");
        TemplateStoreRootBuilder templateStoreRootBuilder = templateStoreRootWith(1, projectBuilder);
        EntityBuilder entityBuilder = entityWith(randomUUID());
        de.espirit.firstspirit.forms.FormField<FormDataList> formField = build(FormFieldMock.<FormDataList>formFieldWith().aValue(
                build(formDataListWith().values(() ->
                        singletonList(idProvidingFormDataWith(6L).aValue(() ->
                                FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "tt_private_single_object")
                                .aForm(() ->
                                        gomEditorProviderWith("test").values(() ->
                                                singletonList(gomFormElementBuilder)))))
                        .aProducer(() ->
                                contentFormsProducerWith().creates(() ->
                                        idProvidingFormDataWith(5L).aValue(() ->
                                                FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_string")
                                                .aValue(() ->
                                                        FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_private_string")
                                                .aForm(() ->
                                                        gomEditorProviderWith("test").values(() ->
                                                                asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))), entityBuilder)
                                        .aTableTemplate(tableTemplateWith("tableTemplate", 3L, TEMPLATESTORE, templateStoreRootBuilder).mappings(() ->
                                                new MappingMock.MappingBuilder[]{mappingWith().aName("st_string").aDBAttribute(() -> attributeWith().aName("string"), languageBuilder),
                                                        mappingWith().aName("st_private_string").aDBAttribute(() -> attributeWith().aName("private_string"), languageBuilder)}, true)
                                                .incomingReferences(() -> new ReferenceEntryBuilder[]{referenceEntryWith(4L).aReferencedElement(content2With("database", 5L, contentStoreRootWith(2, projectBuilder))
                                                        .anEntityTypeName("database").aSchema(schemaWith("schema", 6L, templateStoreRootBuilder).aSession(() ->
                                                                sessionWith().aSelect(SelectMock::selectWith, "database", build(entityListWith().values(singletonList(build(entityBuilder))))), true)))})))))
        );
        Language language = build(languageBuilder);
        GomFormElement gomFormElement = build(gomFormElementBuilder);

        formDataListDatabaseMappingStrategy.map(getInstance(testClass.getClass().getMethod("getPrivateSingleObject")), testClass, formField, gomFormElement, language);
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

        formDataListDatabaseMappingStrategy.map(formField, gomFormElement, language, getInstance(testClass.getClass().getField("singleObject")), testClass);
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

        formDataListDatabaseMappingStrategy.map(formField, gomFormElement, language, getInstance(testClass.getClass().getMethod("setPrivateSingleObject", SecondTestClass.class)), testClass);
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

        ProjectBuilder projectBuilder = projectWith("test", 0, languageBuilder);

        TemplateStoreRootBuilder templateStoreRootBuilder = templateStoreRootWith(1, projectBuilder);
        EntityBuilder entityBuilder = entityWith(randomUUID());
        FormDataList formDataList = build(formDataListWith().aProducer(() ->
                contentFormsProducerWith().creates(() ->
                        idProvidingFormDataWith(5L).aValue(() ->
                                FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_string")
                                .aValue(() ->
                                        FormFieldMock.<String>formFieldWith().aType(String.class), languageBuilder, "st_private_string")
                                .aForm(() ->
                                        gomEditorProviderWith("test").values(() ->
                                                asList(gomFormElementWith("st_string"), gomFormElementWith("st_private_string")))), entityBuilder)
                        .aTableTemplate(tableTemplateWith("tableTemplate", 3L, TEMPLATESTORE, templateStoreRootBuilder).mappings(() ->
                                new MappingMock.MappingBuilder[]{mappingWith().aName("st_string").aDBAttribute(() -> attributeWith().aName("string"), languageBuilder),
                                        mappingWith().aName("st_private_string").aDBAttribute(() -> attributeWith().aName("private_string"), languageBuilder)}, true)
                                .incomingReferences(() -> new ReferenceEntryBuilder[]{referenceEntryWith(4L).aReferencedElement(content2With("database", 5L, contentStoreRootWith(2, projectBuilder))
                                        .anEntityTypeName("database").aSchema(schemaWith("schema", 6L, templateStoreRootBuilder).aSession(() ->
                                                sessionWith().aSelect(SelectMock::selectWith, "database", build(entityListWith().values(singletonList(build(entityBuilder))))), true)))})))
        );
        Language language = build(languageBuilder);

        formDataListDatabaseMappingStrategy.map(secondTests, formDataList, language);

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

        SecondTestClass secondTest = formDataListDatabaseMappingStrategy.map(providingFormData, language, SecondTestClass.class.getDeclaredConstructor());

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

        SecondTestClass secondTest = (SecondTestClass) formDataListDatabaseMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getField("singleObject")));
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
        Collection<Object> objects = (Collection<Object>) formDataListDatabaseMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getField("objects")));

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

        SecondTestClass secondTest = (SecondTestClass) formDataListDatabaseMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getMethod("setPrivateSingleObject", SecondTestClass.class)));
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
        Collection<Object> objects = (Collection<Object>) formDataListDatabaseMappingStrategy.map(formDataList, language, getInstance(TestClass.class.getMethod("setPrivateObjects", Collection.class)));

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

        Collection<SecondTestClass> secondTests = formDataListDatabaseMappingStrategy.map(formDataList, language, SecondTestClass.class.getDeclaredConstructor());

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

    @Database("database")
    public static final class SecondTestClass {

        @FormField(value = "fs_id", isEntityName = true)
        public Long fsId;

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