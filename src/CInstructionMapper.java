/*
    AUTHOR: Ian Utnehmer, MiraCosta College
 */


import java.util.HashMap;

public class CInstructionMapper {
    HashMap<String, String> compCodes = new HashMap<String, String>();
    HashMap<String, String> jumpCodes = new HashMap<String, String>();
    HashMap<String, String> destCodes = new HashMap<String, String>();

    public CInstructionMapper() {

        /*
            DESCRIPTION:    initializes hashmaps with binary codes for easy lookup
            PRECONDITION:   comp codes = 7 bits (includes a), dest/jump codes = 3 bits
            POST-CONDITION: all hashmaps have lookups for valid codes
         */

        // build jumpCode tables
        jumpCodes.put("NULL", "000");
        jumpCodes.put("null", "000");
        jumpCodes.put("JGT", "001");
        jumpCodes.put("JEQ", "010");
        jumpCodes.put("JGE", "011");
        jumpCodes.put("JLT", "100");
        jumpCodes.put("JNE", "101");
        jumpCodes.put("JLE", "110");
        jumpCodes.put("JMP", "111");
        // build compCode tables
        compCodes.put("0",   "0101010");
        compCodes.put("1",   "0111111");
        compCodes.put("-1",  "0111010");
        compCodes.put("D",   "0001100");
        compCodes.put("A",   "0110000");
        compCodes.put("!D",  "0001101");
        compCodes.put("!A",  "0110001");
        compCodes.put("-D",  "0001111");
        compCodes.put("-A",  "0110011");
        compCodes.put("D+1", "0011111");
        compCodes.put("A+1", "0110111");
        compCodes.put("D-1", "0001110");
        compCodes.put("A-1", "0110010");
        compCodes.put("D+A", "0000010");
        compCodes.put("A+D", "0000010");
        compCodes.put("D-A", "0010011");
        compCodes.put("A-D", "0000111");
        compCodes.put("D&A", "0000000");
        compCodes.put("A&D", "0000000");
        compCodes.put("D|A", "0010101");
        compCodes.put("A|D", "0010101");
        compCodes.put("M",   "1110000");
        compCodes.put("!M",  "1110001");
        compCodes.put("-M",  "1110011");
        compCodes.put("M+1", "1110111");
        compCodes.put("M-1", "1110010");
        compCodes.put("D+M", "1000010");
        compCodes.put("M+D", "1000010");
        compCodes.put("D-M", "1010011");
        compCodes.put("M-D", "1000111");
        compCodes.put("D&M", "1000000");
        compCodes.put("M&D", "1000000");
        compCodes.put("D|M", "1010101");
        compCodes.put("M|D", "1010101");
        // build destCode tables
        destCodes.put("NULL", "000");
        destCodes.put("null", "000");
        destCodes.put("M", "001");
        destCodes.put("D", "010");
        destCodes.put("MD", "011");
        destCodes.put("A", "100");
        destCodes.put("AM", "101");
        destCodes.put("AD", "110");
        destCodes.put("AMD", "111");

    }

    // Below are basic getters for the maps.

    public String comp(String mnemonic) {

        /*
            DESCRIPTION:    converts to string of bits (7) for given mnemonic
            PRECONDITION:   hashmaps are built with valid values
            POST-CONDITION: returns string of bits if valid, else returns null
         */

        return compCodes.get(mnemonic);
    }

    public String dest(String mnemonic) {

        /*
            DESCRIPTION:    converts to string of bits (3) for given mnemonic
            PRECONDITION:   hashmaps are built with valid values
            POST-CONDITION: returns string of bits if valid, else returns null
         */

        return destCodes.get(mnemonic);
    }

    public String jump(String mnemonic) {

        /*
            DESCRIPTION:    converts to string of bits (3) for given mnemonic
            PRECONDITION:   hashmaps are built with valid values
            POST-CONDITION: returns string of bits if valid, else returns null
         */

        return jumpCodes.get(mnemonic);
    }

    // Convert the decimal number to a 16-bit binary number.
    public static String decimalToBinary(int decimal) {

        /*
            DESCRIPTION:    converts integer from decimal notation to binary notation
            PRECONDITION:   number is valid size for architecture, non-negative
            POST-CONDITION: returns 16-bit string of binary digits (first char is MSB
         */

        // Must be within range
        if (decimal < 0 || decimal >= 32768) {
            throw new IllegalArgumentException("Input must be a non-negative integer less than 2^15.");
        }
        // initial binary value
        String binary = Integer.toBinaryString(decimal);
        // 0 padding
        int length = binary.length();
        if (length < 16) { // if length is less than 16
            int zerosToPrepend = 16 - length; // calculate the number of zeros to prepend
            StringBuilder binaryBuilder = new StringBuilder();
            for (int i = 0; i < zerosToPrepend; i++) { // prepend zeros
                binaryBuilder.append('0');
            }
            binaryBuilder.append(binary); // append the binary value
            binary = binaryBuilder.toString();
        }
        return binary; // return the binary value
    }









}
