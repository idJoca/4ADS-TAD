package com.ads.tad.Entity.entities;

import java.util.ArrayList;

import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.Field;
import com.ads.tad.Entity.fields.EntityField;
import com.ads.tad.Entity.fields.RelationField;
import com.ads.tad.Helpers.Pair;

public class BuildingEntity extends Entity {

    public String address, number;
    private EntityField addressField = new EntityField("address", true), numberField = new EntityField("number", true);

    public BuildingEntity() {
    }

    public BuildingEntity(String address, String number) {
        this.address = address;
        this.number = number;
    }

    @Override
    public String getEntityName() {
        return "building";
    }

    @Override
    public Entity instantiate(ArrayList<Pair<String, String>> modifierArguments) {
        return new BuildingEntity(getField(modifierArguments, addressField), getField(modifierArguments, numberField));
    }

    @Override
    public boolean filter(Entity wantedEntity, ArrayList<Pair<String, String>> queryArguments) {
        if (!wantedEntity.getClass().equals(getClass())) {
            return false;
        }
        boolean matchesArguments = true;

        if (hasField(queryArguments, addressField)) {
            matchesArguments = address.contains(getField(queryArguments, addressField));
        }

        if (matchesArguments && hasField(queryArguments, numberField)) {
            matchesArguments = number.contains(getField(queryArguments, numberField));
        }

        return matchesArguments;
    }

    @Override
    public void update(ArrayList<Pair<String, String>> modifierArguments) {
        if (hasField(modifierArguments, addressField)) {
            address = getField(modifierArguments, addressField);
        }

        if (hasField(modifierArguments, numberField)) {
            number = getField(modifierArguments, numberField);
        }
    }

    @Override
    protected ArrayList<Field> getEntityFields() {
        ArrayList<Field> fields = new ArrayList<>();

        fields.add(addressField);
        fields.add(numberField);

        return fields;
    }

}
