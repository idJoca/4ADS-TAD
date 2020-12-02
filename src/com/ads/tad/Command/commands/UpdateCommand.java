package com.ads.tad.Command.commands;

import com.ads.tad.Command.Command;

public class UpdateCommand extends Command {

    public UpdateCommand(String entity) {
        super(entity);
    }

    public UpdateCommand(String entity, String[] fields, String[] values) {
        super(entity, fields, values);
    }
}
