@SuppressWarnings({"WeakerAccess", "BooleanMethodIsAlwaysInverted"})
public class TextChecker {
    public static boolean containsSpecialSigns(String text){
        return text.matches(".*[;\"',.].*");
    }

    public static boolean isDouble(String text){
        return text.matches("[0-9]+(\\.[0-9]+)?");
    }

    public static boolean noWhiteCharactersOrQuotes(String text){
        return text.matches("[^ \n'\"]*");
    }
}
