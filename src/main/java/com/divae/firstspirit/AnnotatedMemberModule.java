package com.divae.firstspirit;

import com.divae.firstspirit.annotation.FormFieldServant;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.espirit.common.base.Logging.logWarning;
import static org.apache.commons.lang.Validate.noNullElements;
import static org.apache.commons.lang.Validate.notNull;

public class AnnotatedMemberModule {

	private static final FormFieldServant FORM_FIELD_SERVANT = new FormFieldServant();

	public static AnnotatedMember getInstance(final Method method){
		notNull(method);

		return new AnnotatedMember<Method>(method) {

			@Override
			public void set(Object instance, Object value) {
				notNull(instance);

				try {
					method.invoke(instance, value);
				} catch (final IllegalAccessException | InvocationTargetException e) {
					logWarning("Could not set data to [" + method.getName() + "]", e, getClass());
				}
			}

			@Override
			public Object get(Object instance) {
				notNull(instance);

				try {
					return method.invoke(instance);
				} catch (final IllegalAccessException | InvocationTargetException e) {
					logWarning("Could not get data to [" + method.getName() + "]", e, getClass());
				}
				return null;
			}

			@Override
			public Class<?> getGetType() {
				return method.getReturnType();
			}

			@Override
			public Class<?> getSetType() {
				final Class<?>[] parameterTypes = method.getParameterTypes();
				return parameterTypes.length != 1 ? null : parameterTypes[0];
			}

			@Override
			public Constructor<?> getGetDeclaredConstructor() {
				final Class<?> parameterClass = getGetType();
				return parameterClass == null ? null : getDeclaredConstructor(parameterClass);
			}

			@Override
			public Constructor<?> getSetDeclaredConstructor() {
				final Class<?> parameterClass = getSetType();
				return parameterClass == null ? null : getDeclaredConstructor(parameterClass);
			}

			@SuppressWarnings("unchecked")
			Constructor<?> getDeclaredConstructor(final Class<?> parameterClass) {
				final boolean isCollection = Collection.class.isAssignableFrom(parameterClass);

				try {
					return !isCollection ? parameterClass.getDeclaredConstructor() : ((Class)((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments()[0]).getDeclaredConstructor();
				} catch (final NoSuchMethodException e) {
					logWarning("Could get default constructor", e, getClass());
					return null;
				}
			}

		};
	}

	public static Collection<AnnotatedMember> getInstances(final Method[] methods){
		noNullElements(methods);

		List<AnnotatedMember> instances = new ArrayList<>();
		for(Method method : methods) {
			instances.add(getInstance(method));
		}
		return instances;
	}

	public static Collection<AnnotatedMember> getInstances(final Field[] fields){
		noNullElements(fields);

		List<AnnotatedMember> instances = new ArrayList<>();
		for(Field field : fields) {
			instances.add(getInstance(field));
		}
		return instances;
	}

	public static AnnotatedMember<Field> getInstance(final Field field){
		notNull(field);

		return new AnnotatedMember<Field>(field) {

			@Override
			public void set(Object instance, Object value) {
				notNull(instance);

				try {
					field.set(instance, value);
				} catch (final IllegalAccessException e) {
					logWarning("Could not set data to [" + field.getName() + "]", e, getClass());
				}
			}

			@Override
			public Object get(Object instance) {
				notNull(instance);

				try {
					return field.get(instance);
				} catch (final IllegalAccessException e) {
					logWarning("Could not get data to [" + field.getName() + "]", e, getClass());
				}
				return null;
			}

			@Override
			public Class<?> getGetType() {
				return getType();
			}

			@Override
			public Class<?> getSetType() {
				return getType();
			}

			public Class<?> getType() {
				return field.getType();
			}

			@Override
			public Constructor<?> getGetDeclaredConstructor() {
				return getDeclaredConstructor();
			}

			@Override
			public Constructor<?> getSetDeclaredConstructor() {
				return getDeclaredConstructor();
			}

			@SuppressWarnings("unchecked")
			Constructor<?> getDeclaredConstructor() {
				final Class<?> fieldClass = getType();
				final boolean isCollection = Collection.class.isAssignableFrom(fieldClass);

				try {
					return !isCollection ? fieldClass.getDeclaredConstructor() : ((Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]).getDeclaredConstructor();
				} catch (final NoSuchMethodException e) {
					logWarning("Could get default constructor", e, getClass());
					return null;
				}
			}
		};
	}

	public static abstract class AnnotatedMember<AM extends AnnotatedElement & Member> {

		private final AM annotatedMember;

		private AnnotatedMember(AM annotatedMember) {
			this.annotatedMember = annotatedMember;
		}

		public abstract void set(Object instance, Object value);
		public abstract Object get(Object instance);
		public abstract Class<?> getGetType();
		public abstract Class<?> getSetType();
		public abstract Constructor<?> getGetDeclaredConstructor();
		public abstract Constructor<?> getSetDeclaredConstructor();

		public String getFormFieldValue(){
			return FORM_FIELD_SERVANT.getFormFieldValue(annotatedMember);
		}
	}
}
