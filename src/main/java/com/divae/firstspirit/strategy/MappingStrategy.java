package com.divae.firstspirit.strategy;

import com.divae.firstspirit.AnnotatedMemberModule.AnnotatedMember;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.forms.FormField;

public interface MappingStrategy {

	boolean matches(Class<?> fromType, Class<?> toType);

    <O> void map(AnnotatedMember from, O fromObject, FormField<?> to, GomFormElement toGomFormElement, Language language, SpecialistsBroker specialistsBroker);

    <O> void map(FormField<?> from, GomFormElement fromGomFormElement, Language language, SpecialistsBroker specialistsBroker, AnnotatedMember to, O toObject);
}
