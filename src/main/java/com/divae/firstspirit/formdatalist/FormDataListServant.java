package com.divae.firstspirit.formdatalist;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.content2.Content2Servant;
import com.divae.firstspirit.dataset.DatasetServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.editor.value.ContentFormsProducer;
import de.espirit.firstspirit.access.editor.value.SectionFormsProducer;
import de.espirit.firstspirit.access.store.contentstore.Dataset;
import de.espirit.firstspirit.access.store.templatestore.SectionTemplate;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;
import de.espirit.firstspirit.forms.FormDataList;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static de.espirit.common.base.Logging.logWarning;
import static org.apache.commons.lang.Validate.notNull;

public final class FormDataListServant {

    private final static DatasetServant DATASET_SERVANT = new DatasetServant();
    private final static Content2Servant CONTENT2_SERVANT = new Content2Servant();

    public IdProvidingFormData createInlineIdProvidingFormData(final FormDataList formDataList, final String templateUid) {
        notNull(formDataList);
        notNull(templateUid);

        final SectionFormsProducer sectionFormsProducer = (SectionFormsProducer) formDataList.getProducer();

        final Optional<SectionTemplate> sectionTemplate = getSectionTemplate(sectionFormsProducer.getAllowedTemplates(), templateUid);
        if (!sectionTemplate.isPresent()) {
            logWarning("Could not get section template with uid [" + templateUid + "]", getClass());
            return null;
        }

        return sectionFormsProducer.create(sectionTemplate.get());
    }

    public IdProvidingFormData createDatabaseIdProvidingFormData(final FormDataList formDataList, final String databaseUid, final Map<FormField, Object> columnValueMapping, final Language language) {
        notNull(formDataList);
        notNull(databaseUid);

        final ContentFormsProducer contentFormsProducer = (ContentFormsProducer) formDataList.getProducer();
        final TableTemplate tableTemplate = contentFormsProducer.getTableTemplate();

        final Dataset dataset = DATASET_SERVANT.findDataset(databaseUid, columnValueMapping, tableTemplate, language);
        if (dataset == null) {
            return null;
        }
        return contentFormsProducer.create(dataset.getEntity());
    }

    Optional<SectionTemplate> getSectionTemplate(final Collection<SectionTemplate> sectionTemplates, final String uid) {
        return sectionTemplates.parallelStream().filter(sectionTemplate -> sectionTemplate.getUid().equals(uid)).findFirst();
    }
}
