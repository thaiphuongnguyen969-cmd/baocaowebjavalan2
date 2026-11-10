package com.fashion.backend.util;

import java.text.Normalizer;
import java.util.Locale;

public final class AccountNormalizer {
    private AccountNormalizer() {}

    public static String normalize(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFC)
            .trim().toLowerCase(Locale.ROOT);
    }
}
