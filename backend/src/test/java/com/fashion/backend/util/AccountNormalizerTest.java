package com.fashion.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountNormalizerTest {
    @Test
    void trimsLowercasesAndNormalizesUnicode() {
        assertEquals("admin",AccountNormalizer.normalize(" ADMIN "));
        assertEquals("é@shop.vn",AccountNormalizer.normalize("e\u0301@shop.vn"));
    }
}
