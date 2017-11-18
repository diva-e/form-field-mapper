package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.annotation.DatabaseServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.access.editor.value.OptionFactory;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.access.store.templatestore.gom.GomIncludeOptions;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.agency.StoreAgent;
import de.espirit.firstspirit.forms.FormField;
import de.espirit.firstspirit.store.access.contentstore.ContentOptionFactory;
import de.espirit.or.schema.Entity;

import static de.espirit.firstspirit.access.store.Store.Type.TEMPLATESTORE;
import static de.espirit.firstspirit.access.store.templatestore.TableTemplate.UID_TYPE;
import static org.apache.commons.lang.Validate.notNull;

public class OptionDatabaseMappingStrategy implements MappingStrategy {

    private static final DatabaseServant DATABASE_SERVANT = new DatabaseServant();

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

        final ContentOptionFactory type = (ContentOptionFactory) ((GomIncludeOptions) toGomFormElement.getIncludeConfiguration()).getOptionFactory();

        final TableTemplate storeElement = (TableTemplate) specialistsBroker.requireSpecialist(StoreAgent.TYPE).getStore(TEMPLATESTORE).getStoreElement(type.getTable(), UID_TYPE);

        final Entity entity = map(from.get(fromObject), language, specialistsBroker);
        final OptionFactory optionFactory = ((OptionFactoryProvider) toGomFormElement).getOptionFactory();

        to.set(optionFactory.create(entity));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final SpecialistsBroker specialistsBroker, final AnnotatedMember to, final O toObject) {
        notNull(from);
        notNull(to);
        notNull(toObject);

        to.set(toObject, ((FormField<Option>) from).get().getValue());
    }

    <O> Entity map(O object, final Language language, final SpecialistsBroker specialistsBroker) {
        final Class<?> objectClass = object.getClass();
        final String databaseUid = DATABASE_SERVANT.getDatabaseUid(objectClass);
        if (databaseUid == null) {
            return null;
        }

        return map(objectClass, object, databaseUid, language, specialistsBroker);
    }

    <O> Entity map(final Class<?> objectClass, final O object, final String databaseUid, final Language language, final SpecialistsBroker specialistsBroker) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }
}