package com.divae.firstspirit.option;

import de.espirit.firstspirit.access.editor.value.Option;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class OptionServant {

	public Collection<String> getValues(final Set<Option> options) {
		return options.stream().map(option -> (String) option.getValue()).collect(Collectors.toSet());
	}
}
