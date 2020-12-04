package com.ads.tad.Entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ads.tad.Helpers.Pair;

public abstract class Entity {

    protected static String getField(ArrayList<Pair<String, String>> arguments, EntityField field) {
        Optional<Pair<String, String>> argument = arguments.stream()
                .filter((element) -> element.first.equals(field.name)).findFirst();
        return argument.isPresent() ? argument.get().second : field.defaultValue;
    }

    protected static boolean hasField(ArrayList<Pair<String, String>> arguments, EntityField field) {
        return arguments.stream().filter((element) -> element.first.equals(field.name)).findFirst().isPresent();
    }

    public abstract String getEntityName();

    public abstract Entity instantiate(ArrayList<Pair<String, String>> modifierArguments);

    public abstract boolean filter(ArrayList<Pair<String, String>> queryArguments);

    public abstract void update(ArrayList<Pair<String, String>> modifierArguments);

    protected abstract ArrayList<EntityField> getEntityFields();

    /**
     * Validate the arguments, by returning the fields that do not match the ones in
     * the Entity, if any.
     * 
     * @param modifierArguments
     * @return
     */
    public Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> validateWrongArguments(
            ArrayList<Pair<String, String>> modifierArguments, ArrayList<Pair<String, String>> queryArguments) {
        return new Pair<>(
                (ArrayList<Pair<String, String>>) modifierArguments.stream()
                        .filter((argument) -> !getEntityFields().stream()
                                .anyMatch((entityField) -> entityField.name.contains(argument.first)))
                        .collect(Collectors.toList()),
                (ArrayList<Pair<String, String>>) queryArguments.stream()
                        .filter((argument) -> !getEntityFields().stream()
                                .anyMatch((entityField) -> entityField.name.contains(argument.first)))
                        .collect(Collectors.toList()));
    }

    /**
     * Validates the arguments by searching for missing required fields.
     * 
     * @param modifierArguments
     * @return
     */
    public String[] validateMissingRequiredArguments(ArrayList<Pair<String, String>> modifierArguments) {
        return this.getEntityFields().stream()
                .filter((entityField) -> entityField.required
                        && !modifierArguments.stream().anyMatch((argument) -> entityField.name.equals(argument.first)))
                .map((entityField) -> entityField.name).toArray(String[]::new);
    }

    public ArrayList<Pair<String, String>> getFieldPairs() {
        ArrayList<Pair<String, String>> fieldPairs = new ArrayList<>();
        for (EntityField entityField : getEntityFields()) {
            Field field;
            try {
                field = getClass().getDeclaredField(entityField.name);
                field.setAccessible(true);
                fieldPairs.add(new Pair<>(entityField.name, (String) field.get(this)));
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        return fieldPairs;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append(" ");
        stringBuilder.append(getEntityName());
        stringBuilder.append(" [");

        int i = 0;
        final ArrayList<Pair<String, String>> fieldPair = getFieldPairs();
        for (Pair<String, String> argument : fieldPair) {
            stringBuilder.append(String.format("%s=\"%s\"", argument.first, argument.second));
            i++;
            if (i < fieldPair.size()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
