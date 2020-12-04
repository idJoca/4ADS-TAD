package com.ads.tad.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ads.tad.Command.Argument;
import com.ads.tad.Command.Command;
import com.ads.tad.Command.commands.CreateCommand;
import com.ads.tad.Command.commands.DeleteCommand;
import com.ads.tad.Command.commands.ReadCommand;
import com.ads.tad.Command.commands.UpdateCommand;

public class EntityManager {
    public static final String INVALID_ENTITY_ERROR = "The Entity %s does not exist.";
    public static final String INVALID_ARGUMENTS_ERROR = "The Entity %s does not have the following fields: %s.";
    public static final String INVALID_FIELDS_ERROR = "The following required fields are missing: %s.";
    public static EntityManager instance;
    private ArrayList<Entity> entities;
    private ArrayList<Entity> resultSet = new ArrayList<>();

    private EntityManager() {
    }

    public static EntityManager getEntityManager() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    public void registerEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public ArrayList<Entity> handleCommand(Command command) throws Exception {
        final Optional<Entity> entity = entities.stream()
                .filter((element) -> element.getEntityName().toUpperCase().equals(command.entity.toUpperCase()))
                .findFirst();

        if (entity.isEmpty()) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_ENTITY_ERROR, command.entity.toUpperCase()));
        }

        final Argument[] wrongArguments = entity.get().validateWrongArguments(command.arguments);
        if (wrongArguments.length > 0) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_ARGUMENTS_ERROR,
                    command.entity.toUpperCase(),
                    Arrays.stream(wrongArguments).map((argument) -> argument.field).collect(Collectors.joining(", "))));
        }

        final ArrayList<Entity> currentResultSet = new ArrayList<>();

        if (command instanceof CreateCommand) {
            validateRequiredArguments(entity, command);

            Entity newEntity = entity.get().instantiate(command.arguments);
            currentResultSet.add(newEntity);
            resultSet.add(newEntity);
        } else if (command instanceof ReadCommand) {
            resultSet.stream().forEach((result) -> {
                if (result.filter(command.arguments)) {
                    currentResultSet.add(result);
                }
            });
        } else if (command instanceof DeleteCommand) {
            resultSet.stream().forEach((result) -> {
                if (result.filter(command.arguments)) {
                    resultSet.remove(result);
                }
            });
        } else if (command instanceof UpdateCommand) {
            // TODO
        }

        return currentResultSet;
    }

    private void validateRequiredArguments(Optional<Entity> entity, Command command) throws Exception {
        final String[] missingRequiredArguments = entity.get().validateMissingRequiredArguments(command.arguments);
        if (missingRequiredArguments.length > 0) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_FIELDS_ERROR,
                    Arrays.stream(missingRequiredArguments).collect(Collectors.joining(", "))));
        }
    }
}
