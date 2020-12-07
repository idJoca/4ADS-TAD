package com.ads.tad.Entity.fields;

import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.Field;

public class RelationField extends Field {
    public Entity relation;

    public RelationField(String name, Entity relation) {
        this.name = name;
        this.relation = relation;
    }

    public RelationField(String name, Entity relation, boolean required) {
        this.name = name;
        this.relation = relation;
        this.required = required;
    }
}
