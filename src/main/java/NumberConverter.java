@SuppressWarnings("WeakerAccess")
public class NumberConverter {
    public static String convertNumber(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "-th";
            default:
                return i + "-" + suffixes[i % 10];
        }
    }
}
