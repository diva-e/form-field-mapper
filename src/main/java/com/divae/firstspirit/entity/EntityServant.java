package com.divae.firstspirit.entity;

import com.divae.firstspirit.annotation.FormField;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.store.templatestore.Schema;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate.Mapping;
import de.espirit.or.EntityList;
import de.espirit.or.Session;
import de.espirit.or.query.And;
import de.espirit.or.query.Equal;
import de.espirit.or.query.Select;
import de.espirit.or.schema.Entity;

import java.util.Map;
import java.util.Optional;

import static de.espirit.common.base.Logging.logWarning;
import static java.util.stream.Stream.of;

public final class EntityServant {

    public Entity findEntity(final Schema schema, final String entityTypeName, final Mapping[] tableTemplateMappings, final Map<FormField, Object> columnValueMapping, final Language language) {
        final Session session = schema.getSession(true);
        final Select select = session.createSelect(entityTypeName);
        final And and = new And();
        final String languageAbbreviation = language.getAbbreviation();

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
            logWarning("Could not entity with constraint [" + and + "]", getClass());
            return null;
        }

        if (entityList.size() > 1) {
            logWarning("Found more then one entity with query [" + String.valueOf(select) + "]. Will use first entity", getClass());
            return entityList.get(0);
        }

        return entityList.get(0);
    }
}
