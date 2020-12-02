package com.ads.tad.Command.commands;

import com.ads.tad.Command.Command;

public class DeleteCommand extends Command {

    public DeleteCommand(String entity) {
        super(entity);
    }

    public DeleteCommand(String entity, String[] fields, String[] values) {
        super(entity, fields, values);
    }
}
