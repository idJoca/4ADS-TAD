package com.ads.tad.Entity.entities;

import java.util.ArrayList;

import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.Field;
import com.ads.tad.Entity.fields.EntityField;
import com.ads.tad.Entity.fields.RelationField;
import com.ads.tad.Helpers.Pair;

public class ApartmentEntity extends Entity {

    public String number, floor, name, building;

    private EntityField nameField = new EntityField("name", true, true), numberField = new EntityField("number", true),
            floorField = new EntityField("floor", true);
    private RelationField buildingField = new RelationField("building", new BuildingEntity(), false);

    public ApartmentEntity(String number, String floor, String name) {
        this.number = number;
        this.floor = floor;
        this.name = name;
    }

    public ApartmentEntity() {

    }

    @Override
    public String getEntityName() {

        return "apartment";
    }

    @Override
    public Entity instantiate(ArrayList<Pair<String, String>> modifierArguments) {
        return new ApartmentEntity(getField(modifierArguments, numberField), getField(modifierArguments, floorField),
                getField(modifierArguments, nameField));
    }

    @Override
    public boolean filter(Entity wantedEntity, ArrayList<Pair<String, String>> queryArguments) {
        if (!wantedEntity.getClass().equals(getClass())) {
            return false;
        }
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
    protected ArrayList<Field> getEntityFields() {
        ArrayList<Field> fields = new ArrayList<>();

        fields.add(numberField);
        fields.add(floorField);
        fields.add(buildingField);
        fields.add(nameField);

        return fields;
    }

}
