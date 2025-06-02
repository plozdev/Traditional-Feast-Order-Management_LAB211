package tools;

public interface Acceptable {
    String CUS_ID_VALID = "^[cgkCGK]\\d{4}$";
    String MENU_ID_VALID = "[Pp][Ww]\\d{3}";
    String NAME_VALID = ".{2,25}";
    String PHONE_VALID = "^0[98753]\\d{8}$";
    String EMAIL_VALID = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    String DATE_VALID = "^\\d{2}/\\d{2}/\\d{4}$";
    String ORDER_CODE_VALID = "ORD-[A-Z0-9]{8}";
    String POSITIVE_DOUBLE_VALID = "\\d+(\\.\\d+)?";
    String INTEGER_VALID = "0|[1-9]\\d*";
    String STRING_NOT_EMPTY_VALID = ".+";

    static boolean isValid (String data, String pattern) {
        return data.matches(pattern);
    }
}
