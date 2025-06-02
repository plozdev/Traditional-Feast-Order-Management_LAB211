package tools;

import java.util.Date;
import java.util.Scanner;

public class Inputter {

    private Scanner scanner;
    public Inputter(Scanner scanner) {
        this.scanner = scanner;
    }
    public String getString (String msg, String pattern, String errorMsg, boolean allowEmpty) {
        String input;
        while (true) {
            System.out.print(msg);
            input = scanner.nextLine().trim();
            if (input.isEmpty() && allowEmpty)
                return input;

            if (pattern == null || pattern.isEmpty() || Acceptable.isValid(input, pattern)) {
                return input;
            }
            if (errorMsg != null) System.out.println(errorMsg);
        }
    }
    public String getString(String msg, String pattern, String errorMsg) {
        return getString(msg, pattern, errorMsg, false); // Mặc định không cho phép null/empty
    }

    public int getInt(String msg, String errorRangeMsg, String errorMsg, int min, int max) {
        int number;
        while (true) {
            String input = getString(msg, Acceptable.INTEGER_VALID, errorMsg);
            number = Integer.parseInt(input);
            if (number >= min && number <= max) {
                return number;
            }
            if (errorRangeMsg != null) System.out.println(errorRangeMsg);
        }
    }


    public Date getDate(String msg, String errorMsg, String errorMessageInvalidDate) {
        Date date;
        while (true) {
            String input = getString(msg, Acceptable.DATE_VALID, errorMsg);
            date = DateUtils.parseDate(input); // Sử dụng DateUtils để parse
            if (date != null) { // DateUtils.parseDate có thể trả về null nếu có lỗi parse
                return date;
            }
            // DateUtils.parseDate đã in lỗi format, ở đây có thể in lỗi logic nếu cần
            if(errorMessageInvalidDate != null) System.out.println(errorMessageInvalidDate);

        }
    }

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
