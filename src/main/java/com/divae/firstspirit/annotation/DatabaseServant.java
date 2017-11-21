package com.divae.firstspirit.annotation;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;

import java.lang.reflect.AccessibleObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstances;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang.Validate.notNull;

public final class DatabaseServant {

    public String getDatabase(final AccessibleObject accessibleObject) {
        notNull(accessibleObject);

        final Database formFieldAnnotation = getDatabaseAnnotation(accessibleObject);
        return formFieldAnnotation != null ? formFieldAnnotation.value() : null;
    }

    public <T> String getDatabase(final Class<T> clazz) {
        notNull(clazz);

        final Database formFieldAnnotation = getDatabaseAnnotation(clazz);
        return formFieldAnnotation != null ? formFieldAnnotation.value() : null;
    }

    public String getDatabaseUid(final AccessibleObject accessibleObject) {
        notNull(accessibleObject);

        final Database formFieldAnnotation = getDatabaseAnnotation(accessibleObject);
        return formFieldAnnotation != null ? formFieldAnnotation.value() : null;
    }

    public <T> String getDatabaseUid(final Class<T> clazz) {
        notNull(clazz);

        final Database formFieldAnnotation = getDatabaseAnnotation(clazz);
        return formFieldAnnotation != null ? formFieldAnnotation.value() : null;
    }

    public <T> boolean hasDatabase(final Class<T> clazz) {
        notNull(clazz);

        return getDatabase(clazz) != null;
    }

    public <T> boolean hasDatabaseUid(final Class<T> clazz) {
        notNull(clazz);

        return getDatabaseUid(clazz) != null;
    }

    public <T> boolean hasDatabaseAnnotation(final Class<T> clazz) {
        notNull(clazz);

        return hasDatabase(clazz) && hasDatabaseUid(clazz);
    }

    public <T> Map<FormField, Object> getColumnValueMapping(final Class<?> objectClass, final T object) {
        final List<AnnotatedMember> annotatedMembers = concat(getInstances(objectClass.getDeclaredFields()).stream(), getInstances(objectClass.getDeclaredMethods()).stream()).collect(toList());

        final Map<com.divae.firstspirit.annotation.FormField, Object> columnValueMapping = new HashMap<>();
        annotatedMembers.parallelStream().filter(fromAnnotatedMember -> fromAnnotatedMember.getFormField() != null && fromAnnotatedMember.get(object) != null).forEach(fromAnnotatedMember -> columnValueMapping.put(fromAnnotatedMember.getFormField(), fromAnnotatedMember.get(object)));
        return columnValueMapping;
    }

    Database getDatabaseAnnotation(final AccessibleObject accessibleObject) {
        return accessibleObject.getAnnotation(Database.class);
    }

    <T> Database getDatabaseAnnotation(final Class<T> clazz) {
        return clazz.getAnnotation(Database.class);
    }
}
