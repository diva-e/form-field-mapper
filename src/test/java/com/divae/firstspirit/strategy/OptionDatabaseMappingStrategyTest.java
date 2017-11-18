package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.Database;
import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import de.espirit.firstspirit.access.editor.value.Option;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import org.junit.Ignore;
import org.junit.Test;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.BuilderMock.build;
import static com.divae.firstspirit.Proxy.proxy;
import static com.divae.firstspirit.Proxy.with;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.editor.value.OptionFactoryMock.optionFactoryWith;
import static com.divae.firstspirit.access.editor.value.OptionFactoryProviderMock.optionFactoryProviderWith;
import static com.divae.firstspirit.access.editor.value.OptionMock.optionWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static com.divae.firstspirit.agency.SpecialistsBrokerMock.specialistsBrokerWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Ignore("Not implemented yet.")
public class OptionDatabaseMappingStrategyTest {

    private final OptionMappingStrategy optionMappingStrategy = new OptionMappingStrategy();

    @Test
    public void matches() throws Exception {
        assertThat(optionMappingStrategy.matches(Option.class, String.class), is(true));
        assertThat(optionMappingStrategy.matches(String.class, Option.class), is(true));
    }

    @Test
    public void mapFieldOFormField() throws Exception {
        TestClass test = new TestClass();
        //test.option = "option";

        OptionFactoryProvider object = build(optionFactoryProviderWith().anOptionFactory(optionFactoryWith()
                .create(optionWith().aValue("option"), "option")));
        GomFormElement proxy = proxy(with(object, OptionFactoryProvider.class).aTarget(GomFormElement.class));

        de.espirit.firstspirit.forms.FormField<Option> formField = build(FormFieldMock.<Option>formFieldWith().aType(Option.class));
        optionMappingStrategy.map(getInstance(test.getClass().getField("option")), test, formField, proxy, build(languageWith("DE")), build(specialistsBrokerWith()));

        assertThat(formField.get().getValue(), is("option"));
    }

    @Test
    public void mapMethodOFormField() throws Exception {
        TestClass test = new TestClass();
        //test.setPrivateOption("option");

        GomFormElement proxy = proxy(with(build(optionFactoryProviderWith().anOptionFactory(optionFactoryWith()
                .create(optionWith().aValue("option"), "option"))), OptionFactoryProvider.class).aTarget(GomFormElement.class));

        de.espirit.firstspirit.forms.FormField<Option> formField = build(FormFieldMock.<Option>formFieldWith().aType(Option.class));
        optionMappingStrategy.map(getInstance(test.getClass().getMethod("getPrivateOption")), test, formField, proxy, build(languageWith("DE")), build(specialistsBrokerWith()));

        assertThat(formField.get().getValue(), is("option"));
    }

    @Test
    public void mapFormFieldFieldO() throws Exception {
        TestClass test = new TestClass();

        de.espirit.firstspirit.forms.FormField<Option> formField = build(FormFieldMock.<Option>formFieldWith().aValue(build(optionWith().aValue("option"))));
        optionMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), build(specialistsBrokerWith()), getInstance(test.getClass().getField("option")), test);

        assertThat(test.option, is("option"));
    }


    @Test
    public void mapFormFieldMethodO() throws Exception {
        TestClass test = new TestClass();

        de.espirit.firstspirit.forms.FormField<Option> formField = build(FormFieldMock.<Option>formFieldWith().aValue(build(optionWith().aValue("option"))));
        optionMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), build(specialistsBrokerWith()), getInstance(test.getClass().getMethod("setPrivateOption", String.class)), test);

        assertThat(test.getPrivateOption(), is("option"));
    }

    public static final class TestClass {
        @FormField("tt_option")
        public SecondTestClass option;

        private SecondTestClass privateOption;

        @FormField("tt_option")
        public SecondTestClass getPrivateOption() {
            return privateOption;
        }

        @FormField("tt_option")
        public void setPrivateOption(SecondTestClass privateOption) {
            this.privateOption = privateOption;
        }
    }

    @Database("database")
    public static final class SecondTestClass {
        @FormField(value = "fs_id", isEntityName = true)
        public Long fsId;
    }
}