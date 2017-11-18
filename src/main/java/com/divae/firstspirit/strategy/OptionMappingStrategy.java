package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.access.editor.value.OptionFactory;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormField;
import de.espirit.or.schema.Entity;

import static org.apache.commons.lang.Validate.notNull;

public class OptionMappingStrategy implements MappingStrategy {

	public boolean matches(final Class<?> fromType, final Class<?> toType) {
		notNull(fromType);
		notNull(toType);

		return Option.class.isAssignableFrom(fromType) && String.class.isAssignableFrom(toType)
                || String.class.isAssignableFrom(fromType) && Option.class.isAssignableFrom(toType)
                || Option.class.isAssignableFrom(fromType) && Entity.class.isAssignableFrom(toType)
                || Entity.class.isAssignableFrom(fromType) && Option.class.isAssignableFrom(toType);
    }

	@SuppressWarnings("unchecked")
	@Override
	public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language) {
		notNull(from);
		notNull(fromObject);
		notNull(to);
		notNull(toGomFormElement);

		OptionFactory optionFactory = ((OptionFactoryProvider) toGomFormElement).getOptionFactory();
		to.set(optionFactory.create(from.get(fromObject)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final AnnotatedMember to, final O toObject) {
		notNull(from);
		notNull(to);
		notNull(toObject);

		to.set(toObject, ((FormField<Option>) from).get().getValue());
	}
}
