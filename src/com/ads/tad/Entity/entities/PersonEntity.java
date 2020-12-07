package com.ads.tad.Entity.entities;

import java.util.ArrayList;

import com.ads.tad.Helpers.Pair;
import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.Field;
import com.ads.tad.Entity.fields.EntityField;
import com.ads.tad.Entity.fields.RelationField;

public class PersonEntity extends Entity {
    public String name, surname, apartment;

    private EntityField nameField = new EntityField("name", true), surnameField = new EntityField("surname", true);
    private RelationField apartmentField = new RelationField("apartment", new ApartmentEntity(), true);

    public PersonEntity(String name, String surname, String apartment) {
        this.name = name;
        this.surname = surname;
        this.apartment = apartment;
    }

    public PersonEntity() {
    }

    @Override
    public String getEntityName() {
        return "person";
    }

    @Override
    public Entity instantiate(ArrayList<Pair<String, String>> arguments) {
        return new PersonEntity(getField(arguments, nameField), getField(arguments, surnameField),
                getField(arguments, apartmentField));
    }

    @Override
    public boolean filter(Entity wantedEntity, ArrayList<Pair<String, String>> arguments) {
        if (!wantedEntity.getClass().equals(getClass())) {
            return false;
        }
        boolean matchesArguments = true;

        if (hasField(arguments, nameField)) {
            matchesArguments = name.contains(getField(arguments, nameField));
        }

        if (matchesArguments && hasField(arguments, surnameField)) {
            matchesArguments = surname.contains(getField(arguments, surnameField));
        }

        if (matchesArguments && hasField(arguments, apartmentField)) {
            matchesArguments = apartment.contains(getField(arguments, apartmentField));
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

        if (hasField(modifierArguments, apartmentField)) {
            apartment = getField(modifierArguments, apartmentField);
        }
    }

    @Override
    protected ArrayList<Field> getEntityFields() {
        ArrayList<Field> fields = new ArrayList<>();

        fields.add(nameField);
        fields.add(surnameField);
        fields.add(apartmentField);

        return fields;
    }
}
