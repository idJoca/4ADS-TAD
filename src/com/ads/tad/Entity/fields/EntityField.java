package com.ads.tad.Entity.fields;

import com.ads.tad.Entity.Field;

public class EntityField extends Field {
    public boolean mainField = false;

    public EntityField(String name) {
        this.name = name;
    }

    public EntityField(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public EntityField(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public EntityField(String name, boolean required, boolean mainField) {
        this.name = name;
        this.required = required;
        this.mainField = mainField;
    }

    public EntityField(String name, boolean required, String defaultValue, boolean mainField) {
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
        this.mainField = mainField;
    }
}
