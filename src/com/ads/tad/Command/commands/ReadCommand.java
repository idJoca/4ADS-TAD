package com.ads.tad.Command.commands;

import java.util.ArrayList;

import com.ads.tad.Command.Argument;
import com.ads.tad.Command.Command;

public class ReadCommand extends Command {

    public ReadCommand(String entity) {
        super(entity);
    }

    public ReadCommand(String entity, ArrayList<Argument> arguments) {
        super(entity, arguments);
    }
}
