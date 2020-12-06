package com.ads.tad.Entity.entities;

import java.util.ArrayList;

import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.EntityField;
import com.ads.tad.Helpers.Pair;

public class ApartmentEntity extends Entity {

    public String number, floor;
    private EntityField numberField = new EntityField("number", true), floorField = new EntityField("floor", true);

    public ApartmentEntity(String number, String floor) {
        this.number = number;
        this.floor = floor;
    }

    public ApartmentEntity() {

    }

    @Override
    public String getEntityName() {

        return "apartment";
    }

    @Override
    public Entity instantiate(ArrayList<Pair<String, String>> modifierArguments) {
        return new ApartmentEntity(getField(modifierArguments, numberField),getField(modifierArguments, floorField));
    }

    @Override
    public boolean filter(ArrayList<Pair<String, String>> queryArguments) {
        boolean matchesArguments = true;

        if (hasField(queryArguments, numberField)) {
            matchesArguments = number.contains(getField(queryArguments, numberField));
        }

        if (matchesArguments && hasField(queryArguments, floorField)) {
            matchesArguments = floor.contains(getField(queryArguments, floorField));
        }

        return matchesArguments;
    }

    @Override
    public void update(ArrayList<Pair<String, String>> modifierArguments) {
        if (hasField(modifierArguments, numberField)) {
            number = getField(modifierArguments, numberField);
        }
        if (hasField(modifierArguments, floorField)) {
            floor = getField(modifierArguments, floorField);
        }

    }

    @Override
    protected ArrayList<EntityField> getEntityFields() {
        ArrayList<EntityField> fields = new ArrayList<>();

        fields.add(numberField);
        fields.add(floorField);

        return fields;
    }

}
