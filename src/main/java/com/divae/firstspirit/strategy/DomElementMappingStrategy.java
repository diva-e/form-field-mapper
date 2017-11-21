package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.value.DomElement;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.forms.FormField;

import static org.apache.commons.lang.Validate.notNull;

public class DomElementMappingStrategy implements MappingStrategy {

	public boolean matches(final Class<?> fromType, final Class<?> toType) {
		notNull(fromType);
		notNull(toType);

		return DomElement.class.isAssignableFrom(fromType) && String.class.isAssignableFrom(toType)
				|| String.class.isAssignableFrom(fromType) && DomElement.class.isAssignableFrom(toType);
	}

	@SuppressWarnings("unchecked")
	@Override
    public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language, final SpecialistsBroker specialistsBroker) {
        notNull(from);
		notNull(fromObject);
		notNull(to);

		final FormField<DomElement> domElementFormField = (FormField<DomElement>) to;
		final DomElement domElement = domElementFormField.get();
		domElement.parseHtml((String) from.get(fromObject));
		domElementFormField.set(domElement);
	}

	@SuppressWarnings("unchecked")
	@Override
    public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final SpecialistsBroker specialistsBroker, final AnnotatedMember to, final O toObject) {
        notNull(from);
		notNull(to);
		notNull(toObject);

		to.set(toObject, ((FormField<DomElement>)from).get().toText(true));
	}
}
