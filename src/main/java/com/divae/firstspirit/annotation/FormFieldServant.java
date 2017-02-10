package com.divae.firstspirit.annotation;

import com.divae.firstspirit.formdata.FormDataServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.forms.FormData;
import de.espirit.firstspirit.forms.FormField;

import java.lang.reflect.AnnotatedElement;

import static org.apache.commons.lang.Validate.notNull;

public final class FormFieldServant {

	private static final FormDataServant FORM_DATA_SERVANT = new FormDataServant();

	public <T> FormField<T> getFormField(final String formField, final FormData formData, final Language language){
		notNull(formField);
		notNull(formData);

		return FORM_DATA_SERVANT.getFormField(formData, formField, language);
	}

	public String getFormFieldValue(final AnnotatedElement annotatedElement) {
		notNull(annotatedElement);

		final com.divae.firstspirit.annotation.FormField formFieldAnnotation = getFormFieldAnnotation(annotatedElement);
		return formFieldAnnotation != null ? formFieldAnnotation.value() : null;
	}

	com.divae.firstspirit.annotation.FormField getFormFieldAnnotation(final AnnotatedElement annotatedElement){
		return annotatedElement.getAnnotation(com.divae.firstspirit.annotation.FormField.class);
	}
}
