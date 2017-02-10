package com.divae.firstspirit.formdatalist;

import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.editor.value.SectionFormsProducer;
import de.espirit.firstspirit.access.store.templatestore.SectionTemplate;
import de.espirit.firstspirit.forms.FormDataList;

import java.util.Collection;
import java.util.Optional;

import static de.espirit.common.base.Logging.logWarning;
import static org.apache.commons.lang.Validate.notNull;

public final class FormDataListServant {

	public IdProvidingFormData createIdProvidingFormData(final FormDataList formDataList, final String templateUid) {
		notNull(formDataList);
		notNull(templateUid);

		final SectionFormsProducer sectionFormsProducer = (SectionFormsProducer) formDataList.getProducer();

		final Optional<SectionTemplate> sectionTemplate = getSectionTemplate(sectionFormsProducer.getAllowedTemplates(), templateUid);
		if (!sectionTemplate.isPresent()) {
			logWarning("Could not get section template with uid [" + templateUid + "]", getClass());
			return null;
		}

		return sectionFormsProducer.create(sectionTemplate.get());
	}

	Optional<SectionTemplate> getSectionTemplate(final Collection<SectionTemplate> sectionTemplates, final String uid) {
		return sectionTemplates.stream().filter(sectionTemplate -> sectionTemplate.getUid().equals(uid)).findFirst();
	}
}
