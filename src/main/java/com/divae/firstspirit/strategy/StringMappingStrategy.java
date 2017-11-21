package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.forms.FormField;

import static org.apache.commons.lang.Validate.notNull;

public class StringMappingStrategy implements MappingStrategy {

	@Override
	public boolean matches(final Class<?> fromType, final Class<?> toType) {
		notNull(fromType);
		notNull(toType);

		return String.class.isAssignableFrom(fromType) && String.class.isAssignableFrom(toType);
	}

	@Override
    public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language, final SpecialistsBroker specialistsBroker) {
        notNull(from);
		notNull(fromObject);
		notNull(to);

		to.set(from.get(fromObject));
	}

	@Override
    public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final SpecialistsBroker specialistsBroker, final AnnotatedMember to, final O toObject) {
        notNull(from);
		notNull(to);
		notNull(toObject);

		to.set(toObject, from.get());
	}
}
