package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public static String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMATTER.format(date);
    }

    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            return DATE_FORMATTER.parse(dateString.trim());
        } catch (ParseException e) {
            System.err.println("Invalid date format. Please use dd/MM/yyyy.");
            return null;
        }
    }
}