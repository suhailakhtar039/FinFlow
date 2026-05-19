package com.finflow.observability;

import org.slf4j.MDC;

public final class MdcUtil {

    private MdcUtil() {}

    public static void put(String key, String value) {
        if (value != null && !value.isBlank()) {
            MDC.put(key, value);
        }
    }

    public static void remove(String key) {
        MDC.remove(key);
    }

    public static void clear() {
        MDC.clear();
    }
}
