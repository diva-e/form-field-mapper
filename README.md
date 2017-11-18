## Description
This is a FirstSpirit form field mapper that transfer data from a java class via annotations to a form field and vise versa.


## Features
* Map different kind of java types from and to java class via annotations
    * String <-> String
    * Date <-> Date
    * Boolean <-> Boolean
    * Number<-> Number
    * String (HTML) <-> DomElement
    * Collection<Object> <-> FormDataList (inline, database)
    * String <-> Option
    * Entity (native, database) <-> Option
    * Collection<String> <-> Collection<Options>
    * Collection<Entity> ((native, database) <-> Collection<Options>
* Annotations can be used in Setter, Getter or accessible class attributes
* Map nested java classes for example FirstSpirit FormDataList with section templates


## Installation
* Maven is required
* Git is required
* Clone project via git because no public artifact is available.

### Via Maven Repo (local or remote)
#### Maven
```<dependency>
	<groupId>com.diva-e.firstspirit</groupId>
	<artifactId>form-field-mapper</artifactId>
	<version>${form-field-mapper-version}</version>
</dependency>
```


## Ho to use it
Usage start with creation of a reusable (stateless) FormFieldMapper instance:

`FormFieldMapper formFieldMapper = new FormFieldMapper();`


### Usage, reading from FirstSpirit

```FormData formData = <getFormDataViaAPI()>
Language language = <getLanguageForAllFormFieldFromViaAPIOrNull()>
```

```DTO dto = new DTO() {

	@FormField("<FirstSpiritFormFieldName>")
	public <SupportedClassOrCollection> attribute;

	@FormField("<OtherFirstSpiritFormFieldName>")
	public void setOtherAttribute(<SupportedClassOrCollection> otherAttribute) {
		...
	}
}
```

`formFieldMapper.map(formData, dto, language);`


### Usage, writing to FirstSpirit
```DTO dto = new DTO() {

	@FormField("<FirstSpiritFormFieldName>")
	public <SupportedClassOrCollection> attribute;

	@FormField("<OtherFirstSpiritFormFieldName>")
	public <SupportedClassOrCollection> getOtherAttribute() {
		...
	}
}
```

```FormData formData = <getFormDataViaAPI()>
Language language = <getLanguageForAllFormFieldFromViaAPIOrNull()>
```

`formFieldMapper.map(dto, formData, language);`


### Mapping FormFields with section templates like in FormDataList
```@Template("<FirstSpiritTemplateName>")
public class Source {

	@FormField(<FirstSpiritFormFieldName>")
	public <SupportedClassOrCollection> attribute;
}
```

```public class DTO {

	@FormField(<FirstSpiritFormFieldName>")
	public Collection<Source> attribute;
}
```

```FormData formData = <getFormDataViaAPI()>
Language language = <getLanguageForAllFormFieldFromViaAPIOrNull()>
```

`formFieldMapper.map(dto, formData, language);`


## Ho to add custom mapping strategies
You can add new mapping strategies by using this method `FormFieldMapper#addStrategy`