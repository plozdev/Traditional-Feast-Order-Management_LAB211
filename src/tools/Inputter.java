package tools;

import java.util.Date;
import java.util.Scanner;

/**
 * Class for handling various types of user input with validation
 */
public class Inputter {

    private Scanner scanner;

    /**
     * Constructs an Inputter with Scanner
     *
     * @param scanner The scanner object
     */
    public Inputter(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prompt the user for a string input and validates it against a regex pattern
     * @param msg - the message to display
     * @param pattern - the regex pattern to validate the input
     * @param errorMsg - the error message to display if the input is invalid
     * @param allowEmpty - if true -> allows empty input (for update)
     * @return the validate string input
     */
    public String getString (String msg, String pattern, String errorMsg, boolean allowEmpty) {
        String input;
        while (true) {
            System.out.print(msg);
            input = scanner.nextLine().trim();
            if (input.isEmpty() && allowEmpty)
                return input; // Return empty string if allowed

            if (pattern == null || pattern.isEmpty() || Acceptable.isValid(input, pattern)) {
                return input; // Return valid input
            }
            if (errorMsg != null) System.out.println(errorMsg); // Display error if input doesn't match pattern
        }
    }
    /**
     * Prompts the user for a non-empty string input and validates it against a regex pattern.
     * By default, does not allow empty input.
     *
     * @param msg         The message to display to the user.
     * @param pattern     The regex pattern to validate the input against.
     * @param errorMsg    The error message to display if the input is invalid.
     * @return The validated non-empty string input.
     */
    public String getString(String msg, String pattern, String errorMsg) {
        return getString(msg, pattern, errorMsg, false); // Default: do not allow null/empty
    }

    /**
     * Prompts the user for an integer input within a specified range.
     *
     * @param msg            The message to display to the user.
     * @param errorRangeMsg  The error message to display if the number is out of range.
     * @param errorMsg       The error message to display if the input is not a valid integer.
     * @param min            The minimum acceptable integer value.
     * @param max            The maximum acceptable integer value.
     * @return The validated integer input.
     */
    public int getInt(String msg, String errorRangeMsg, String errorMsg, int min, int max) {
        int number;
        while (true) {
            String input = getString(msg, Acceptable.INTEGER_VALID, errorMsg); // Get a string that matches integer pattern
            number = Integer.parseInt(input); // Parse the valid string to an integer
            if (number >= min && number <= max) {
                return number; // Return if within range
            }
            if (errorRangeMsg != null) System.out.println(errorRangeMsg);  // Display range error
        }
    }

    /**
     * Prompts the user for a date input and validates its format (dd/MM/yyyy).
     *
     * @param msg                      The message to display to the user.
     * @param errorMsg                 The error message to display if the input format is initially invalid (before parsing).
     * @param errorMessageInvalidDate  The error message to display if the parsed date is null (e.g., parse error from DateUtils).
     * @return The validated Date object.
     */
    public Date getDate(String msg, String errorMsg, String errorMessageInvalidDate) {
        Date date;
        while (true) {
            String input = getString(msg, Acceptable.DATE_VALID, errorMsg); // Get a string that matches date pattern
            date = DateUtils.parseDate(input); // Use DateUtils to parse the string
            if (date != null) { // DateUtils.parseDate might return null if parsing fails (e.g., invalid day/month)
                return date;
            }
            // DateUtils.parseDate already prints a format error message.
            // This is an additional message for logical date errors if needed.
            if(errorMessageInvalidDate != null) System.out.println(errorMessageInvalidDate);
        }
    }


    /**
     * Prompts the user for a yes/no confirmation.
     * Accepts "yes", "y", "no", or "n" (case-insensitive).
     *
     * @param msg The message to display to the user.
     * @return true if the user answers yes, false if the user answers no.
     */
    public boolean getYesNo(String msg) {
        String input;
        while (true) {
            System.out.print(msg + " (yes/no or y/n): ");
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            }
            if (input.equals("no") || input.equals("n")) {
                return false;
            }
            System.out.println("Invalid input. Please enter 'yes', 'no', 'y', or 'n'.");
        }
    }

}
