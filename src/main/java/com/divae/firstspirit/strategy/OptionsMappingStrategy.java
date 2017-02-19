package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.option.OptionServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.access.editor.value.OptionFactory;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormField;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
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
	public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language) {
		notNull(from);
		notNull(fromObject);
		notNull(to);
		notNull(toGomFormElement);

		final FormField<Set<Option>> formField = (FormField<Set<Option>>) to;
		final Set<Option> options = formField.get();
		options.clear();

		final OptionFactory optionFactory = ((OptionFactoryProvider) toGomFormElement).getOptionFactory();
		options.addAll(((Collection<String>) from.get(fromObject)).stream().map(optionFactory::create).collect(toSet()));

		formField.set(options);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final AnnotatedMember to, final O toObject) {
		notNull(from);
		notNull(to);
		notNull(toObject);

		to.set(toObject, optionServant.getValues(((FormField<Set<Option>>) from).get()));
	}
}
