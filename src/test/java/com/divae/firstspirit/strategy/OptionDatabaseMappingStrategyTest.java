package com.divae.firstspirit.strategy;

import com.divae.firstspirit.access.LanguageMock.LanguageBuilder;
import com.divae.firstspirit.access.ReferenceEntryMock.ReferenceEntryBuilder;
import com.divae.firstspirit.access.project.ProjectMock.ProjectBuilder;
import com.divae.firstspirit.access.store.contentstore.Content2Mock.Content2Builder;
import com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.GomFormElementBuilder;
import com.divae.firstspirit.agency.SpecialistsBrokerMock.TruncatedSpecialistsBrokerBuilder;
import com.divae.firstspirit.annotation.Database;
import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import com.divae.firstspirit.or.schema.EntityMock.EntityBuilder;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import org.junit.Test;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.GomIncludeConfigurationAndOptionFactoryProviderMock.gomIncludeConfigurationAndOptionFactoryProviderWith;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.ReferenceEntryMock.referenceEntryWith;
import static com.divae.firstspirit.access.editor.OptionModelAndTableTemplateProviderMock.optionModelAndTableTemplateProviderWith;
import static com.divae.firstspirit.access.editor.value.OptionFactoryMock.optionFactoryWith;
import static com.divae.firstspirit.access.editor.value.OptionMock.optionWith;
import static com.divae.firstspirit.access.project.ProjectMock.projectWith;
import static com.divae.firstspirit.access.store.contentstore.Content2Mock.content2With;
import static com.divae.firstspirit.access.store.contentstore.ContentStoreRootMock.contentStoreRootWith;
import static com.divae.firstspirit.access.store.contentstore.DatasetMock.datasetWith;
import static com.divae.firstspirit.access.store.templatestore.TableTemplateMock.tableTemplateWith;
import static com.divae.firstspirit.access.store.templatestore.TemplateStoreRootMock.templateStoreRootWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.agency.SpecialistsBrokerMock.specialistsBrokerWith;
import static com.divae.firstspirit.forms.FormDataMock.formDataWith;
import static com.divae.firstspirit.or.schema.EntityMock.entityWith;
import static de.espirit.firstspirit.access.store.IDProvider.UidType.TEMPLATESTORE;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OptionDatabaseMappingStrategyTest {

    private final OptionDatabaseMappingStrategy optionDatabaseMappingStrategy = new OptionDatabaseMappingStrategy();

    @Test
    public void matches() throws Exception {
        assertThat(optionDatabaseMappingStrategy.matches(Option.class, SecondTestClass.class), is(true));
        assertThat(optionDatabaseMappingStrategy.matches(SecondTestClass.class, Option.class), is(true));
    }

    @Test
    public void mapFormFieldFieldO() throws Exception {
        final EntityBuilder entity = entityWith(randomUUID()).aValue("fs_id", 1L);
        final TruncatedSpecialistsBrokerBuilder<SpecialistsBroker> specialistsBroker = specialistsBrokerWith();
        final LanguageBuilder language = languageWith("DE");
        final ProjectBuilder project = projectWith("project", 0, language);

        final Content2Builder content2 = content2With("database", 4, contentStoreRootWith(3, project));

        final GomFormElementBuilder gomFormElement = gomFormElementWith("fs_id").anIncludeConfiguration(() ->
                gomIncludeConfigurationAndOptionFactoryProviderWith().anOptionFactory(optionFactoryWith().anOptionModel(() ->
                        optionModelAndTableTemplateProviderWith().aTableTemplate(tableTemplateWith("table_template", 2, TEMPLATESTORE, templateStoreRootWith(1, project)).incomingReferences(() ->
                                new ReferenceEntryBuilder[]{referenceEntryWith(5).aReferencedElement(content2)})
                        ), specialistsBroker, language, true)));

        content2.aDataset(parent ->
                datasetWith("dataset", 6, parent).aFormData(() ->
                        formDataWith().aValue(() ->
                                FormFieldMock.<Long>formFieldWith().aValue(1L), language, "fs_id").aForm(() ->
                                gomEditorProviderWith("gomEditorProvider").values(() -> singletonList(gomFormElement)))
                ), () -> entity);


        de.espirit.firstspirit.forms.FormField<Option> formField = build(FormFieldMock.<Option>formFieldWith().aValue(build(optionWith().aValue(build(entity)))));

        TestClass test = new TestClass();

        optionDatabaseMappingStrategy.map(formField, build(gomFormElement), build(language), build(specialistsBroker), getInstance(test.getClass().getField("option")), test);

        assertThat(test.option.fsId, is(1L));
    }

    @Test
    public void mapFormFieldMethodO() throws Exception {
        final EntityBuilder entity = entityWith(randomUUID()).aValue("fs_id", 1L);
        final TruncatedSpecialistsBrokerBuilder<SpecialistsBroker> specialistsBroker = specialistsBrokerWith();
        final LanguageBuilder language = languageWith("DE");
        final ProjectBuilder project = projectWith("project", 0, language);

        final Content2Builder content2 = content2With("database", 4, contentStoreRootWith(3, project));

        final GomFormElementBuilder gomFormElement = gomFormElementWith("fs_id").anIncludeConfiguration(() ->
                gomIncludeConfigurationAndOptionFactoryProviderWith().anOptionFactory(optionFactoryWith().anOptionModel(() ->
                        optionModelAndTableTemplateProviderWith().aTableTemplate(tableTemplateWith("table_template", 2, TEMPLATESTORE, templateStoreRootWith(1, project)).incomingReferences(() ->
                                new ReferenceEntryBuilder[]{referenceEntryWith(5).aReferencedElement(content2)})
                        ), specialistsBroker, language, true)));

        content2.aDataset(parent ->
                datasetWith("dataset", 6, parent).aFormData(() ->
                        formDataWith().aValue(() ->
                                FormFieldMock.<Long>formFieldWith().aValue(1L), language, "fs_id").aForm(() ->
                                gomEditorProviderWith("gomEditorProvider").values(() -> singletonList(gomFormElement)))
                ), () -> entity);


        de.espirit.firstspirit.forms.FormField<Option> formField = build(FormFieldMock.<Option>formFieldWith().aValue(build(optionWith().aValue(build(entity)))));

        TestClass test = new TestClass();

        optionDatabaseMappingStrategy.map(formField, build(gomFormElement), build(language), build(specialistsBroker), getInstance(test.getClass().getMethod("setPrivateOption", SecondTestClass.class)), test);

        assertThat(test.getPrivateOption().fsId, is(1L));
    }

    public static final class TestClass {
        @FormField("tt_option")
        public SecondTestClass option;

        private SecondTestClass privateOption;

        @FormField("tt_option")
        public SecondTestClass getPrivateOption() {
            return privateOption;
        }

        @FormField("tt_option")
        public void setPrivateOption(SecondTestClass privateOption) {
            this.privateOption = privateOption;
        }
    }

    @Database("database")
    public static final class SecondTestClass {
        @FormField(value = "fs_id", isEntityName = true)
        public Long fsId;
    }
}