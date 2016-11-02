package com.divae.firstspirit.formdatalist;

import de.espirit.firstspirit.access.editor.fslist.IdProvidingFormData;
import de.espirit.firstspirit.access.editor.value.SectionFormsProducer;
import de.espirit.firstspirit.access.store.templatestore.SectionTemplate;
import de.espirit.firstspirit.forms.FormDataList;

import java.util.Collection;

import static de.espirit.common.base.Logging.logWarning;
import static org.apache.commons.lang.Validate.notNull;

public final class FormDataListServant {

	public IdProvidingFormData createIdProvidingFormData(final FormDataList formDataList, final String templateUid) {
		notNull(formDataList);
		notNull(templateUid);

		final SectionFormsProducer sectionFormsProducer = (SectionFormsProducer) formDataList.getProducer();

		final SectionTemplate sectionTemplate = getSectionTemplate(sectionFormsProducer.getAllowedTemplates(), templateUid);
		if (sectionTemplate == null) {
			logWarning("Could not get section template with uid [" + templateUid + "]", getClass());
			return null;
		}

		return sectionFormsProducer.create(sectionTemplate);
	}

	SectionTemplate getSectionTemplate(final Collection<SectionTemplate> sectionTemplates, final String uid){
		for (final SectionTemplate sectionTemplate : sectionTemplates) {
			if (sectionTemplate.getUid().equals(uid)){
				return sectionTemplate;
			}
		}
		return null;
	}
}
