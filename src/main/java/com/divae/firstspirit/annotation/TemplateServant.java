package com.divae.firstspirit.annotation;

import java.lang.reflect.AccessibleObject;

import static org.apache.commons.lang.Validate.notNull;

public final class TemplateServant {

	public String getTemplateValue(final AccessibleObject accessibleObject){
		notNull(accessibleObject);

		final Template formFieldAnnotation = getTemplateAnnotation(accessibleObject);
		return formFieldAnnotation != null ? formFieldAnnotation.value() : null;
	}

	public <T> String getTemplateValue(final Class<T> clazz){
		notNull(clazz);

		final Template formFieldAnnotation = getTemplateAnnotation(clazz);
		return formFieldAnnotation != null ? formFieldAnnotation.value() : null;
	}

	public <T> boolean hasTemplateAnnotation(final Class<T> clazz){
		notNull(clazz);

		return getTemplateValue(clazz) != null;
	}

	Template getTemplateAnnotation(final AccessibleObject accessibleObject){
		return accessibleObject.getAnnotation(Template.class);
	}


	<T> Template getTemplateAnnotation(final Class<T> clazz){
		return clazz.getAnnotation(Template.class);
	}
}
