package com.ads.tad.Entity;

import java.util.ArrayList;
import java.util.Optional;

import com.ads.tad.Command.Argument;

public abstract class Entity implements Cloneable {

    protected static String getField(ArrayList<Argument> arguments, EntityField field) {
        Optional<Argument> argument = arguments.stream().filter((element) -> element.field.equals(field.name))
                .findFirst();
        return argument.isPresent() ? argument.get().value : field.defaultValue;
    }

    protected static boolean hasField(ArrayList<Argument> arguments, EntityField field) {
        return arguments.stream().filter((element) -> element.field.equals(field.name)).findFirst().isPresent();
    }

    public abstract String getEntityName();

    public abstract Entity instantiate(ArrayList<Argument> arguments);

    public abstract boolean filter(ArrayList<Argument> arguments);

    protected abstract ArrayList<EntityField> getEntityFields();

    /**
     * Validate the arguments, by returning the fields that do not match the ones in
     * the Entity, if any.
     * 
     * @param arguments
     * @return
     */
    public Argument[] validateWrongArguments(ArrayList<Argument> arguments) {
        return (Argument[]) arguments.stream().filter((argument) -> !getEntityFields().stream()
                .anyMatch((entityField) -> entityField.name.contains(argument.field))).toArray(Argument[]::new);
    }

    /**
     * Validates the arguments by searching for missing required fields.
     * 
     * @param arguments
     * @return
     */
    public String[] validateMissingRequiredArguments(ArrayList<Argument> arguments) {
        return this.getEntityFields().stream()
                .filter((entityField) -> entityField.required
                        && !arguments.stream().anyMatch((argument) -> entityField.name.equals(argument.field)))
                .map((entityField) -> entityField.name).toArray(String[]::new);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
