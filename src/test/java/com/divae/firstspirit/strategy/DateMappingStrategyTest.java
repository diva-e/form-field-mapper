package com.divae.firstspirit.strategy;

import com.divae.firstspirit.annotation.FormField;
import com.divae.firstspirit.forms.FormFieldMock;
import org.junit.Test;

import java.util.Date;

import static com.divae.firstspirit.AnnotatedMemberModule.getInstance;
import static com.divae.firstspirit.Creator.build;
import static com.divae.firstspirit.access.LanguageMock.languageWith;
import static com.divae.firstspirit.access.store.templatestore.gom.GomFormElementMock.gomFormElementWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateMappingStrategyTest {

	private final DateMappingStrategy dateMappingStrategy = new DateMappingStrategy();

	@Test
	public void matches() throws Exception {
		assertThat(dateMappingStrategy.matches(Date.class, Date.class), is(true));
	}

	@Test
	public void mapFieldOFormField() throws Exception {
		TestClass test = new TestClass();
		Date currentDate = new Date();
		test.date = currentDate;

		de.espirit.firstspirit.forms.FormField<Date> formField = build(FormFieldMock.<Date>formFieldWith().aType(Date.class));
		dateMappingStrategy.map(getInstance(test.getClass().getField("date")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get(), is(currentDate));
	}

	@Test
	public void mapMethodOFormField() throws Exception {
		TestClass test = new TestClass();
		Date currentDate = new Date();
		test.setPrivateDate(currentDate);

		de.espirit.firstspirit.forms.FormField<Date> formField = build(FormFieldMock.<Date>formFieldWith().aType(Date.class));
		dateMappingStrategy.map(getInstance(test.getClass().getMethod("getPrivateDate")), test, formField, build(gomFormElementWith("test")), build(languageWith("DE")));

		assertThat(formField.get(), is(currentDate));
	}

	@Test
	public void mapFormFieldFieldO() throws Exception {
		TestClass test = new TestClass();
		Date currentDate = new Date();

		de.espirit.firstspirit.forms.FormField<Date> formField = build(FormFieldMock.<Date>formFieldWith().aValue(currentDate));
		dateMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getField("date")), test);

		assertThat(test.date, is(currentDate));
	}

	@Test
	public void mapFormFieldMethodO() throws Exception {
		TestClass test = new TestClass();
		Date currentDate = new Date();

		de.espirit.firstspirit.forms.FormField<Date> formField = build(FormFieldMock.<Date>formFieldWith().aValue(currentDate));
		dateMappingStrategy.map(formField, build(gomFormElementWith("test")), build(languageWith("DE")), getInstance(test.getClass().getMethod("setPrivateDate", Date.class)), test);

		assertThat(test.getPrivateDate(), is(currentDate));
	}

	public static final class TestClass {
		@FormField("tt_date")
		public Date date;

		private Date privateDate;

		@FormField("tt_date")
		public Date getPrivateDate() {
			return privateDate;
		}

		@FormField("tt_date")
		public void setPrivateDate(Date privateDate) {
			this.privateDate = privateDate;
		}
	}

}