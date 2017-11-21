package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.FormFieldMapper;
import com.divae.firstspirit.annotation.DatabaseServant;
import com.divae.firstspirit.content2.Content2Servant;
import com.divae.firstspirit.entity.EntityServant;
import com.divae.firstspirit.gom.GomFormElementServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.access.editor.value.OptionFactory;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.contentstore.Content2;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate.Mapping;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.forms.FormField;
import de.espirit.or.schema.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static de.espirit.common.base.Logging.logWarning;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

public class OptionDatabaseMappingStrategy implements MappingStrategy {

    private static final DatabaseServant DATABASE_SERVANT = new DatabaseServant();
    private final static EntityServant ENTITY_SERVANT = new EntityServant();
    private final static Content2Servant CONTENT2_SERVANT = new Content2Servant();
    private static final FormFieldMapper FORM_FIELD_MAPPER = new FormFieldMapper();
    private static final GomFormElementServant GOM_FORM_ELEMENT_SERVANT = new GomFormElementServant();

    public boolean matches(final Class<?> fromType, final Class<?> toType) {
        notNull(fromType);
        notNull(toType);

        return Option.class.isAssignableFrom(fromType) && DATABASE_SERVANT.hasDatabaseAnnotation(toType)
                || DATABASE_SERVANT.hasDatabaseAnnotation(fromType) && Option.class.isAssignableFrom(toType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language, final SpecialistsBroker specialistsBroker) {
        notNull(from);
        notNull(fromObject);
        notNull(to);
        notNull(toGomFormElement);

        final Entity entity = map(from.get(fromObject), language, GOM_FORM_ELEMENT_SERVANT.getTableTemplate(toGomFormElement, specialistsBroker, language));
        final OptionFactory optionFactory = ((OptionFactoryProvider) toGomFormElement).getOptionFactory();

        to.set(optionFactory.create(entity));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final SpecialistsBroker specialistsBroker, final AnnotatedMember to, final O toObject) {
        notNull(from);
        notNull(to);
        notNull(toObject);

        final Option option = ((FormField<Option>) from).get();
        if (option == null) {
            return;
        }

        final Object value = option.getValue();
        isTrue(value instanceof Entity);

        final Object object = map((Entity) value, fromGomFormElement, language, specialistsBroker, to.getSetDeclaredConstructor());
        if (object == null) {
            return;
        }

        to.set(toObject, object);
    }

    <O> O map(final Entity entity, final GomFormElement fromGomFormElement, final Language language, final SpecialistsBroker specialistsBroker, final Constructor<O> constructor) {
        final O object;
        try {
            object = constructor.newInstance();
        } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logWarning("Could not create object", e, getClass());
            return null;
        }

        return map(entity, language, specialistsBroker, GOM_FORM_ELEMENT_SERVANT.getTableTemplate(fromGomFormElement, specialistsBroker, language), object);
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

    <O> Entity map(final O object, final Language language, final TableTemplate tableTemplate) {
        final Class<?> objectClass = object.getClass();
        final String databaseUid = DATABASE_SERVANT.getDatabaseUid(objectClass);
        if (databaseUid == null) {
            return null;
        }

        return map(objectClass, object, databaseUid, language, tableTemplate);
    }

    <O> Entity map(final Class<?> objectClass, final O object, final String databaseUid, final Language language, final TableTemplate tableTemplate) {
        final Content2 database = CONTENT2_SERVANT.getDatabase(tableTemplate, databaseUid);
        if (database == null) {
            logWarning("Could not get database with uid [" + databaseUid + "]", getClass());
            return null;
        }
        final Mapping[] tableTemplateMappings = tableTemplate.getMappings(true);
        final Map<com.divae.firstspirit.annotation.FormField, Object> columnValueMapping = DATABASE_SERVANT.getColumnValueMapping(objectClass, object);
        return ENTITY_SERVANT.findEntity(database.getSchema(), database.getEntityType().getName(), tableTemplateMappings, columnValueMapping, language);
    }
}