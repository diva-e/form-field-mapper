package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.FormFieldMapper;
import com.divae.firstspirit.annotation.DatabaseServant;
import com.divae.firstspirit.content2.Content2Servant;
import com.divae.firstspirit.entity.EntityServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.value.DatasetContainer;
import de.espirit.firstspirit.access.store.contentstore.Content2;
import de.espirit.firstspirit.access.store.contentstore.Dataset;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.forms.FormField;
import de.espirit.or.schema.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static de.espirit.common.base.Logging.logWarning;
import static de.espirit.firstspirit.access.editor.value.DatasetContainer.Factory.create;
import static org.apache.commons.lang.Validate.notNull;

public class DatasetContainerMappingStrategy implements MappingStrategy {

    private static final DatabaseServant DATABASE_SERVANT = new DatabaseServant();
    private final static Content2Servant CONTENT2_SERVANT = new Content2Servant();
    private final static EntityServant ENTITY_SERVANT = new EntityServant();
    private static final FormFieldMapper FORM_FIELD_MAPPER = new FormFieldMapper();

    @Override
    public boolean matches(final Class<?> fromType, final Class<?> toType) {
        notNull(fromType);
        notNull(toType);

        return DatasetContainer.class.isAssignableFrom(fromType) && DATABASE_SERVANT.hasDatabaseAnnotation(toType)
                || DATABASE_SERVANT.hasDatabaseAnnotation(fromType) && DatasetContainer.class.isAssignableFrom(toType);
    }

    @Override
    public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language, final SpecialistsBroker specialistsBroker) {
        notNull(from);
        notNull(fromObject);
        notNull(to);

        final Dataset dataset = map(from.get(fromObject), language, specialistsBroker);
        to.set(create(dataset, language));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final SpecialistsBroker specialistsBroker, final AnnotatedMember to, final O toObject) {
        notNull(from);
        notNull(to);
        notNull(toObject);

        final DatasetContainer datasetContainer = ((FormField<DatasetContainer>) from).get();
        if (datasetContainer == null) {
            return;
        }

        final Object object = map(datasetContainer.getDataset().getEntity(), datasetContainer.getDataset().getTableTemplate(), language, specialistsBroker, to.getSetDeclaredConstructor());
        if (object == null) {
            return;
        }

        to.set(toObject, object);
    }

    <O> Dataset map(final O object, final Language language, final SpecialistsBroker specialistsBroker) {
        final Class<?> objectClass = object.getClass();
        final String databaseUid = DATABASE_SERVANT.getDatabaseUid(objectClass);
        if (databaseUid == null) {
            return null;
        }

        return map(objectClass, object, databaseUid, language, specialistsBroker);
    }

    <O> Dataset map(final Class<?> objectClass, final O object, final String databaseUid, final Language language, final SpecialistsBroker specialistsBroker) {
        final Content2 database = CONTENT2_SERVANT.getDatabase(specialistsBroker, databaseUid);
        if (database == null) {
            logWarning("Could not get database with uid [" + databaseUid + "]", getClass());
            return null;
        }
        final TableTemplate.Mapping[] tableTemplateMappings = database.getTemplate().getMappings(true);
        final Map<com.divae.firstspirit.annotation.FormField, Object> columnValueMapping = DATABASE_SERVANT.getColumnValueMapping(objectClass, object);
        final Entity entity = ENTITY_SERVANT.findEntity(database.getSchema(), database.getEntityType().getName(), tableTemplateMappings, columnValueMapping, language);
        return entity != null ? database.getDataset(entity) : null;
    }

    <O> O map(final Entity entity, final TableTemplate tableTemplate, final Language language, final SpecialistsBroker specialistsBroker, final Constructor<O> constructor) {
        final O object;
        try {
            object = constructor.newInstance();
        } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logWarning("Could not create object", e, getClass());
            return null;
        }

        return map(entity, language, specialistsBroker, tableTemplate, object);
    }

    <O> O map(final Entity entity, final Language language, final SpecialistsBroker specialistsBroker, final TableTemplate tableTemplate, O object) {
        final Class<?> objectClass = object.getClass();
        final String databaseUid = DATABASE_SERVANT.getDatabaseUid(objectClass);
        if (databaseUid == null) {
            return null;
        }
        final Content2 database = CONTENT2_SERVANT.getDatabase(tableTemplate, databaseUid);
        if (database == null) {
            logWarning("Could not get database with uid [" + databaseUid + "]", getClass());
            return null;
        }

        FORM_FIELD_MAPPER.map(database.getDataset(entity).getFormData(), language, specialistsBroker, object);
        return object;
    }

}
