package com.ads.tad.Entity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ads.tad.Helpers.Pair;
import com.ads.tad.Command.Command;
import com.ads.tad.Command.commands.CreateCommand;
import com.ads.tad.Command.commands.DeleteCommand;
import com.ads.tad.Command.commands.ReadCommand;
import com.ads.tad.Command.commands.UpdateCommand;
import com.ads.tad.Entity.fields.EntityField;
import com.ads.tad.Entity.fields.RelationField;

public class EntityManager {
    public static final String INVALID_ENTITY_ERROR = "The Entity %s does not exist.";
    public static final String INVALID_MODIFIER_ARGUMENTS_ERROR = "The Entity %s does not have the following modifier fields: %s.";
    public static final String INVALID_QUERY_ARGUMENTS_ERROR = "The Entity %s does not have the following query fields: %s.";
    public static final String INVALID_ENTITY_FIELDS_ERROR = "The following required modifier fields are missing: %s.";
    public static final String INVALID_RELATION_FIELDS_ERROR = "The following required relation fields are missing: %s.";
    public static final String NONEXISTING_RELATION_FIELDS_VALUE_ERROR = "The following relation fields target nonexisting entities: %s.";
    public static EntityManager instance;
    private ArrayList<Entity> entities;
    private ArrayList<Entity> resultSet = new ArrayList<>();

    private EntityManager() {
    }

    public static EntityManager getEntityManager(ArrayList<Command> commands, ArrayList<Entity> entities)
            throws Exception {
        if (instance == null) {
            instance = new EntityManager();
        }

        instance.registerEntities(entities);
        if (commands != null) {
            for (Command command : commands) {
                instance.handleCommand(command);
            }
        }
        return instance;
    }

    public void registerEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public String serialize() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        for (Entity entity : resultSet) {
            stringBuilder.append(entity.toCreateCommand() + "\n");
        }

        return stringBuilder.toString();
    }

    public ArrayList<Entity> handleCommand(Command command) throws Exception {
        final Optional<Entity> entity = entities.stream()
                .filter((element) -> element.getEntityName().toUpperCase().equals(command.entity.toUpperCase()))
                .findFirst();

        if (entity.isEmpty()) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_ENTITY_ERROR, command.entity.toUpperCase()));
        }

        final Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> wrongArguments = entity.get()
                .validateWrongArguments(command.modifierArguments, command.modifierArguments);

        if (wrongArguments.first.size() > 0) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_MODIFIER_ARGUMENTS_ERROR,
                    command.entity.toUpperCase(),
                    wrongArguments.first.stream().map((argument) -> argument.first).collect(Collectors.joining(", "))));
        }

        if (wrongArguments.second.size() > 0) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_QUERY_ARGUMENTS_ERROR,
                    command.entity.toUpperCase(), wrongArguments.second.stream().map((argument) -> argument.first)
                            .collect(Collectors.joining(", "))));
        }

        final ArrayList<Entity> currentResultSet = new ArrayList<>();

        if (command instanceof CreateCommand) {
            validateRequiredArguments(entity, command);
            validateRelationFields(entity, command);

            Entity newEntity = entity.get().instantiate(command.modifierArguments);
            currentResultSet.add(newEntity);
            resultSet.add(newEntity);
        } else if (command instanceof ReadCommand) {
            resultSet.stream().forEach((result) -> {
                if (result.filter(entity.get(), command.queryArguments)) {
                    currentResultSet.add(result);
                }
            });
        } else if (command instanceof DeleteCommand) {
            ArrayList<Entity> resultSetClone = new ArrayList<>(resultSet);
            resultSetClone.stream().forEach((result) -> {
                if (result.filter(entity.get(), command.queryArguments)) {
                    currentResultSet.add(result);
                    resultSet.remove(result);
                }
            });
        } else if (command instanceof UpdateCommand) {
            validateRelationFields(entity, command);

            resultSet.stream().forEach((result) -> {
                if (result.filter(entity.get(), command.queryArguments)) {
                    currentResultSet.add(result);
                    result.update(command.modifierArguments);
                }
            });
        }

        return currentResultSet;
    }

    private void validateRequiredArguments(Optional<Entity> entity, Command command) throws Exception {
        final ArrayList<Field> missingRequiredArguments = entity.get()
                .validateMissingRequiredArguments(command.modifierArguments);

        final String missingEntityFields = missingRequiredArguments.stream()
                .filter((field) -> field instanceof EntityField).map((field) -> field.name)
                .collect(Collectors.joining(", "));
        final String missingRelationFields = missingRequiredArguments.stream()
                .filter((field) -> field instanceof RelationField).map((field) -> field.name)
                .collect(Collectors.joining(", "));

        if (missingEntityFields.length() > 0) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_ENTITY_FIELDS_ERROR, missingEntityFields));
        }

        if (missingRelationFields.length() > 0) {
            throw new Exception(
                    String.format(Locale.getDefault(), INVALID_RELATION_FIELDS_ERROR, missingRelationFields));
        }
    }

    private void validateRelationFields(Optional<Entity> entity, Command command) throws Exception {
        // Get all the relation arguments that do not match a existing entity
        String nonexistingArguments = command.modifierArguments.stream().filter((argument) -> {
            // Get the relation arguments fields from the target entity
            return entity.get().getEntityFields().stream().anyMatch((field) -> {
                // Get the existing entities that matches the relation constraint
                return field instanceof RelationField
                        && argument.first.equals(field.name) && field.required && !argument.second
                                .isEmpty()
                        && resultSet.stream().anyMatch((result) -> {
                            // Check the entity fields for it's main key
                            return result.getClass().equals(((RelationField) field).relation
                                    .getClass()) && result.getEntityFields().stream().anyMatch(
                                            (entityField) -> {
                                                // Check if the Main Key value matches the argument provided
                                                return entityField instanceof EntityField
                                                        && ((EntityField) entityField).mainField
                                                        && !result.getFieldByName(entityField.name)
                                                                .equals(argument.second);
                                            });
                        });
            });
        }).map((argument) -> String.format(Locale.getDefault(), "%s=\"%s\"", argument.first, argument.second))
                .collect(Collectors.joining(", "));

        if (nonexistingArguments.length() > 0) {
            throw new Exception(
                    String.format(Locale.getDefault(), NONEXISTING_RELATION_FIELDS_VALUE_ERROR, nonexistingArguments));
        }
    }
}
