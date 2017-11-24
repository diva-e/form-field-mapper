package com.divae.firstspirit.dataset;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.content2.Content2Servant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.store.contentstore.Content2;
import de.espirit.firstspirit.access.store.contentstore.Dataset;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate.Mapping;
import de.espirit.or.EntityList;
import de.espirit.or.Session;
import de.espirit.or.query.And;
import de.espirit.or.query.Equal;
import de.espirit.or.query.Select;

import java.util.Map;
import java.util.Optional;

import static de.espirit.common.base.Logging.logWarning;
import static java.util.stream.Stream.of;

public final class DatasetServant {

    private final static Content2Servant CONTENT2_SERVANT = new Content2Servant();

    public Dataset findDataset(final String databaseUid, Map<com.divae.firstspirit.annotation.FormField, Object> columnValueMapping, final TableTemplate tableTemplate, final Language language) {
        final Content2 database = CONTENT2_SERVANT.getDatabase(tableTemplate, databaseUid);
        if (database == null) {
            logWarning("Could not get database with uid [" + databaseUid + "]", getClass());
            return null;
        }

        final Session session = database.getSchema().getSession(true);
        final Select select = session.createSelect(database.getEntityType().getName());
        final And and = new And();
        final String languageAbbreviation = language.getAbbreviation();
        final Mapping[] tableTemplateMappings = tableTemplate.getMappings(true);

        columnValueMapping.entrySet().parallelStream().forEach(columnValue -> {
            final FormField formField = columnValue.getKey();
            if (formField.isEntityName()) {
                and.add(new Equal(formField.value(), columnValue.getValue()));
                return;
            }
            final String columnKey = formField.value();
            final Optional<String> attributeNameOptional = of(tableTemplateMappings).parallel()
                    .filter(tableTemplateMapping -> tableTemplateMapping.getName().equals(columnKey))
                    .map(filteredTableTemplateMapping -> filteredTableTemplateMapping.getDBAttribute(languageAbbreviation).getName()).findFirst();
            attributeNameOptional.ifPresent(attributeName -> and.add(new Equal(attributeName, columnValue.getValue())));
        });

        select.setConstraint(and);
        final EntityList entityList = session.executeQuery(select);
        if (entityList.isEmpty()) {
            logWarning("Could not get an entity with constraint [" + and + "]", getClass());
            return null;
        }

        if (entityList.size() > 1) {
            logWarning("Found more then one entity with query [" + String.valueOf(select) + "]. Will use first entity", getClass());
            return database.getDataset(entityList.get(0));
        }

        return database.getDataset(entityList.get(0));
    }
}
