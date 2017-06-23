package com.divae.firstspirit.formdatalist;

import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.ReferenceEntry;
import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.editor.value.ContentFormsProducer;
import de.espirit.firstspirit.access.editor.value.SectionFormsProducer;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.contentstore.Content2;
import de.espirit.firstspirit.access.store.templatestore.Schema;
import de.espirit.firstspirit.access.store.templatestore.SectionTemplate;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate.Mapping;
import de.espirit.firstspirit.forms.FormDataList;
import de.espirit.or.EntityList;
import de.espirit.or.Session;
import de.espirit.or.query.And;
import de.espirit.or.query.Equal;
import de.espirit.or.query.Select;
import de.espirit.or.schema.Entity;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static de.espirit.common.base.Logging.logWarning;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang.Validate.notNull;

public final class FormDataListServant {

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

    public IdProvidingFormData createDatabaseIdProvidingFormData(final FormDataList formDataList, final String databaseUid, final Map<String, Object> columnValueMapping, final Language language) {
        notNull(formDataList);
        notNull(databaseUid);

        final ContentFormsProducer contentFormsProducer = (ContentFormsProducer) formDataList.getProducer();
        final TableTemplate tableTemplate = contentFormsProducer.getTableTemplate();
        final Mapping[] tableTemplateMappings = tableTemplate.getMappings(true);
        final Content2 database = getDatabase(tableTemplate, databaseUid);
        if (database == null) {
            logWarning("Could not get database with uid [" + databaseUid + "]", getClass());
            return null;
        }

        return contentFormsProducer.create(findEntity(database.getSchema(), tableTemplateMappings, columnValueMapping, language));
    }

    Content2 getDatabase(final TableTemplate tableTemplate, final String databaseUid) {
        for (final ReferenceEntry referenceEntry : tableTemplate.getIncomingReferences()) {
            final IDProvider idProvider = referenceEntry.getReferencedElement();
            if (!(idProvider instanceof Content2) || !databaseUid.equals(idProvider.getUid())) {
                continue;
            }
            return (Content2) idProvider;
        }
        return null;
    }

    Entity findEntity(final Schema schema, final Mapping[] tableTemplateMappings, final Map<String, Object> columnValueMapping, final Language language) {
        final Session session = schema.getSession(true);
        final Select select = session.createSelect(schema.getName());
        final And and = new And();
        final String languageAbbreviation = language.getAbbreviation();

        columnValueMapping.entrySet().parallelStream().forEach(columnValue -> {
            final String columnKey = columnValue.getKey();
            final Optional<String> attributeNameOptional = of(tableTemplateMappings).parallel()
                    .filter(tableTemplateMapping -> tableTemplateMapping.getName().equals(columnKey))
                    .map(filteredTableTemplateMapping -> filteredTableTemplateMapping.getDBAttribute(languageAbbreviation).getName()).findFirst();
            attributeNameOptional.ifPresent(attributeName -> and.add(new Equal(attributeName, columnValue.getValue())));
        });

        select.setConstraint(and);
        final EntityList entityList = session.executeQuery(select);
        if (entityList.isEmpty()) {
            return null;
        }

        if (entityList.size() > 1) {
            logWarning("Found more then one entity with query [" + String.valueOf(select) + "]. Will use first entity", getClass());
            return entityList.get(0);
        }

        return entityList.get(0);
    }

    Optional<SectionTemplate> getSectionTemplate(final Collection<SectionTemplate> sectionTemplates, final String uid) {
        return sectionTemplates.parallelStream().filter(sectionTemplate -> sectionTemplate.getUid().equals(uid)).findFirst();
    }
}
