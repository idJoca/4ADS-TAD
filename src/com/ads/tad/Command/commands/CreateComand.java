package com.ads.tad.Command.commands;

import com.ads.tad.Command.Command;

public class CreateComand extends Command {

    public CreateComand(String entity) {
        super(entity);
    }

    public CreateComand(String entity, String[] fields, String[] values) {
        super(entity, fields, values);
    }
}
