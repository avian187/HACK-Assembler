/*
    AUTHOR: Ian Utnehmer, MiraCosta College
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import static java.lang.System.exit;

/*
    * This is the main class for the Assembler. It is responsible for the following:
    * 1. Initialize the symbol table
    * 2. Iterate through the file twice:
    *  a. First pass: Add all labels to the symbol table
    * b. Second pass: Translate all commands to binary
    * 3. Write the binary to the output file
    * 4. Print a success message
    * 5. Handle errors
    * AUTHOR: Ian Utnehmer
 */
public class Assembler {
    private static SymbolTable symbolTable; // Initialize the symbolTable global variable
    public static void main(String[] args) {
        /*
            DESCRIPTION: Main method. Initializes all variables and gets the gears turning.
            PRECONDITION: N/A
            POST-CONDITION: The program will have run successfully.
         */
        // Set file name
        //CInstructionMapper test = new CInstructionMapper();
        //System.out.println(test.decimalToBinary(23));
        //exit(0);

        String inputFileName = "C:\\Users\\protr\\IdeaProjects\\ASSEMBLER\\Rect.asm", outputFileName = "C:\\Users\\protr\\IdeaProjects\\ASSEMBLER\\Rect.hack";
        // Check if file name is valid / file is valid
        symbolTable = new SymbolTable();
        try {
            PrintWriter outputFile = new PrintWriter(new FileOutputStream(outputFileName));
            firstPass(inputFileName);
            secondPass(inputFileName, outputFile);
            outputFile.close();
            System.out.println("Compiled to " + outputFileName + " successfully!");
        } catch (FileNotFoundException ex) {
            // handleError will by default print the error message an exit with a failure indication.
            handleError("Could not open output file " + outputFileName +
                    ". Run program again, make sure you have write permissions, etc.");
        }
    }

    // Handle first pass
    private static void firstPass(String inputFileName) {

        /*
            DESCRIPTION:    Parses line depending on instruction type. The first pass is responsible for adding all labels to the symbol table.
            PRECONDITION:   advance() called so cleanLine has value
            POST-CONDITION: appropriate parts (instance vars) of instruction filled
         */
        String symbol;
        int romAddress = 0; // ROM addressing initialized to 0
        try {
            Parser parser = new Parser(inputFileName); // Create a new parser at the given file.
            while (parser.hasMoreCommands()) { // Loop through the file
                parser.advance(); // Next line
                Command commandType = parser.getCommandType(); // Get the command type
                switch (commandType) { // Switch on the command type
                    case L_COMMAND: // If the command is a label
                        symbol = parser.getSymbol(); // Get the symbol
                        if (!symbolTable.contains(symbol)) { // If the symbol is not in the symbol table
                            if (!symbolTable.addEntry(symbol, Integer.toString(romAddress))) {
                                handleError("Invalid symbol.");
                                continue;
                            }
                        }
                        break;
                    case NO_COMMAND: // If the command is not a command
                        // Go to the next line
                        break;
                    case C_COMMAND, A_COMMAND: // If the command is a C-Command
                        romAddress++;
                        break;
                }
            }
        } catch (FileNotFoundException e) { // If the file is not found
            e.printStackTrace(); // Print the stack trace
        } catch (Exception e) { // If there is an exception
            throw new RuntimeException(e); // Throw a new runtime exception
        }
    }

    // Handle second pass
    private static void secondPass(String inputFileName, PrintWriter outputFile) {

        /*
            DESCRIPTION:    Parses line depending on instruction type. The second pass is responsible for translating all commands to binary.
            PRECONDITION:   First pass has been completed, advance() called so cleanLine has value
            POST-CONDITION: appropriate parts (instance vars) of instruction filled
         */

        String symbol;
        CInstructionMapper Mapper = new CInstructionMapper(); // Initialize the maps
        int ramAddress = 16; // ramAddress = 16
        try {
            Parser parser = new Parser(inputFileName); // Create a new parser at the given file.
            while (parser.hasMoreCommands()) { // Loop through the file
                parser.advance(); // Next line
                Command commandType = parser.getCommandType(); // Get the command type
                switch (commandType) {
                    case A_COMMAND: // If the command is an A-Command
                        symbol = parser.getSymbol(); // Store current A-Command symbol
                        if (symbolTable.contains(symbol)) { // If symbol is already in symbol table, replace it with its address.
                            symbol = symbolTable.getAddress(symbol);
                        } else if (SymbolTable.validName(symbol)) { // If symbol is valid, add to symbol table and increment ROM addressing.
                            symbolTable.addEntry(symbol, Integer.toString(ramAddress));
                            symbol = Integer.toString(ramAddress);
                        } else {
                            // Not a symbol - Is a constant value.
                            symbol = Integer.toString(Integer.parseInt(symbol));
                        }
                        // Write to file the binary representation of the A-Command.
                        outputFile.println(Mapper.decimalToBinary(Integer.parseInt(symbol)));
                        break;
                    case C_COMMAND:
                        // Get the binary representation of the C-Command
                        String comp = Mapper.comp(parser.getCompMnemonic()); // comp;
                        String dest = Mapper.dest(parser.getDestMnemonic()); // dest;
                        String jump = Mapper.jump(parser.getJumpMnemonic()); // jump;
                        // Write 16-bit binary representation to file
                        outputFile.println("111" + comp + dest + jump);
                        break;
                    case L_COMMAND, NO_COMMAND:
                        // Ignore labels or no commands.
                        break;
                }
            }
        } catch (FileNotFoundException e) { // If the file is not found
            e.printStackTrace(); // Print the stack trace
        } catch (Exception e) { // If there is an exception
            throw new RuntimeException(e); // Throw a new runtime exception
        }
    }

    private static void handleError(String message) {

        /*
            DESCRIPTION:    Manages errors for debugging.
            PRECONDITION:   An error must have occurred.
            POST-CONDITION: The error message will be printed and the program will exit with a failure indication.
         */

        System.out.println(message); // Print the error message
    }


}