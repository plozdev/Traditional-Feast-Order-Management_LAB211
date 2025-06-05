package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date formatting and parsing.
 * Uses "dd/MM/yyyy" as the standard date format.
 */
public class DateUtils {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    static {
        DATE_FORMATTER.setLenient(false); // Ensure strict date parsing
    }

    /**
     * Formats a Date object into a string (dd/MM/yyyy).
     *
     * @param date The Date object to format.
     * @return The formatted date string, or an empty string if the date is null.
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMATTER.format(date);
    }

    /**
     * Parses a date string (expected format dd/MM/yyyy) into a Date object.
     *
     * @param dateString The date string to parse.
     * @return The parsed Date object, or null if the string is null, empty, or in an invalid format.
     * An error message is printed to System.err if parsing fails.
     */
    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return DATE_FORMATTER.parse(dateString.trim());
        } catch (ParseException e) {
            System.err.println("Invalid date format for input \"" + dateString + "\". Please use dd/MM/yyyy.");
            return null;
        }
    }
}