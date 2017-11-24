package com.divae.firstspirit.strategy;

import com.divae.firstspirit.access.LanguageMock.LanguageBuilder;
import com.divae.firstspirit.access.ReferenceEntryMock.ReferenceEntryBuilder;
import com.divae.firstspirit.access.project.ProjectMock.ProjectBuilder;
import com.divae.firstspirit.access.store.contentstore.Content2Mock.Content2Builder;
import com.divae.firstspirit.access.store.contentstore.DatasetMock.DatasetBuilder;
import com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.GomFormElementBuilder;
import com.divae.firstspirit.agency.SpecialistsBrokerMock.TruncatedSpecialistsBrokerBuilder;
import com.divae.firstspirit.annotation.Database;
import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import com.divae.firstspirit.or.schema.EntityMock.EntityBuilder;
import de.espirit.firstspirit.access.editor.value.DatasetContainer;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import org.junit.Test;

import java.util.UUID;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.ReferenceEntryMock.referenceEntryWith;
import static com.divae.firstspirit.access.editor.value.DatasetContainerMock.datasetContainerWith;
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

public class DatasetContainerMappingStrategyTest {

    private final DatasetContainerMappingStrategy datasetContainerMappingStrategy = new DatasetContainerMappingStrategy();

    @Test
    public void matches() throws Exception {
        assertThat(datasetContainerMappingStrategy.matches(DatasetContainer.class, SecondTestClass.class), is(true));
        assertThat(datasetContainerMappingStrategy.matches(SecondTestClass.class, DatasetContainer.class), is(true));
    }

    @Test
    public void mapFormFieldFieldO() throws Exception {
        final TruncatedSpecialistsBrokerBuilder<SpecialistsBroker> specialistsBroker = specialistsBrokerWith();
        final LanguageBuilder language = languageWith("DE");
        final ProjectBuilder project = projectWith("project", 0, language);

        final GomFormElementBuilder gomFormElement = gomFormElementWith("fs_id");
        final EntityBuilder entity = entityWith(randomUUID()).aValue("fs_id", 1L);
        final Content2Builder content2 = content2With("database", 4, contentStoreRootWith(3, project));

        DatasetBuilder dataset = datasetWith("dataset", 5L, content2).anEntity(entity).aTableTemplate(tableTemplateWith("table_template", 2, TEMPLATESTORE, templateStoreRootWith(1, project)).incomingReferences(() ->
                new ReferenceEntryBuilder[]{referenceEntryWith(5).aReferencedElement(content2)})).aFormData(() ->
                formDataWith().aValue(() ->
                        FormFieldMock.<Long>formFieldWith().aValue(1L), language, "fs_id").aForm(() ->
                        gomEditorProviderWith("gomEditorProvider").values(() -> singletonList(gomFormElement)))
        );

        content2.aDataset(parent -> dataset, () -> entity);

        de.espirit.firstspirit.forms.FormField<DatasetContainer> formField = build(FormFieldMock.<DatasetContainer>formFieldWith().aValue(build(datasetContainerWith(UUID.randomUUID()).aDataset(dataset))));

        TestClass test = new TestClass();

        datasetContainerMappingStrategy.map(formField, build(gomFormElement), build(language), build(specialistsBroker), getInstance(test.getClass().getField("dataset")), test);

        assertThat(test.dataset.fsId, is(1L));
    }

    @Test
    public void mapFormFieldMethodO() throws Exception {
        final TruncatedSpecialistsBrokerBuilder<SpecialistsBroker> specialistsBroker = specialistsBrokerWith();
        final LanguageBuilder language = languageWith("DE");
        final ProjectBuilder project = projectWith("project", 0, language);

        final GomFormElementBuilder gomFormElement = gomFormElementWith("fs_id");
        final EntityBuilder entity = entityWith(randomUUID()).aValue("fs_id", 1L);
        final Content2Builder content2 = content2With("database", 4, contentStoreRootWith(3, project));

        DatasetBuilder dataset = datasetWith("dataset", 5L, content2).anEntity(entity).aTableTemplate(tableTemplateWith("table_template", 2, TEMPLATESTORE, templateStoreRootWith(1, project)).incomingReferences(() ->
                new ReferenceEntryBuilder[]{referenceEntryWith(5).aReferencedElement(content2)})).aFormData(() ->
                formDataWith().aValue(() ->
                        FormFieldMock.<Long>formFieldWith().aValue(1L), language, "fs_id").aForm(() ->
                        gomEditorProviderWith("gomEditorProvider").values(() -> singletonList(gomFormElement)))
        );

        content2.aDataset(parent -> dataset, () -> entity);

        de.espirit.firstspirit.forms.FormField<DatasetContainer> formField = build(FormFieldMock.<DatasetContainer>formFieldWith().aValue(build(datasetContainerWith(UUID.randomUUID()).aDataset(dataset))));

        TestClass test = new TestClass();

        datasetContainerMappingStrategy.map(formField, build(gomFormElement), build(language), build(specialistsBroker), getInstance(test.getClass().getMethod("setPrivateDataset", SecondTestClass.class)), test);

        assertThat(test.getPrivateDataset().fsId, is(1L));
    }

    public static final class TestClass {
        @FormField("tt_dataset")
        public SecondTestClass dataset;

        private SecondTestClass privateDataset;

        @FormField("tt_dataset")
        public SecondTestClass getPrivateDataset() {
            return privateDataset;
        }

        @FormField("tt_dataset")
        public void setPrivateDataset(SecondTestClass privateDataset) {
            this.privateDataset = privateDataset;
        }
    }

    @Database("database")
    public static final class SecondTestClass {
        @FormField(value = "fs_id", isEntityName = true)
        public Long fsId;
    }
}