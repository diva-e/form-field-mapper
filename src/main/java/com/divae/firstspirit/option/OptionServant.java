package com.divae.firstspirit.option;

import de.espirit.firstspirit.access.editor.value.Option;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OptionServant {

	public Collection<String> getValues(final Set<Option> options) {
		final Collection<String> values = new HashSet<>();
		for (final Option option : options) {
			values.add((String) option.getValue());
		}
		return values;
	}
}
