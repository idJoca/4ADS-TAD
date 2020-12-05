package com.ads.tad.Command.commands;

import java.util.ArrayList;
import java.util.Locale;

import com.ads.tad.Helpers.Pair;
import com.ads.tad.Command.Command;

public class CreateCommand extends Command {

    public CreateCommand(String entity) {
        super(entity);
    }

    public CreateCommand(String entity, ArrayList<Pair<String, String>> modifierArguments,
            ArrayList<Pair<String, String>> queryArguments) throws Exception {
        super(entity, modifierArguments, queryArguments);
        if (queryArguments != null && queryArguments.size() > 0) {
            throw new Exception(String.format(Locale.getDefault(), INVALID_TYPE_QUERY_ARGUMENT_ERROR, "CREATE"));
        }
    }
}
