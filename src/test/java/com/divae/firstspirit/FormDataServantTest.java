package com.divae.firstspirit;

import com.divae.firstspirit.access.LanguageMock.LanguageBuilder;
import com.divae.firstspirit.formdata.FormDataServant;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.forms.FormData;
import org.junit.Test;

import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomEditorProviderMock.gomEditorProviderWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.forms.FormDataMock.formDataWith;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FormDataServantTest {

	private final FormDataServant formDataServant = new FormDataServant();

	@Test
	public void getGomFormElement() throws Exception {
		FormData formData = build(formDataWith().aForm(() ->
				gomEditorProviderWith("test").values(() ->
						singletonList(gomFormElementWith("test"))))
		);
		assertThat(formDataServant.getGomFormElement(formData, "test").name(), is("test"));
	}

	@Test
	public void hasFormField() throws Exception {
		FormData formData = build(formDataWith().aForm(() ->
				gomEditorProviderWith("test").values(() ->
						singletonList(gomFormElementWith("test"))))
		);
		assertThat(formDataServant.hasFormField(formData, "test"), is(true));

	}

	@Test
	public void getFormField() throws Exception {
		LanguageBuilder languageBuilder = languageWith("DE");
		FormData formData = build(formDataWith().aForm(() ->
				gomEditorProviderWith("test").values(() ->
						singletonList(gomFormElementWith("test"))))
				.aValue(FormFieldMock::formFieldWith, languageBuilder, "test")
		);
		assertThat(formDataServant.<String>getFormField(formData, "test", build(languageBuilder)), is(notNullValue()));
	}

}