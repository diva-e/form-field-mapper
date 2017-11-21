package com.divae.firstspirit.gom;

import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.editor.TableTemplateProvider;
import de.espirit.firstspirit.access.editor.value.OptionFactoryProvider;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;
import de.espirit.firstspirit.access.store.templatestore.gom.GomFormElement;
import de.espirit.firstspirit.agency.SpecialistsBroker;

public final class GomFormElementServant {

    public TableTemplate getTableTemplate(final GomFormElement gomFormElement, final SpecialistsBroker specialistsBroker, final Language language) {
        return ((TableTemplateProvider) ((OptionFactoryProvider) gomFormElement.getIncludeConfiguration()).getOptionFactory().getOptionModel(specialistsBroker, language, true)).getTableTemplate();
    }
}
