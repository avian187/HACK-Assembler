/*
    AUTHOR: Ian Utnehmer, MiraCosta College
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
    String cleanLine = "";
    Command commandType;
    String compMnemonic = "";
    String destMnemonic = "";
    Scanner inputFile;
    String jumpMnemonic = "";
    int lineNumber;
    String rawLine = "";
    String symbol = "";

    public Parser(String fileName) throws FileNotFoundException {

        // DESCRIPTION: opens input file/stream and prepares to parse
        // PRECONDITION: provided file is ASM File
        // POSTCONDITION: if file can’t be opened, ends program w/ error message

        // Open the file
        if (fileName != null) {
            try {
                inputFile = new Scanner(new File(fileName));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            lineNumber = 0; // Initialize the line number to 0
            commandType = Command.NO_COMMAND; // Initialize the command type to NO_COMMAND
        }
    }

    public boolean hasMoreCommands() {

        //DESCRIPTION: returns boolean if more commands left, closes stream if not
        //PRECONDITION: file stream is open
        //POSTCONDITION: returns true if more commands, else closes stream

        try {
            if (inputFile.hasNextLine()) {
                return true;
            } else {
                inputFile.close();
                return false;
            }
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static String cleanLine(String rawLine) {

        //DESCRIPTION: cleans raw instruction by removing non-essential parts
        //PRECONDITION: String parameter given (not null)
        //POSTCONDITION: returned without comments and whitespace

        // Remove comments and whitespace
        if(rawLine != null && rawLine.contains("\\"))
            rawLine = rawLine.substring(0, rawLine.indexOf('/')).trim();
        else
            rawLine = rawLine.trim();
        return rawLine;
    }

    public Command parseCommandType(String cleanLine) {

        //DESCRIPTION: determines command type from parameter
        //PRECONDITION: String parameter is clean instruction
        //POSTCONDITION: returns A_COMMAND (A-instruction), C_COMMAND(Cinstruction),L_COMMAND(Label),or NO_COMMAND (no command)

        // Check if the line is a command
        if (cleanLine.contains("(")) // Labels will contain "("
            return commandType = Command.L_COMMAND;
        else if (cleanLine.startsWith("@")) // A-Commands will start with "@"
            return commandType = Command.A_COMMAND;
        else if (cleanLine.contains("=") || cleanLine.contains(";")) // C-Commands will contain "=" or ";"
            return commandType = Command.C_COMMAND;
        return Command.NO_COMMAND; // If the line is not a command, return NO_COMMAND
    }


    private void parseSymbol() {

        //DESCRIPTION: parses symbol for A- or L-commands
        //PRECONDITION: advance() called so cleanLine has value, call for A- and L-Commands only
        //POSTCONDITION: symbol has appropriate value from instruction assigned

        // If the line is an A-Command, get the symbol
        if(cleanLine.startsWith("@")) {
            symbol = cleanLine;
            symbol = symbol.replaceAll("@", "");
        } else {
            // If the line is a label, get the symbol
            if (cleanLine.startsWith("(")) {
                symbol = cleanLine;
                symbol = symbol.replaceAll("\\((.*?)\\)", "$1");
            }
        }
    }

    private void parseDest() {

        //DESCRIPTION: helper method parses line to get dest part
        //PRECONDITION: advance() called so cleanLine has value, call for C-instructions only
        //POSTCONDITION: destMnemonic set to appropriate value from instruction

        // If the line is a C-Command, get the destination mnemonic
        if(cleanLine.contains("=")) {
            destMnemonic = cleanLine.substring(0, cleanLine.indexOf('='));
        } else
            // If the line is not a C-Command, set the destination mnemonic to NULL
            destMnemonic = "NULL";
    }

    private void parseComp() {

        //DESCRIPTION: helper method parses line to get comp part
        //PRECONDITION: advance() called so cleanLine has value, call for C-instructions only
        //POSTCONDITION: compMnemonic set to appropriate value from instruction

        // If the line is a C-Command, get the computation mnemonic
        String retComp = cleanLine;
        if(cleanLine.contains("=")) {
            int endIndex = retComp.lastIndexOf("="); // Get the index of the "="
            retComp = retComp.substring(endIndex+1, retComp.length()); // Get the substring after the "="
            destMnemonic = retComp; // Set the destination mnemonic to the substring
        } else if (cleanLine.contains(";")) {
            int endIndex = retComp.lastIndexOf(";");
            retComp = retComp.substring(0, endIndex);
        }
        compMnemonic = retComp;
    }

    private void parseJump() {

        //DESCRIPTION: helper method parses line to get jump part
        //PRECONDITION: advance() called so cleanLine has value, call for C-instructions only
        //POSTCONDITION: jumpMnemonic set to appropriate value from instruction

        // If the line is a C-Command, get the jump mnemonic
        if(cleanLine.contains(";")) {
            String retJump = cleanLine;
            int endIndex = retJump.lastIndexOf(";");
            jumpMnemonic = retJump.substring(endIndex+1,retJump.length());
        } else
            jumpMnemonic = "NULL";
    }

    private void parse() {

        //DESCRIPTION: helper method parses line depending on instruction type
        //PRECONDITION: advance() called so cleanLine has value
        //POSTCONDITION: appropriate parts (instance vars) of instruction filled

        // Parse the line
        commandType = parseCommandType(cleanLine);
        switch(commandType) { // Switch on the command type
            case NO_COMMAND:
                break; // If the command is not a command, do nothing
            case A_COMMAND, L_COMMAND:
                parseSymbol();
                break; // If the command is an A-Command or L-Command, get the symbol
            case C_COMMAND:
                parseComp();
                parseDest();
                parseJump();
                break; // If the command is a C-Command, get the computation, destination, and jump mnemonics
        }
    }

    public String getCleanLine() {

        //DESCRIPTION: getter for rawLine from file (Debugging)
        //PRECONDITION: advance() and cleanLine() were called
        //POSTCONDITION: returns string of current clean instructionfrom file

        return cleanLine;
    }

    public void advance() {

        //DESCRIPTION: reads next line from file and parsaes it into instance vars
        //PRECONDITION: file stream is open, called only if hasMoreCommands()
        //POSTCONDITION: current instructoin parts put into instance vars

        // Advance to the next line
        lineNumber++;
        rawLine = inputFile.nextLine();
        cleanLine = cleanLine(rawLine);
        parse(); // Parse the line
    }

    // Basic getters below
    public String getCompMnemonic() {

        //DESCRIPTION: getter for comp part of C-instruction
        //PRECONDITION: cleanLine has been parsed (advance was called)
        //Call for C-instructions only (use getCommandType())
        //POSTCONDITION: returns mnemonic (ASM symbol) for comp part

        return compMnemonic;
    }
    public String getDestMnemonic() {

        //DESCRIPTION: getter for dest part of C-instruction
        //PRECONDITION: cleanLine has been parsed (advance was called)
        // Call for C-instructions only (use getCommandType())
        //POSTCONDITION: returns mnemonic (ASM symbol) for dest part

        return destMnemonic;
    }

    public int getLineNumber() {

        //DESCRIPTION: getter for lineNumber (Debugging)
        //PRECONDITION: n/a
        //POSTCONDITION: returns line number currently being processed from file

        return lineNumber;
    }

    public String getRawLine() {

        //DESCRIPTION: getter for rawLine from file (Debugging)
        //PRECONDITION: advance() was called to put value from file in here
        //POSTCONDITION: returns string of current original line from file


        return rawLine;
    }


    public String getJumpMnemonic() {

        //PRECONDITION: cleanLine has been parsed (advance was called)
        // Call for C-instructions only (use getCommandType())
        //POSTCONDITION: returns mnemonic (ASM symbol) for jump part

        return jumpMnemonic;
    }

    public Command getCommandType() {

        //DESCRIPTION: getter for command type
        //PRECONDITION: cleanLine has been parsed (advance was called)
        //POSTCONDITION: returns Command for type (N/A/C/L)

        return commandType;
    }

    public String getSymbol() {

        //DESCRIPTION: getter for command type
        //PRECONDITION: cleanLine has been parsed (advance was called)
        // Call for labels only (use getCommandType())
        //POSTCONDITION: returns string for symbol name

        return symbol;
    }
}