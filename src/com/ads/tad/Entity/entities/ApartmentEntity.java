package com.ads.tad.Entity.entities;

import java.util.ArrayList;

import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.Field;
import com.ads.tad.Entity.fields.EntityField;
import com.ads.tad.Entity.fields.RelationField;
import com.ads.tad.Helpers.Pair;

public class ApartmentEntity extends Entity {

    public String name, number, floor, building;

    private EntityField nameField = new EntityField("name", true, true), numberField = new EntityField("number", true),
            floorField = new EntityField("floor", true);
    private RelationField buildingField = new RelationField("building", new BuildingEntity(), true);

    public ApartmentEntity(String name, String number, String floor, String building) {
        this.name = name;
        this.number = number;
        this.floor = floor;
        this.building = building;
    }

    public ApartmentEntity() {

    }

    @Override
    public String getEntityName() {

        return "apartment";
    }

    @Override
    public Entity instantiate(ArrayList<Pair<String, String>> modifierArguments) {
        return new ApartmentEntity(getField(modifierArguments, nameField), getField(modifierArguments, numberField),
                getField(modifierArguments, floorField), getField(modifierArguments, buildingField));
    }

    @Override
    public boolean filter(Entity wantedEntity, ArrayList<Pair<String, String>> queryArguments) {
        if (!wantedEntity.getClass().equals(getClass())) {
            return false;
        }
        boolean matchesArguments = true;

        if (hasField(queryArguments, nameField)) {
            matchesArguments = name.contains(getField(queryArguments, nameField));
        }

        if (matchesArguments && hasField(queryArguments, numberField)) {
            matchesArguments = number.contains(getField(queryArguments, numberField));
        }

        if (matchesArguments && hasField(queryArguments, floorField)) {
            matchesArguments = floor.contains(getField(queryArguments, floorField));
        }

        if (matchesArguments && hasField(queryArguments, buildingField)) {
            matchesArguments = building.contains(getField(queryArguments, buildingField));
        }

        return matchesArguments;
    }

    @Override
    public void update(ArrayList<Pair<String, String>> modifierArguments) {
        if (hasField(modifierArguments, nameField)) {
            name = getField(modifierArguments, nameField);
        }

        if (hasField(modifierArguments, numberField)) {
            number = getField(modifierArguments, numberField);
        }

        if (hasField(modifierArguments, floorField)) {
            floor = getField(modifierArguments, floorField);
        }

        if (hasField(modifierArguments, buildingField)) {
            building = getField(modifierArguments, buildingField);
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
