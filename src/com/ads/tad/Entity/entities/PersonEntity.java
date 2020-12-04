package com.ads.tad.Entity.entities;

import java.util.ArrayList;

import com.ads.tad.Helpers.Pair;
import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.EntityField;

public class PersonEntity extends Entity {
    public String name, surname;
    private EntityField nameField = new EntityField("name", true), surnameField = new EntityField("surname", true);

    public PersonEntity(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public PersonEntity() {
    }

    @Override
    public String getEntityName() {
        return "person";
    }

    @Override
    protected ArrayList<EntityField> getEntityFields() {
        ArrayList<EntityField> fields = new ArrayList<>();

        fields.add(nameField);
        fields.add(surnameField);

        return fields;
    }

    @Override
    public Entity instantiate(ArrayList<Pair<String, String>> arguments) {
        return new PersonEntity(getField(arguments, nameField), getField(arguments, surnameField));
    }

    @Override
    public boolean filter(ArrayList<Pair<String, String>> arguments) {
        boolean matchesArguments = true;

        if (hasField(arguments, nameField)) {
            matchesArguments = name.contains(getField(arguments, nameField));
        }

        if (matchesArguments && hasField(arguments, surnameField)) {
            matchesArguments = surname.contains(getField(arguments, surnameField));
        }

        return matchesArguments;
    }

    @Override
    public void update(ArrayList<Pair<String, String>> modifierArguments) {
        if (hasField(modifierArguments, nameField)) {
            name = getField(modifierArguments, nameField);
        }

        if (hasField(modifierArguments, surnameField)) {
            surname = getField(modifierArguments, surnameField);
        }
    }
}
