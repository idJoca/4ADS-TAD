package com.ads.tad.Sample;

import java.util.ArrayList;
import java.util.Scanner;

import com.ads.tad.Command.Command;
import com.ads.tad.Command.CommandHandler;
import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.EntityManager;
import com.ads.tad.Entity.entities.ApartmentEntity;
import com.ads.tad.Entity.entities.BuildingEntity;
import com.ads.tad.Entity.entities.PersonEntity;
import com.ads.tad.Helpers.FileHelper;

public class App {
    public static final String PREFIX = "ENTITIES";
    public static final boolean DEBUG = false;

    public static void main(String[] args) {
        String content = null;
        CommandHandler commandHandler = null;
        EntityManager entityManager = null;
        try {
            content = FileHelper.read(PREFIX);
            commandHandler = new CommandHandler();
            entityManager = setup(commandHandler.deserialize(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        String rawCommand = "";
        do {
            try {
                rawCommand = scanner.nextLine();
                if (rawCommand == null || rawCommand.isEmpty()) {
                    continue;
                }
                if (rawCommand.toUpperCase().equals("CLOSE")) {
                    FileHelper.save(entityManager.serialize(), PREFIX);
                    break;
                }
                Command command = commandHandler.parseCommand(rawCommand);
                ArrayList<Entity> resultSet = entityManager.handleCommand(command);
                System.out.println(resultSet);
            } catch (Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                } else {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } while (true);
        scanner.close();
    }

    public static EntityManager setup(ArrayList<Command> commands) throws Exception {
        ArrayList<Entity> entities = new ArrayList<>();

        entities.add(new PersonEntity());
        entities.add(new ApartmentEntity());
        entities.add(new BuildingEntity());

        EntityManager entityManager = EntityManager.getEntityManager(commands, entities);

        return entityManager;
    }
}
