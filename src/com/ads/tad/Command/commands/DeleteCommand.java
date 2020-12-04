package com.ads.tad.Command.commands;

import java.util.ArrayList;

import com.ads.tad.Command.Argument;
import com.ads.tad.Command.Command;

public class DeleteCommand extends Command {

    public DeleteCommand(String entity) {
        super(entity);
    }

    public DeleteCommand(String entity, ArrayList<Argument> arguments) {
        super(entity, arguments);
    }
}