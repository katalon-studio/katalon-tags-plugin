package com.katalon.plugin.tags.entity;

public abstract class StringConsoleOption extends AbstractConsoleOption<String> {
    @Override
    public Class<String> getArgumentType() {
        return String.class;
    }

    @Override
    public void setValue(String rawValue) {
        value = rawValue;
    }
}
