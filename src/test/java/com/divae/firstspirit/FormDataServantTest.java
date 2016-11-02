package com.divae.firstspirit;

import com.divae.firstspirit.formdata.FormDataServant;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.forms.FormData;
import de.espirit.firstspirit.forms.FormField;
import org.junit.Test;

import static com.divae.firstspirit.Creator.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.forms.FormDataMock.formDataWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FormDataServantTest {

	private final FormDataServant formDataServant = new FormDataServant();

	@Test
	public void getGomFormElement() throws Exception {
		GomFormElement gomFormElement = build(gomFormElementWith("test"));
		FormData formData = build(formDataWith().aForm(build(gomEditorProviderWith("test").values(gomFormElement))));
		assertThat(formDataServant.getGomFormElement(formData, "test"), is(gomFormElement));
	}

	@Test
	public void hasFormField() throws Exception {
		FormData formData = build(formDataWith().aForm(build(gomEditorProviderWith("test").values(build(gomFormElementWith("test"))))));
		assertThat(formDataServant.hasFormField(formData, "test"), is(true));

	}

	@Test
	public void getFormField() throws Exception {
		Language language = build(languageWith("DE"));
		FormField<String> formField = build(FormFieldMock.<String>formFieldWith());
		FormData formData = build(formDataWith().aForm(build(gomEditorProviderWith("test").values(build(gomFormElementWith("test"))))).aValue(language, "test", formField));
		assertThat(formDataServant.<String>getFormField(formData, "test", language), is(formField));
	}

}