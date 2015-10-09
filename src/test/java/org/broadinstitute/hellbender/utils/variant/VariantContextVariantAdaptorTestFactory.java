package org.broadinstitute.hellbender.utils.variant;

import java.util.UUID;

/**
 * Hack for creating {@link VariantContextVariantAdapter} with non-random UUIDs for testing
 */
public class VariantContextVariantAdaptorTestFactory {

    public static VariantContextVariantAdapter createWithKnownUUID(VariantContextVariantAdapter vc, UUID uuid) {
        return new VariantContextVariantAdapter(vc, uuid);
    }
}
