package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.option.OptionServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.access.editor.value.OptionFactory;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.forms.FormField;
import de.espirit.or.schema.Entity;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang.Validate.allElementsOfType;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

public class OptionsMappingStrategy implements MappingStrategy {

	private final OptionServant optionServant = new OptionServant();

	public boolean matches(final Class<?> fromType, final Class<?> toType) {
		notNull(fromType);
		notNull(toType);

		return Set.class.isAssignableFrom(fromType) && Collection.class.isAssignableFrom(toType)
				|| Collection.class.isAssignableFrom(fromType) && Set.class.isAssignableFrom(toType);
	}

	@SuppressWarnings("unchecked")
	@Override
    public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language, final SpecialistsBroker specialistsBroker) {
        notNull(from);
		notNull(fromObject);
		notNull(to);
		notNull(toGomFormElement);

        final FormField<Set<?>> formField = (FormField<Set<?>>) to;
        final Set<?> set = formField.get();
        allElementsOfType(set, Option.class);

        final Set<Option> options = (Set<Option>) set;
        options.clear();

        final Collection<Object> objects = (Collection) from.get(fromObject);
        if (!objects.isEmpty()) {
            final Object first = objects.iterator().next();
            isTrue(first instanceof String || first instanceof Entity);
        }

		final OptionFactory optionFactory = ((OptionFactoryProvider) toGomFormElement).getOptionFactory();
        options.addAll(objects.stream().map(optionFactory::create).collect(toSet()));

		formField.set(options);
	}

	@SuppressWarnings("unchecked")
	@Override
    public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final SpecialistsBroker specialistsBroker, final AnnotatedMember to, final O toObject) {
        notNull(from);
		notNull(to);
		notNull(toObject);

        final FormField<Set<?>> formField = (FormField<Set<?>>) from;
        final Set<?> set = formField.get();
        allElementsOfType(set, Option.class);

        final Set<Option> options = (Set<Option>) set;
        final Collection<Object> values = optionServant.getValues(options);
        if (!values.isEmpty()) {
            final Object first = values.iterator().next();
            isTrue(first instanceof String || first instanceof Entity);
        }

        to.set(toObject, values);
    }
}
