package com.ads.tad.Command.commands;

import com.ads.tad.Command.Command;

public class ReadCommand extends Command {

    public ReadCommand(String entity) {
        super(entity);
    }

    public ReadCommand(String entity, String[] fields, String[] values) {
        super(entity, fields, values);
    }
}
