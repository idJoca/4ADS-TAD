package com.ads.tad.Command.commands;

import java.util.ArrayList;

import com.ads.tad.Command.Argument;
import com.ads.tad.Command.Command;

public class CreateCommand extends Command {

    public CreateCommand(String entity) {
        super(entity);
    }

    public CreateCommand(String entity, ArrayList<Argument> arguments) {
        super(entity, arguments);
    }
}
