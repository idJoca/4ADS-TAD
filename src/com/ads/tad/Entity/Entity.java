package com.ads.tad.Entity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ads.tad.Helpers.Normalizer;
import com.ads.tad.Helpers.Pair;

public abstract class Entity {

    protected static String getField(ArrayList<Pair<String, String>> arguments, Field field) {
        Optional<Pair<String, String>> argument = arguments.stream()
                .filter((element) -> element.first.equals(field.name)).findFirst();
        return argument.isPresent() ? argument.get().second : field.defaultValue;
    }

    protected static boolean hasField(ArrayList<Pair<String, String>> arguments, Field field) {
        return arguments.stream().filter((element) -> element.first.equals(field.name)).findFirst().isPresent();
    }

    public abstract String getEntityName();

    public abstract Entity instantiate(ArrayList<Pair<String, String>> modifierArguments);

    public abstract boolean filter(Entity wantedEntity, ArrayList<Pair<String, String>> queryArguments);

    public abstract void update(ArrayList<Pair<String, String>> modifierArguments);

    protected abstract ArrayList<Field> getEntityFields();

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
    public ArrayList<Field> validateMissingRequiredArguments(ArrayList<Pair<String, String>> modifierArguments) {
        return (ArrayList<Field>) this.getEntityFields().stream()
                .filter((entityField) -> entityField.required
                        && !modifierArguments.stream().anyMatch((argument) -> entityField.name.equals(argument.first)))
                .collect(Collectors.toList());
    }

    public ArrayList<Pair<String, String>> getFieldPairs() {
        ArrayList<Pair<String, String>> fieldPairs = new ArrayList<>();
        for (Field entityField : getEntityFields()) {
            try {
                fieldPairs.add(new Pair<>(entityField.name, getFieldByName(entityField.name)));
            } catch (IllegalArgumentException | SecurityException e) {
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

    public String toCreateCommand() throws Exception {
        return String.format(Locale.getDefault(), "create %s %s", getEntityName(),
                getFieldPairs().stream().map((field) -> String.format(Locale.getDefault(), "%s=\"%s\"", field.first,
                        Normalizer.normalize(field.second))).collect(Collectors.joining(",")));
    }

    public String getFieldByName(String name) {
        try {
            java.lang.reflect.Field field = getClass().getDeclaredField(name);
            field.setAccessible(true);
            return (String) field.get(this);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
