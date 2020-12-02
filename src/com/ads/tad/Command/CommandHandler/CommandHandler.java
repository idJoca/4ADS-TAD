package com.ads.tad.Command.CommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ads.tad.Command.Command;
import com.ads.tad.Command.commands.CreateComand;
import com.ads.tad.Command.commands.DeleteCommand;
import com.ads.tad.Command.commands.ReadCommand;
import com.ads.tad.Command.commands.UpdateCommand;

public class CommandHandler {
    public static final String[] KEYWORDS = { "CREATE", "READ", "UPDATE", "DELETE" };
    public static final String INVALID_KEYWORD_ERRROR = "Invalid keyword.";
    public static final String INVALID_COMMAND_ERRROR = "The command does not have a valid syntax. Should be: KEYWORD ENTITY [,FIELD=VALUE,...]";
    public static final String INVALID_ARGUMENT_ERRROR = "The argument does not have a valid syntax. Should be: KEYWORD ENTITY [,FIELD=VALUE,...]";
    public static final String[] ERRORS = { INVALID_COMMAND_ERRROR };

    /**
     * 
     * @param rawCommand
     * @return
     * @throws Exception
     */
    public Command parseCommand(String rawCommand) throws Exception {
        final String rawKeyword = rawCommand.split(" ")[0].toUpperCase();
        final String[] pieces = rawCommand.split(" ");

        if (pieces.length < 2) {
            throw new Exception(INVALID_COMMAND_ERRROR);
        }

        if (rawKeyword.equals(KEYWORDS[0])) {
            return parseCreate(rawCommand, pieces);
        } else if (rawKeyword.equals(KEYWORDS[1])) {
            return parseRead(rawCommand, pieces);
        } else if (rawKeyword.equals(KEYWORDS[2])) {
            return parseUpdate(rawCommand, pieces);
        } else if (rawKeyword.equals(KEYWORDS[3])) {
            return parseDelete(rawCommand, pieces);
        } else {
            throw new Exception(INVALID_KEYWORD_ERRROR);
        }
    }

    private Command parseCreate(String rawCommand, String[] pieces) throws Exception {
        List<List<String>> arguments = extractArguments(rawCommand, pieces);
        return new CreateComand(pieces[1], arguments.get(0).toArray(new String[0]),
                arguments.get(1).toArray(new String[0]));
    }

    private Command parseRead(String rawCommand, String[] pieces) throws Exception {
        List<List<String>> arguments = extractArguments(rawCommand, pieces);
        return new ReadCommand(pieces[1], arguments.get(0).toArray(new String[0]),
                arguments.get(1).toArray(new String[0]));
    }

    private Command parseUpdate(String rawCommand, String[] pieces) throws Exception {
        List<List<String>> arguments = extractArguments(rawCommand, pieces);
        return new UpdateCommand(pieces[1], arguments.get(0).toArray(new String[0]),
                arguments.get(1).toArray(new String[0]));
    }

    private Command parseDelete(String rawCommand, String[] pieces) throws Exception {
        List<List<String>> arguments = extractArguments(rawCommand, pieces);
        return new DeleteCommand(pieces[1], arguments.get(0).toArray(new String[0]),
                arguments.get(1).toArray(new String[0]));
    }

    private List<List<String>> extractArguments(String rawCommand, String[] rawPieces) throws Exception {
        final String arguments = rawCommand.substring(rawPieces[0].length() + rawPieces[1].length() + 2);

        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();

        // Matches the following:
        // a-Z="any character",a-Z="any character"
        // ^^^|_^^^^^^^^^^^^^_^___________________^
        // ^^^|_^^^^^^^^^^^^^_^___________________End of line (instead of the comma)
        // ^^^|_^^^^^^^^^^^^^_Comma between arguments
        // ^^^|_Matches any character between the quotes
        // ^^^Matches a equals sign, with optional space padding
        // Matches any word character (from a to z and from A to Z)
        Pattern pattern = Pattern.compile("([\\w]+)[\s]{0,1}=[\s]{0,1}\"(.*?)\"(,|\\z)");
        Matcher matcher = pattern.matcher(arguments);
        while (matcher.find()) {
            if (matcher.groupCount() < 2) {
                throw new Exception(INVALID_KEYWORD_ERRROR);
            }
            fields.add(matcher.group(1));
            values.add(matcher.group(2));
        }

        List<List<String>> pieces = new ArrayList<>();

        pieces.add(fields);
        pieces.add(values);

        return pieces;
    }
}
