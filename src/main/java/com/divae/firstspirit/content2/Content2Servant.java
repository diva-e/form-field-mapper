package com.divae.firstspirit.content2;

import de.espirit.firstspirit.access.ReferenceEntry;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.contentstore.Content2;
import de.espirit.firstspirit.access.store.templatestore.TableTemplate;

public final class Content2Servant {

    public Content2 getDatabase(final TableTemplate tableTemplate, final String databaseUid) {
        for (final ReferenceEntry referenceEntry : tableTemplate.getIncomingReferences()) {
            final IDProvider idProvider = referenceEntry.getReferencedElement();
            if (!(idProvider instanceof Content2) || !databaseUid.equals(idProvider.getUid())) {
                continue;
            }
            return (Content2) idProvider;
        }
        return null;
    }
}
