package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import com.divae.firstspirit.FormFieldMapper;
import com.divae.firstspirit.annotation.TemplateServant;
import com.divae.firstspirit.formdatalist.FormDataListServant;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormDataList;
import de.espirit.firstspirit.forms.FormField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import static de.espirit.common.base.Logging.logWarning;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang.Validate.notNull;

public class FormDataListInlineMappingStrategy implements MappingStrategy {
    private static final FormFieldMapper FORM_FIELD_MAPPER = new FormFieldMapper();
    private static final TemplateServant TEMPLATE_SERVANT = new TemplateServant();
    private static final FormDataListServant FORM_DATA_LIST_SERVANT = new FormDataListServant();

    public boolean matches(final Class<?> fromType, final Class<?> toType) {
        notNull(fromType);
        notNull(toType);

        return FormDataList.class.isAssignableFrom(fromType) && Collection.class.isAssignableFrom(toType)
                || Collection.class.isAssignableFrom(fromType) && FormDataList.class.isAssignableFrom(toType)
                || FormDataList.class.isAssignableFrom(fromType) && TEMPLATE_SERVANT.hasTemplateAnnotation(toType)
                || TEMPLATE_SERVANT.hasTemplateAnnotation(fromType) && FormDataList.class.isAssignableFrom(toType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> void map(final AnnotatedMember from, final O fromObject, final FormField<?> to, final GomFormElement toGomFormElement, final Language language) {
        notNull(from);
        notNull(fromObject);
        notNull(to);

        final FormField<FormDataList> formField = (FormField<FormDataList>) to;
        final FormDataList formDataList = formField.get();
        formDataList.clear();

        final Collection<Object> objects = createCollection(from.get(fromObject));

        map(objects, formDataList, language);

        formField.set(formDataList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> void map(final FormField<?> from, final GomFormElement fromGomFormElement, final Language language, final AnnotatedMember to, final O toObject) {
        notNull(from);
        notNull(to);
        notNull(toObject);

        final FormDataList formDataList = ((FormField<FormDataList>) from).get();
        if (formDataList.isEmpty()) {
            return;
        }

        final Object object = map(formDataList, language, to);
        if (object == null) {
            return;
        }

        to.set(toObject, object);
    }

    <O> void map(final Collection<O> objects, final FormDataList formDataList, final Language language) {
        for (final O object : objects) {
            final String templateUid = TEMPLATE_SERVANT.getTemplateValue(object.getClass());
            if (templateUid == null) {
                continue;
            }

            final IdProvidingFormData idProvidingFormData = FORM_DATA_LIST_SERVANT.createInlineIdProvidingFormData(formDataList, templateUid);
            if (idProvidingFormData == null) {
                logWarning("Could not create a new form data list entry for [" + templateUid + "]", getClass());
                return;
            }

            FORM_FIELD_MAPPER.map(object, idProvidingFormData, language);
            formDataList.add(idProvidingFormData);
        }
    }

    <O> O map(final IdProvidingFormData idProvidingFormData, final Language language, final Constructor<O> constructor) {
        final O object;
        try {
            object = constructor.newInstance();
        } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logWarning("Could not create object", e, getClass());
            return null;
        }

        FORM_FIELD_MAPPER.map(idProvidingFormData, language, object);
        return object;
    }

    @SuppressWarnings("unchecked")
    Object map(final FormDataList formDataList, final Language language, final AnnotatedMember annotatedMember) {
        final Class<?> parameterClass = annotatedMember.getSetType();
        final boolean isCollection = Collection.class.isAssignableFrom(parameterClass);
        final Constructor<?> declaredConstructor = annotatedMember.getSetDeclaredConstructor();

        return !isCollection ? map(formDataList.get(0), language, declaredConstructor) : map(formDataList, language, declaredConstructor);
    }

    <O> Collection<O> map(final FormDataList formDataList, final Language language, final Constructor<O> constructor) {
        final Collection<O> objects = new ArrayList<>();

        for (final IdProvidingFormData idProvidingFormData : formDataList) {
            final O object = map(idProvidingFormData, language, constructor);
            if (object == null) {
                continue;
            }
            FORM_FIELD_MAPPER.map(idProvidingFormData, language, object);
            objects.add(object);
        }

        return objects;
    }

    @SuppressWarnings("unchecked")
    private <O> Collection<O> createCollection(final O object) {
        return object instanceof Collection ? (Collection<O>) object : singletonList(object);
    }
}
