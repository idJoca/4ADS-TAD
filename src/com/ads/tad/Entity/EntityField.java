package com.ads.tad.Entity;

public class EntityField {
    public String name;
    public boolean required = false;
    public String defaultValue = "";

    public EntityField(String name) {
        this.name = name;
    }

    public EntityField(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public EntityField(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public EntityField(String name, boolean required, String defaultValue) {
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
    }
}
