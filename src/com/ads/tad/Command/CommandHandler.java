package com.ads.tad.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ads.tad.Command.commands.CreateCommand;
import com.ads.tad.Command.commands.DeleteCommand;
import com.ads.tad.Command.commands.ReadCommand;
import com.ads.tad.Command.commands.UpdateCommand;
import com.ads.tad.Helpers.Pair;

public class CommandHandler {
    public static final String[] KEYWORDS = { "CREATE", "READ", "UPDATE", "DELETE" };
    public static final String INVALID_KEYWORD_ERRROR = "Invalid keyword.";
    public static final String INVALID_COMMAND_ERRROR = "The command does not have a valid syntax. Should be: KEYWORD ENTITY [,FIELD=\"VALUE\",...]";
    public static final String INVALID_ARGUMENT_ERRROR = "The argument does not have a valid syntax. Should be: KEYWORD ENTITY [,FIELD=\"VALUE\",...]";
    public static final String INVALID_QUERY_ARGUMENT_ERRROR = "Error at argument: \"%s\". A query argument (:FIELD=\"VALUE\") is only valid on a UPDATE command.";
    public static final String[] ERRORS = { INVALID_COMMAND_ERRROR };

    public ArrayList<Command> deserialize(String content) {
        if (content == null) {
            return null;
        }

        ArrayList<Command> commands = new ArrayList<>();
        content.lines().forEach((command) -> {
            try {
                commands.add(parseCommand(command));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return commands;
    }

    /**
     * 
     * @param rawCommand
     * @return
     * @throws Exception
     */
    public Command parseCommand(String rawCommand) throws Exception {
        final String[] pieces = rawCommand.split(" ");

        if (pieces.length < 2) {
            throw new Exception(INVALID_COMMAND_ERRROR);
        }

        if (!Arrays.stream(KEYWORDS).anyMatch((keyword) -> keyword.equals(pieces[0].toUpperCase()))) {
            throw new Exception(INVALID_KEYWORD_ERRROR);
        }

        Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> pair = extractArguments(rawCommand,
                pieces, true);

        if (pieces[0].toUpperCase().equals(KEYWORDS[0])) {
            return parseCreate(pieces[1], pair);
        } else if (pieces[0].toUpperCase().equals(KEYWORDS[1])) {
            return parseRead(pieces[1], pair);
        } else if (pieces[0].toUpperCase().equals(KEYWORDS[2])) {
            return parseUpdate(pieces[1], pair);
        } else {
            // Delete Command
            return parseDelete(pieces[1], pair);
        }
    }

    private Command parseCreate(String entity,
            Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> pair) throws Exception {
        return new CreateCommand(entity, pair.first, pair.second);
    }

    private Command parseRead(String entity,
            Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> pair) throws Exception {
        return new ReadCommand(entity, pair.first, pair.second);
    }

    private Command parseUpdate(String entity,
            Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> pair) throws Exception {
        return new UpdateCommand(entity, pair.first, pair.second);
    }

    private Command parseDelete(String entity,
            Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> pair) throws Exception {
        return new DeleteCommand(entity, pair.first, pair.second);
    }

    private Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> extractArguments(String rawCommand,
            String[] rawPieces, boolean hasQueryArguments) throws Exception {
        ArrayList<Pair<String, String>> arguments = new ArrayList<>();
        ArrayList<Pair<String, String>> queryArguments = new ArrayList<>();
        Pair<ArrayList<Pair<String, String>>, ArrayList<Pair<String, String>>> pair = new Pair<>(arguments,
                queryArguments);

        if (rawPieces.length < 3) {
            return pair;
        }
        final String pieces = rawCommand.substring(rawPieces[0].length() + rawPieces[1].length() + 2);

        // Matches the following:
        // a-Z="any character",a-Z="any character"
        // ^^^|_^^^^^^^^^^^^^_^___________________^
        // ^^^|_^^^^^^^^^^^^^_^___________________End of line (instead of the comma)
        // ^^^|_^^^^^^^^^^^^^_Comma between arguments
        // ^^^|_Matches any character between the quotes
        // ^^^Matches a equals sign, with optional space padding
        // Matches any word character (from a to z and from A to Z)
        Pattern pattern = Pattern.compile("([:\\w]+)[\s]{0,1}=[\s]{0,1}\"(.*?)\"(,|\\z)");
        Matcher matcher = pattern.matcher(pieces);
        while (matcher.find()) {
            if (matcher.groupCount() < 2) {
                throw new Exception(INVALID_KEYWORD_ERRROR);
            }
            if (matcher.group(1).substring(0, 1).equals(":")) {
                if (!hasQueryArguments) {
                    throw new Exception(
                            String.format(Locale.getDefault(), INVALID_QUERY_ARGUMENT_ERRROR, matcher.group(1)));
                }
                // Removes the ':'
                queryArguments.add(new Pair<>(matcher.group(1).substring(1), normalize(matcher.group(2))));
            } else {
                arguments.add(new Pair<>(matcher.group(1), normalize(matcher.group(2))));
            }
        }

        return pair;
    }

    private String normalize(String value) {
        return value.replaceAll("\\\\n", "\n");
    }
}
