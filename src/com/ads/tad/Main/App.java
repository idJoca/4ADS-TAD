package com.ads.tad.Main;

import java.util.ArrayList;
import java.util.Scanner;

import com.ads.tad.Command.Command;
import com.ads.tad.Command.CommandHandler;
import com.ads.tad.Entity.Entity;
import com.ads.tad.Entity.EntityManager;
import com.ads.tad.Entity.entities.PersonEntity;

public class App {
    public static void main(String[] args) {
        EntityManager entityManager = setup();
        CommandHandler commandHandler = new CommandHandler();
        Scanner scanner = new Scanner(System.in);
        String rawCommand = "";
        do {
            try {
                rawCommand = scanner.nextLine();
                if (rawCommand == null) {
                    continue;
                }
                Command command = commandHandler.parseCommand(rawCommand);
                System.out.println(command);
                System.out.println(entityManager.handleCommand(command));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!rawCommand.toUpperCase().equals("CLOSE"));
        scanner.close();
    }

    public static EntityManager setup() {
        ArrayList<Entity> entities = new ArrayList<>();

        entities.add(new PersonEntity());

        EntityManager entityManager = EntityManager.getEntityManager();
        entityManager.registerEntities(entities);

        return entityManager;
    }
}
