package com.ads.tad.Command.commands;

import java.util.ArrayList;
import java.util.Locale;

import com.ads.tad.Helpers.Pair;
import com.ads.tad.Command.Command;

public class DeleteCommand extends Command {

    public DeleteCommand(String entity) {
        super(entity);
    }

    public DeleteCommand(String entity, ArrayList<Pair<String, String>> modifierArguments,
            ArrayList<Pair<String, String>> queryArguments) throws Exception {
        super(entity, modifierArguments, queryArguments);
        if (modifierArguments.size() > 0) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_TYPE_MODIFIER_ARGUMENT_ERROR, "DELETE"));
        }
    }
}
