package com.divae.firstspirit.formdata;

import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormData;
import de.espirit.firstspirit.forms.FormField;

import static org.apache.commons.lang.Validate.notNull;

public final class FormDataServant {

	public GomFormElement getGomFormElement(final FormData formData, final String fieldName) {
		notNull(formData);
		notNull(fieldName);

		return formData.getForm().findEditor(fieldName);
	}

	public boolean hasFormField(final FormData formData, final String fieldName) {
		return getGomFormElement(formData, fieldName) != null;
	}

	@SuppressWarnings("unchecked")
	public <T> FormField<T> getFormField(final FormData formData, final String fieldName, final Language language){
		notNull(formData);
		notNull(fieldName);

		if (!hasFormField(formData, fieldName)) {
			return null;
		}

		return (FormField<T>) formData.get(language, fieldName);
	}
}
