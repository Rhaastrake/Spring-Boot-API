package com.michelegarofalo.springbootapi.fiscalcode;
// ############################################
// IMPORTS
// ############################################

import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.web.bind.annotation.*;

//
// THIS CLASS CAN BE USED TO CALCULATE AN ITALIAN FISCAL CODE
//
@RestController
@RequestMapping ("fiscalCode")

@CrossOrigin(origins = "*")
public class FiscalCode {

    // ############################################
    // CONSTANTS AND MAPS
    // ############################################

    private static final String VOWELS = "AEIOU";
    private static final String CITIES_COUNTRY_CODES_SOURCE = "https://raw.githubusercontent.com/Rhaastrake/Codice-Fiscale/master/Cities%20codes.txt";

    public static final Map <Integer,   Character>  MONTHS_LETTERS_VALUES =    new HashMap<>();
    public static final Map <Character, Integer>    EVEN_POS_LETTERS_VALUES =  new HashMap<>();
    public static final Map <Character, Integer>    ODD_POS_LETTERS_VALUES =   new HashMap<>();
    public static final Map <Integer,   Character>  CONTROL_LETTERS_VALUES =   new HashMap<>();
    public static final Map <String,    String>     CITIES_COUNTRY_CODES =     new HashMap<>();

    private static final char[] ACCENTED_LETTERS = {
            'À', 'Á', 'Ä', 'Â',
            'È', 'É', 'Ë', 'Ê',
            'Ì', 'Í', 'Ï', 'Î',
            'Ò', 'Ó', 'Ö', 'Ô',
            'Ù', 'Ú', 'Ü', 'Û'};
    private static final char[] ACCENTED_LETTERS_REPLACEMENT = {
            'A', 'A', 'A', 'A',
            'E', 'E', 'E', 'E',
            'I', 'I', 'I', 'I',
            'O', 'O', 'O', 'O',
            'U', 'U', 'U', 'U'};


    //
    // STATIC BLOCK THAT INITIALIZES ALL THE MAPS
    //
    static {
        // MONTHS MAP
        MONTHS_LETTERS_VALUES.put(1, 'A');
        MONTHS_LETTERS_VALUES.put(2, 'B');
        MONTHS_LETTERS_VALUES.put(3, 'C');
        MONTHS_LETTERS_VALUES.put(4, 'D');
        MONTHS_LETTERS_VALUES.put(5, 'E');
        MONTHS_LETTERS_VALUES.put(6, 'H');
        MONTHS_LETTERS_VALUES.put(7, 'L');
        MONTHS_LETTERS_VALUES.put(8, 'M');
        MONTHS_LETTERS_VALUES.put(9, 'P');
        MONTHS_LETTERS_VALUES.put(10, 'R');
        MONTHS_LETTERS_VALUES.put(11, 'S');
        MONTHS_LETTERS_VALUES.put(12, 'T');

        // EVEN LETTERS MAP
        EVEN_POS_LETTERS_VALUES.put('0', 1);
        EVEN_POS_LETTERS_VALUES.put('1', 0);
        EVEN_POS_LETTERS_VALUES.put('2', 5);
        EVEN_POS_LETTERS_VALUES.put('3', 7);
        EVEN_POS_LETTERS_VALUES.put('4', 9);
        EVEN_POS_LETTERS_VALUES.put('5', 13);
        EVEN_POS_LETTERS_VALUES.put('6', 15);
        EVEN_POS_LETTERS_VALUES.put('7', 17);
        EVEN_POS_LETTERS_VALUES.put('8', 19);
        EVEN_POS_LETTERS_VALUES.put('9', 21);
        EVEN_POS_LETTERS_VALUES.put('A', 1);
        EVEN_POS_LETTERS_VALUES.put('B', 0);
        EVEN_POS_LETTERS_VALUES.put('C', 5);
        EVEN_POS_LETTERS_VALUES.put('D', 7);
        EVEN_POS_LETTERS_VALUES.put('E', 9);
        EVEN_POS_LETTERS_VALUES.put('F', 13);
        EVEN_POS_LETTERS_VALUES.put('G', 15);
        EVEN_POS_LETTERS_VALUES.put('H', 17);
        EVEN_POS_LETTERS_VALUES.put('I', 19);
        EVEN_POS_LETTERS_VALUES.put('J', 21);
        EVEN_POS_LETTERS_VALUES.put('K', 2);
        EVEN_POS_LETTERS_VALUES.put('L', 4);
        EVEN_POS_LETTERS_VALUES.put('M', 18);
        EVEN_POS_LETTERS_VALUES.put('N', 20);
        EVEN_POS_LETTERS_VALUES.put('O', 11);
        EVEN_POS_LETTERS_VALUES.put('P', 3);
        EVEN_POS_LETTERS_VALUES.put('Q', 6);
        EVEN_POS_LETTERS_VALUES.put('R', 8);
        EVEN_POS_LETTERS_VALUES.put('S', 12);
        EVEN_POS_LETTERS_VALUES.put('T', 14);
        EVEN_POS_LETTERS_VALUES.put('U', 16);
        EVEN_POS_LETTERS_VALUES.put('V', 10);
        EVEN_POS_LETTERS_VALUES.put('W', 22);
        EVEN_POS_LETTERS_VALUES.put('X', 25);
        EVEN_POS_LETTERS_VALUES.put('Y', 24);
        EVEN_POS_LETTERS_VALUES.put('Z', 23);

        // ODD LETTERS MAP
        ODD_POS_LETTERS_VALUES.put('0', 0);
        ODD_POS_LETTERS_VALUES.put('1', 1);
        ODD_POS_LETTERS_VALUES.put('2', 2);
        ODD_POS_LETTERS_VALUES.put('3', 3);
        ODD_POS_LETTERS_VALUES.put('4', 4);
        ODD_POS_LETTERS_VALUES.put('5', 5);
        ODD_POS_LETTERS_VALUES.put('6', 6);
        ODD_POS_LETTERS_VALUES.put('7', 7);
        ODD_POS_LETTERS_VALUES.put('8', 8);
        ODD_POS_LETTERS_VALUES.put('9', 9);
        ODD_POS_LETTERS_VALUES.put('A', 0);
        ODD_POS_LETTERS_VALUES.put('B', 1);
        ODD_POS_LETTERS_VALUES.put('C', 2);
        ODD_POS_LETTERS_VALUES.put('D', 3);
        ODD_POS_LETTERS_VALUES.put('E', 4);
        ODD_POS_LETTERS_VALUES.put('F', 5);
        ODD_POS_LETTERS_VALUES.put('G', 6);
        ODD_POS_LETTERS_VALUES.put('H', 7);
        ODD_POS_LETTERS_VALUES.put('I', 8);
        ODD_POS_LETTERS_VALUES.put('J', 9);
        ODD_POS_LETTERS_VALUES.put('K', 10);
        ODD_POS_LETTERS_VALUES.put('L', 11);
        ODD_POS_LETTERS_VALUES.put('M', 12);
        ODD_POS_LETTERS_VALUES.put('N', 13);
        ODD_POS_LETTERS_VALUES.put('O', 14);
        ODD_POS_LETTERS_VALUES.put('P', 15);
        ODD_POS_LETTERS_VALUES.put('Q', 16);
        ODD_POS_LETTERS_VALUES.put('R', 17);
        ODD_POS_LETTERS_VALUES.put('S', 18);
        ODD_POS_LETTERS_VALUES.put('T', 19);
        ODD_POS_LETTERS_VALUES.put('U', 20);
        ODD_POS_LETTERS_VALUES.put('V', 21);
        ODD_POS_LETTERS_VALUES.put('W', 22);
        ODD_POS_LETTERS_VALUES.put('X', 23);
        ODD_POS_LETTERS_VALUES.put('Y', 24);
        ODD_POS_LETTERS_VALUES.put('Z', 25);

        // CONTROL LETTERS MAP
        CONTROL_LETTERS_VALUES.put(0, 'A');
        CONTROL_LETTERS_VALUES.put(1, 'B');
        CONTROL_LETTERS_VALUES.put(2, 'C');
        CONTROL_LETTERS_VALUES.put(3, 'D');
        CONTROL_LETTERS_VALUES.put(4, 'E');
        CONTROL_LETTERS_VALUES.put(5, 'F');
        CONTROL_LETTERS_VALUES.put(6, 'G');
        CONTROL_LETTERS_VALUES.put(7, 'H');
        CONTROL_LETTERS_VALUES.put(8, 'I');
        CONTROL_LETTERS_VALUES.put(9, 'J');
        CONTROL_LETTERS_VALUES.put(10, 'K');
        CONTROL_LETTERS_VALUES.put(11, 'L');
        CONTROL_LETTERS_VALUES.put(12, 'M');
        CONTROL_LETTERS_VALUES.put(13, 'N');
        CONTROL_LETTERS_VALUES.put(14, 'O');
        CONTROL_LETTERS_VALUES.put(15, 'P');
        CONTROL_LETTERS_VALUES.put(16, 'Q');
        CONTROL_LETTERS_VALUES.put(17, 'R');
        CONTROL_LETTERS_VALUES.put(18, 'S');
        CONTROL_LETTERS_VALUES.put(19, 'T');
        CONTROL_LETTERS_VALUES.put(20, 'U');
        CONTROL_LETTERS_VALUES.put(21, 'V');
        CONTROL_LETTERS_VALUES.put(22, 'W');
        CONTROL_LETTERS_VALUES.put(23, 'X');
        CONTROL_LETTERS_VALUES.put(24, 'Y');
        CONTROL_LETTERS_VALUES.put(25, 'Z');


        //
        // THESE INSTRUCTIONS INITIALIZE THE CITIES_COUNTRY_CODES MAP BY READING AN EXTERNAL FILE (CITIES_COUNTRY_CODES_SOURCE)
        //
        try (BufferedReader file = new BufferedReader(
                new InputStreamReader(
                        new URL(CITIES_COUNTRY_CODES_SOURCE)
                                .openStream()
                )
        )) {

            String line;
            while ((line = file.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    CITIES_COUNTRY_CODES.put(parts[1].toUpperCase(), parts[0]);
                }
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // ############################################
    // CONSTRUCTORS
    // ############################################

    //

    // ############################################
    // METHODS
    // ############################################

    //
    // THIS STATIC METHOD CAN BE USED TO CALCULATE THE CODE FISCAL CODE PROVIDING ALL THE INFO
    //
    @GetMapping ("calcFiscalCode")
    public static String calcFiscalCode(@RequestParam String surname, @RequestParam String name, @RequestParam char gender, @RequestParam int year, @RequestParam int month, @RequestParam int day, @RequestParam String city) {

        System.out.println(">> calcFiscalCode method called");

        String finalFiscalCode = "";

        finalFiscalCode += encodeSurname(surname);
        finalFiscalCode += encodeName(name);
        finalFiscalCode += encodeYear(year);
        finalFiscalCode += encodeMonth(month);
        finalFiscalCode += encodeDay(day, gender);
        finalFiscalCode += encodeCity(city);
        finalFiscalCode += encodeControlLetter(finalFiscalCode);

        return finalFiscalCode;
    }

    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE SURNAME RETURNING A STRING OF 3
    //
    @GetMapping ("encodeSurname")
    public static String encodeSurname(@RequestParam String surname) {

        System.out.println(">> encodeSurname method called");

        surname = normalize(surname);

        String consonants = extractConsonants(surname);
        String vowels = extractVowels(surname);

        if (consonants.length() >= 3) {
            return consonants.substring(0, 3);
        }

        if (consonants.length() == 2) {
            return consonants + vowels.charAt(0);
        }

        if (vowels.length() >= 2) {
            return consonants + vowels.substring(0, 2);
        }

        return consonants + vowels + 'X';
    }
    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE NAME RETURNING A STRING OF 3
    //
    @GetMapping ("encodeName")
    public static String encodeName(@RequestParam String name) {

        System.out.println(">> encodeName method called");

        name = normalize(name);

        String consonants = extractConsonants(name);
        String vowels = extractVowels(name);

        if (consonants.length() >= 4) {
            return consonants.charAt(0) + consonants.substring(2, 4);
        }

        if (consonants.length() == 3) {
            return consonants;
        }

        if (consonants.length() == 2) {
            return consonants + vowels.charAt(0);
        }

        if (vowels.length() >= 2) {
            return consonants + vowels.substring(0, 2);
        }

        return consonants + vowels + 'X';
    }

    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE YEAR (PASSING AN INT) RETURNING A STRING OF 2
    //
    @GetMapping ("encodeYear")
    public static String encodeYear(@RequestParam int year) {

        System.out.println(">> encodeYear method called");

        return Integer.toString(year).substring(2, 4);
    }

    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE YEAR (PASSING A STRING) RETURNING A STRING OF 2
    //
    public static String encodeYear(String year) {
        return year.substring(2, 4);
    }

    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE MONTH (PASSING AN INT) RETURNING A CHAR
    //
    @GetMapping ("encodeMonth")
    public static char encodeMonth(@RequestParam int month) {

        System.out.println(">> encodeMonth method called");

        return MONTHS_LETTERS_VALUES.get(month);
    }

    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE MONTH (PASSING A STRING) RETURNING A CHAR
    //
    public static char encodeMonth(String month) {
        return MONTHS_LETTERS_VALUES.get(Integer.parseInt(month));
    }

    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE DAY (PASSING AN INT) RETURNING A STRING OF 2
    //
    @GetMapping ("encodeDay")
    public static String encodeDay(@RequestParam int day, @RequestParam char gender) {

        System.out.println(">> encodeDay method called");

        gender = Character.toUpperCase(gender);
        if (gender == 'M') {
            return day < 10 ? "0" + day : Integer.toString(day);
        }
        return Integer.toString(day + 40);
    }

    //
    // THIS STATIC METHOD CAN BE USED TO ENCODE THE DAY (PASSING A STRING) RETURNING A STRING OF 2
    //
    public static String encodeDay(String day, char gender) {
        return encodeDay(Integer.parseInt(day), gender);
    }

    //
    // THIS STATIC METHOD CAN BE USED TO FIND THE CITY CODE (PASSING AN INT) RETURNING A STRING OF 2
    //

    @GetMapping ("encodeCity")
    public static String encodeCity(@RequestParam String city) {

        System.out.println(">> encodeCity method called");

        city = normalize(city);

        String code = CITIES_COUNTRY_CODES.get(city);

        if (code != null) {
            return code;
        } else {
            throw new IllegalArgumentException("City not found: " + city);
        }
    }

    //
    // THIS STATIC METHOD CAN BE USED TO CALCULATE THE VALUE OF THE FINAL CONTROL LETTER
    //
    @GetMapping ("encodeControlLetter")
    public static char encodeControlLetter(@RequestParam String partialFiscalCode) {

        System.out.println(">> encodeControlLetter method called");

        partialFiscalCode = partialFiscalCode.toUpperCase();
        //

        short lettersValuesSum = 0;

        for (byte i = 0; i < partialFiscalCode.length(); ++i) {
            if (i % 2 == 0) {
                lettersValuesSum = (short)(lettersValuesSum + EVEN_POS_LETTERS_VALUES.get(partialFiscalCode.charAt(i)));
            } else {
                lettersValuesSum = (short)(lettersValuesSum + ODD_POS_LETTERS_VALUES.get(partialFiscalCode.charAt(i)));
            }
        }

        return CONTROL_LETTERS_VALUES.get(lettersValuesSum % 26);
    }

    //
    // THIS METHOD IS INTERNALLY USED TO EXTRACT ALL THE CONSONANTS
    //
    private static String extractConsonants(String input) {

        input = normalize(input);

        String consonants = "";

        for (byte i = 0; i < input.length() && consonants.length() < 4; ++i) {
            if (!VOWELS.contains(input.substring(i, i + 1))) {
                consonants += input.charAt(i);
            }
        }

        return consonants;
    }

    //
    // THIS METHOD IS INTERNALLY USED TO EXTRACT ALL THE VOWELS
    //
    private static String extractVowels(String input) {

        input = normalize(input);

        String vowels = "";

        for (byte i = 0; i < input.length() && vowels.length() < 2; ++i) {
            if (VOWELS.contains(input.substring(i, i + 1))) {
                vowels += input.charAt(i);
            }
        }

        return vowels;
    }

    //
    // THIS METHOD IS INTERNALLY USED TO NORMALIZE ANY INPUT REMOVING ANY ' OR SPACE AND RETURNING TO UPPERCASE
    //
    private static String normalize(String input) {

        input = input.toUpperCase();

        input = input.replace("'", "");
        input = input.replace(" ", "");

        for (byte i = 0; i < input.length(); ++i) {
            for (byte j = 0; j < ACCENTED_LETTERS.length; ++j) {
                if (input.charAt(i) == ACCENTED_LETTERS[j]) {
                    input = input.replace(input.charAt(i), ACCENTED_LETTERS_REPLACEMENT[j]);
                }
            }
        }

        return input;
    }
}