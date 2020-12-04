package com.ads.tad.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ads.tad.Command.commands.CreateCommand;
import com.ads.tad.Command.commands.DeleteCommand;
import com.ads.tad.Command.commands.ReadCommand;
import com.ads.tad.Command.commands.UpdateCommand;

public class CommandHandler {
    public static final String[] KEYWORDS = { "CREATE", "READ", "UPDATE", "DELETE" };
    public static final String INVALID_KEYWORD_ERRROR = "Invalid keyword.";
    public static final String INVALID_COMMAND_ERRROR = "The command does not have a valid syntax. Should be: KEYWORD ENTITY [,FIELD=\"VALUE\",...]";
    public static final String INVALID_ARGUMENT_ERRROR = "The argument does not have a valid syntax. Should be: KEYWORD ENTITY [,FIELD=\"VALUE\",...]";
    public static final String INVALID_QUERY_ARGUMENT_ERRROR = "Error at argument: \"%s\". A query argument (:FIELD=\"VALUE\") is only valid on a UPDATE command.";
    public static final String[] ERRORS = { INVALID_COMMAND_ERRROR };

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

        if (pieces[0].toUpperCase().equals(KEYWORDS[0])) {
            return parseCreate(rawCommand, pieces);
        } else if (pieces[0].toUpperCase().equals(KEYWORDS[1])) {
            return parseRead(rawCommand, pieces);
        } else if (pieces[0].toUpperCase().equals(KEYWORDS[2])) {
            return parseUpdate(rawCommand, pieces);
        } else if (pieces[0].toUpperCase().equals(KEYWORDS[3])) {
            return parseDelete(rawCommand, pieces);
        } else {
            throw new Exception(INVALID_KEYWORD_ERRROR);
        }
    }

    private Command parseCreate(String rawCommand, String[] pieces) throws Exception {
        return new CreateCommand(pieces[1], extractArguments(rawCommand, pieces, false).get(0));
    }

    private Command parseRead(String rawCommand, String[] pieces) throws Exception {
        return new ReadCommand(pieces[1], extractArguments(rawCommand, pieces, false).get(0));
    }

    private Command parseUpdate(String rawCommand, String[] pieces) throws Exception {
        List<ArrayList<Argument>> pair = extractArguments(rawCommand, pieces, true);
        return new UpdateCommand(pieces[1], pair.get(0), pair.get(1));
    }

    private Command parseDelete(String rawCommand, String[] pieces) throws Exception {
        return new DeleteCommand(pieces[1], extractArguments(rawCommand, pieces, false).get(0));
    }

    private List<ArrayList<Argument>> extractArguments(String rawCommand, String[] rawPieces, boolean hasQueryArguments)
            throws Exception {
        if (rawPieces.length < 3) {
            return new ArrayList<>();
        }
        final String pieces = rawCommand.substring(rawPieces[0].length() + rawPieces[1].length() + 2);

        ArrayList<Argument> arguments = new ArrayList<>();
        ArrayList<Argument> queryArguments = new ArrayList<>();

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
                queryArguments.add(new Argument(matcher.group(1), matcher.group(2)));
            } else {
                arguments.add(new Argument(matcher.group(1), matcher.group(2)));
            }
        }

        List<ArrayList<Argument>> pair = new ArrayList<>();

        pair.add(arguments);
        pair.add(queryArguments);

        return pair;
    }
}
