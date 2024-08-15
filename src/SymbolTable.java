/*
    AUTHOR: Ian Utnehmer, MiraCosta College
 */

import java.util.HashMap;

public class SymbolTable {
    private static final String INITIAL_VALID_CHARS = "[a-zA-Z_$:][a-zA-Z0-9_]*"; // Regex for valid identifier
    private static final String ALL_VALID_CHARS = INITIAL_VALID_CHARS + "[$][:][0-9]*"; // Regex for valid identifier
    private HashMap<String, String> symbolTable = new HashMap<String, String>();
    public SymbolTable() {

        //DESCRIPTION: initializes hashmaps with predefined symbols
        //PRECONDITION: follows symbols/values from book/appendix
        //POSTCONDITION: all hashmap values have valid address integer

        // build symbol table
        symbolTable.put("R0", "0");
        symbolTable.put("R1", "1");
        symbolTable.put("R2", "2");
        symbolTable.put("R3", "3");
        symbolTable.put("R4", "4");
        symbolTable.put("R5", "5");
        symbolTable.put("R6", "6");
        symbolTable.put("R7", "7");
        symbolTable.put("R8", "8");
        symbolTable.put("R9", "9");
        symbolTable.put("R10", "10");
        symbolTable.put("R11", "11");
        symbolTable.put("R12", "12");
        symbolTable.put("R13", "13");
        symbolTable.put("R14", "14");
        symbolTable.put("R15", "15");
        symbolTable.put("SP", "0");
        symbolTable.put("LCL", "1");
        symbolTable.put("ARG", "2");
        symbolTable.put("THIS", "3");
        symbolTable.put("THAT", "4");
        symbolTable.put("SCREEN", "16384");
        symbolTable.put("KBD", "24576");
    }
    public static boolean validName(String symbol) {

        //DESCRIPTION: checks validity of identifiers for assembly code symbols
        //PRECONDITION: start with letters or “_.$:” only, numbers allowed after
        //POSTCONDITION: returns true if valid identifier, false otherwise

        // Check if the symbol is a valid identifier
        if (symbol != null || symbol.length() != 0)
            return symbol.matches(INITIAL_VALID_CHARS) || symbol.matches(ALL_VALID_CHARS);
        return false; // It's not a valid identifier
    }

    public boolean addEntry(String key, String value) {

        //DESCRIPTION: adds a new pair of symbol/address to hashmap
        //PRECONDITION: symbol/address pair not in hashmap (check contains() 1st)
        //POSTCONDITION: adds pair, returns true if added, false if illegal name


        // Add the key and value to the symbol table. Return true to indicate success.
        if (symbolTable.put(key, value) != null) return true;
        return false;
    }

    public boolean contains(String key) {

        //DESCRIPTION: returns boolean of whether hahsmap has symbol or not
        //PRECONDITION: table has been initialized
        //POSTCONDITION: returns boolean if arg is in table or not

        // Check if the symbol table contains the key
        return symbolTable.containsKey(key);
    }

    public String getAddress(String val) {

        //DESCRIPTION: returns address in hashmap of given symbol
        //PRECONDITION: symbol is in hashmap (check w/ contains() first)
        //POSTCONDITION: returns address associated with symbol in hashmap


        // Get the address of the symbol
        return symbolTable.get(val);
    }
}
