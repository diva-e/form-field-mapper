package com.divae.firstspirit;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.formdata.FormDataServant;
import com.divae.firstspirit.strategy.BooleanMappingStrategy;
import com.divae.firstspirit.strategy.DateMappingStrategy;
import com.divae.firstspirit.strategy.DomElementMappingStrategy;
import com.divae.firstspirit.strategy.FormDataListDatabaseMappingStrategy;
import com.divae.firstspirit.strategy.FormDataListInlineMappingStrategy;
import com.divae.firstspirit.strategy.MappingStrategy;
import com.divae.firstspirit.strategy.MappingStrategyServant;
import com.divae.firstspirit.strategy.NumberMappingStrategy;
import com.divae.firstspirit.strategy.OptionMappingStrategy;
import com.divae.firstspirit.strategy.OptionsMappingStrategy;
import com.divae.firstspirit.strategy.StringMappingStrategy;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormData;
import de.espirit.firstspirit.forms.FormField;

import java.util.Collection;
import java.util.List;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstances;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.Validate.notNull;

public class FormFieldMapper {

	private final List<MappingStrategy> mappingStrategies = asList(new BooleanMappingStrategy(),
			new DateMappingStrategy(), new DomElementMappingStrategy(), new NumberMappingStrategy(),
			new StringMappingStrategy(), new OptionsMappingStrategy(), new OptionMappingStrategy(),
            new FormDataListInlineMappingStrategy(), new FormDataListDatabaseMappingStrategy());

	private static final MappingStrategyServant MAPPING_STRATEGY_SERVANT = new MappingStrategyServant();
	private static final FormDataServant FORM_DATA_SERVANT = new FormDataServant();

	public <T> void map(final T from, final FormData to, final Language language) {
		notNull(from);
		notNull(to);

		final Class<?> fromClass = from.getClass();
		map(getInstances(fromClass.getDeclaredFields()), from, to, language);
		map(getInstances(fromClass.getDeclaredMethods()), from, to, language);
	}

	<T> void map(final Collection<AnnotatedMember> fromAnnotatedMembers, final T from, final FormData to, final Language language) {
		for (final MappingStrategy mappingStrategy : mappingStrategies) {
			for (final AnnotatedMember fromAnnotatedMember : fromAnnotatedMembers) {
				if (!MAPPING_STRATEGY_SERVANT.matches(fromAnnotatedMember, to, language, mappingStrategy)) {
					continue;
				}
				final String fieldName = fromAnnotatedMember.getFormFieldValue();
				final FormField<?> toFormField = to.get(language, fieldName);
				final GomFormElement gomFormElement = FORM_DATA_SERVANT.getGomFormElement(to, fieldName);
				mappingStrategy.map(fromAnnotatedMember, from, toFormField, gomFormElement, language);
			}
		}
	}

	public <T> void map(final FormData from, final Language language, final T to) {
		notNull(from);
		notNull(to);

		final Class<?> toClass = to.getClass();
		map(from, language, getInstances(toClass.getDeclaredFields()), to);
		map(from, language, getInstances(toClass.getDeclaredMethods()), to);
	}

	public void addStrategy(MappingStrategy mappingStrategy){
		notNull(mappingStrategy);

		mappingStrategies.add(mappingStrategy);
	}

	<T> void map(final FormData from, final Language language, final  Collection<AnnotatedMember> toAnnotatedMembers, final T to) {
		for (final MappingStrategy mappingStrategy : mappingStrategies) {
			for (final AnnotatedMember toAnnotatedMember : toAnnotatedMembers) {
				if (!MAPPING_STRATEGY_SERVANT.matches(from, language, toAnnotatedMember, mappingStrategy)) {
					continue;
				}
				final String fieldName = toAnnotatedMember.getFormFieldValue();
				final FormField<?> fromFormField = from.get(language, fieldName);
				final GomFormElement gomFormElement = FORM_DATA_SERVANT.getGomFormElement(from, fieldName);
				mappingStrategy.map(fromFormField, gomFormElement, language, toAnnotatedMember, to);
			}
		}
	}
}
