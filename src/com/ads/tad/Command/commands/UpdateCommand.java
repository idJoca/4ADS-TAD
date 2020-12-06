package com.ads.tad.Command.commands;

import java.util.ArrayList;

import com.ads.tad.Helpers.Pair;
import com.ads.tad.Command.Command;

public class UpdateCommand extends Command {
    public UpdateCommand(String entity) {
        super(entity);
    }

    public UpdateCommand(String entity, ArrayList<Pair<String, String>> modifierArguments,
            ArrayList<Pair<String, String>> queryArguments) {
        super(entity, modifierArguments, queryArguments);
    }
}
