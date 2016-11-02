package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.Creator.build;
import static com.divae.firstspirit.Proxy.proxy;
import static com.divae.firstspirit.Proxy.with;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.editor.value.OptionFactoryMock.optionFactoryWith;
import static com.divae.firstspirit.access.editor.value.OptionFactoryProviderMock.optionFactoryProviderWith;
import static com.divae.firstspirit.access.editor.value.OptionMock.optionWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OptionsMappingStrategyTest {

	private final OptionsMappingStrategy optionsMappingStrategy = new OptionsMappingStrategy();

	@Test
	public void matches() throws Exception {
		assertThat(optionsMappingStrategy.matches(Set.class, Set.class), is(true));
	}

	@Test
	public void mapFieldOFormField() throws Exception {
		TestClass test = new TestClass();
		Set<String> options = new HashSet<>();
		options.add("option");
		options.add("option2");
		test.options = options;

		OptionFactoryProvider object = build(optionFactoryProviderWith().anOptionFactory(build(optionFactoryWith()
				.create("option", build(optionWith().aValue("option")))
				.create("option2", build(optionWith().aValue("option2"))))));
		GomFormElement proxy = proxy(with(object, OptionFactoryProvider.class).aTarget(GomFormElement.class));

		de.espirit.firstspirit.forms.FormField<Set<Option>> formField = build(FormFieldMock.<Set<Option>>formFieldWith().aValue(new HashSet<Option>()));
		optionsMappingStrategy.map(getInstance(test.getClass().getField("options")), test, formField, proxy, build(languageWith("DE")));

		Set<Option> createdOptions = formField.get();
		assertThat(createdOptions.size(), is(2));
		Iterator<Option> iterator = createdOptions.iterator();
		assertThat(options.contains((String)iterator.next().getValue()), is(true));
		assertThat(options.contains((String)iterator.next().getValue()), is(true));
	}

	@Test
	public void mapMethodOFormField() throws Exception {
		TestClass test = new TestClass();
		Set<String> options = new HashSet<>();
		options.add("option");
		options.add("option2");
		test.setPrivateOptions(options);

		GomFormElement proxy = proxy(with(build(optionFactoryProviderWith().anOptionFactory(build(optionFactoryWith()
				.create("option", build(optionWith().aValue("option")))
				.create("option2", build(optionWith().aValue("option2")))))), OptionFactoryProvider.class).aTarget(GomFormElement.class));

		de.espirit.firstspirit.forms.FormField<Set<Option>> formField = build(FormFieldMock.<Set<Option>>formFieldWith().aValue(new HashSet<Option>()));

		optionsMappingStrategy.map(getInstance(test.getClass().getMethod("getPrivateOptions")), test, formField, proxy, build(languageWith("DE")));

		Set<Option> createdOptions = formField.get();
		assertThat(createdOptions.size(), is(2));
		Iterator<Option> iterator = createdOptions.iterator();
		assertThat(options.contains((String)iterator.next().getValue()), is(true));
		assertThat(options.contains((String)iterator.next().getValue()), is(true));
	}

	@Test
	public void mapFormFieldFieldO() throws Exception {
		TestClass test = new TestClass();

		Set<Option> options = new HashSet<>();
		options.add(build(optionWith().aValue("option")));
		options.add(build(optionWith().aValue("option2")));

		de.espirit.firstspirit.forms.FormField<Set<Option>> formField = build(FormFieldMock.<Set<Option>>formFieldWith().aValue(options));
		optionsMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getField("options")), test);

		assertThat(test.options.size(), is(2));
		assertThat(test.options.contains("option"), is(true));
		assertThat(test.options.contains("option2"), is(true));
	}

	@Test
	public void mapFormFieldMethodO() throws Exception {
		TestClass test = new TestClass();

		Set<Option> options = new HashSet<>();
		options.add(build(optionWith().aValue("option")));
		options.add(build(optionWith().aValue("option2")));

		de.espirit.firstspirit.forms.FormField<Set<Option>> formField = build(FormFieldMock.<Set<Option>>formFieldWith().aValue(options));
		optionsMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getMethod("setPrivateOptions", Set.class)), test);

		Set<String> privateOptions = test.getPrivateOptions();
		assertThat(privateOptions.size(), is(2));
		assertThat(privateOptions.contains("option"), is(true));
		assertThat(privateOptions.contains("option2"), is(true));
	}

	public static final class TestClass {
		@FormField("tt_options")
		public Set<String> options;

		private Set<String> privateOptions;

		@FormField("tt_options")
		public Set<String> getPrivateOptions() {
			return privateOptions;
		}

		@FormField("tt_options")
		public void setPrivateOptions(Set<String> privateOptions) {
			this.privateOptions = privateOptions;
		}
	}
}