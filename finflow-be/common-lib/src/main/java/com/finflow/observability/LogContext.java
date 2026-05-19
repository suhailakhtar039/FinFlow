package com.finflow.observability;

public final class LogContext implements AutoCloseable {

    private final String key;

    public LogContext(String key, String value) {
        this.key = key;
        MdcUtil.put(key, value);
    }

    @Override
    public void close() {
        MdcUtil.remove(key);
    }
}
