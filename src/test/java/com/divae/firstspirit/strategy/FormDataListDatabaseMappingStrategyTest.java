package com.divae.firstspirit.strategy;

import com.divae.firstspirit.access.LanguageMock.LanguageBuilder;
import com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.GomFormElementBuilder;
import com.divae.firstspirit.annotation.Database;
import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormDataList;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.editor.fslist.IdProvidingFormDataMock.idProvidingFormDataWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.forms.FormDataListMock.formDataListWith;
import static com.divae.firstspirit.forms.FormFieldMock.formFieldWith;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
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