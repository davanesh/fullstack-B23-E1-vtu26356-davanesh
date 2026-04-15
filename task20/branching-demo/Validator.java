public class Validator {
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    public static boolean isPositive(int number) {
        return number > 0;
    }
}
