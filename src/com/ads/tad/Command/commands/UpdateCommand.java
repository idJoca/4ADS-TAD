package com.ads.tad.Command.commands;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.ads.tad.Command.Argument;
import com.ads.tad.Command.Command;

public class UpdateCommand extends Command {
    public ArrayList<Argument> queryArguments;

    public UpdateCommand(String entity) {
        super(entity);
    }

    public UpdateCommand(String entity, ArrayList<Argument> arguments, ArrayList<Argument> queryArguments) {
        super(entity, arguments);
        this.queryArguments = queryArguments;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append(" ");
        stringBuilder.append(entity);
        stringBuilder.append(" [");

        int i = 0;
        for (Argument argument : arguments) {
            stringBuilder.append(String.format("%s=\"%s\"", argument.field, argument.value));
            i++;
            if (i < arguments.size()) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");

        stringBuilder.append(" [");

        i = 0;
        for (Argument argument : queryArguments) {
            stringBuilder.append(String.format("%s=\"%s\"", argument.field, argument.value));
            i++;
            if (i < arguments.size()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
