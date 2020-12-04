package com.ads.tad.Command;

import java.util.ArrayList;

public abstract class Command {
    public String entity;
    public ArrayList<Argument> arguments = new ArrayList<>();

    public Command(String entity, ArrayList<Argument> arguments) {
        this.entity = entity;
        this.arguments = arguments;
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
        for (Argument argument : arguments) {
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
