package com.divae.firstspirit.annotation;

import java.lang.reflect.AccessibleObject;

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

    Database getDatabaseAnnotation(final AccessibleObject accessibleObject) {
        return accessibleObject.getAnnotation(Database.class);
    }

    <T> Database getDatabaseAnnotation(final Class<T> clazz) {
        return clazz.getAnnotation(Database.class);
    }
}
