package com.ads.tad.Main;

import java.util.Scanner;

import com.ads.tad.Command.Command;
import com.ads.tad.Command.CommandHandler.CommandHandler;

public class App {
    public static void main(String[] args) {
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
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!rawCommand.toUpperCase().equals("CLOSE"));
        scanner.close();
    }
}
