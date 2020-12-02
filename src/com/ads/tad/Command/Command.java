package com.ads.tad.Command;

public abstract class Command {
    public String entity;
    public String[] fields = {};
    public String[] values = {};

    public Command(String entity, String[] fields, String[] values) {
        this.entity = entity;
        this.fields = fields;
        this.values = values;
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
        for (String field : fields) {
            stringBuilder.append(String.format("%s=\"%s\"", field, values[i]));
            i++;
            if (i < fields.length) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
