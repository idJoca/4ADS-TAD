package com.ads.tad.Command;

import java.util.ArrayList;

import com.ads.tad.Helpers.Pair;

public abstract class Command {
    public static final String INVALID_TYPE_MODIFIER_ARGUMENT_ERROR = "A %s command does not support modifier arguments.";
    public static final String INVALID_TYPE_QUERY_ARGUMENT_ERROR = "A %s command does not support query arguments.";
    public String entity;
    public ArrayList<Pair<String, String>> modifierArguments = new ArrayList<>();
    public ArrayList<Pair<String, String>> queryArguments = new ArrayList<>();

    public Command(String entity, ArrayList<Pair<String, String>> modifierArguments,
            ArrayList<Pair<String, String>> queryArguments) {
        this.entity = entity;
        this.modifierArguments = modifierArguments;
        this.queryArguments = queryArguments;
    }

    public Command(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append(" ");
        stringBuilder.append(entity);
        stringBuilder.append(" [");

        int i = 0;
        for (Pair<String, String> argument : modifierArguments) {
            stringBuilder.append(String.format("%s=\"%s\"", argument.first, argument.second));
            i++;
            if (i < modifierArguments.size()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        stringBuilder.append(" [");

        i = 0;
        for (Pair<String, String> argument : queryArguments) {
            stringBuilder.append(String.format("%s=\"%s\"", argument.first, argument.second));
            i++;
            if (i < modifierArguments.size()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
