package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.annotation.FormFieldServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.forms.FormData;
import de.espirit.firstspirit.forms.FormField;

import static org.apache.commons.lang.Validate.notNull;

public final class MappingStrategyServant {

	private static final FormFieldServant FORM_FIELD_SERVANT = new FormFieldServant();

	public boolean matches(final AnnotatedMember from, final FormData to, final Language language, final MappingStrategy mappingStrategy) {
		notNull(from);
		notNull(to);
		notNull(language);
		notNull(mappingStrategy);

        final com.divae.firstspirit.annotation.FormField formField = from.getFormField();
        if (formField == null) {
            return false;
		}

		Class<?> fromGetType = from.getGetType();
		if (fromGetType == null) {
			return false;
		}

        final FormField<?> toFormField = FORM_FIELD_SERVANT.getFormField(formField.value(), to, language);
        return toFormField != null && mappingStrategy.matches(fromGetType, toFormField.getType());

	}

	public boolean matches(final FormData from, final Language language, final AnnotatedMember to, final MappingStrategy mappingStrategy) {
		notNull(from);
		notNull(language);
		notNull(to);
		notNull(mappingStrategy);

        final com.divae.firstspirit.annotation.FormField formField = to.getFormField();
        if (formField == null) {
            return false;
		}

		Class<?> toSetType = to.getSetType();
		if (toSetType == null) {
			return false;
		}

        final FormField<?> fromFormField = FORM_FIELD_SERVANT.getFormField(formField.value(), from, language);
        return fromFormField != null && mappingStrategy.matches(fromFormField.getType(), toSetType);

	}
}
