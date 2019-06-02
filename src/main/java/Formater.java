import java.sql.Timestamp;


@SuppressWarnings({"WeakerAccess", "deprecation"})
public class Formater {
    public static String timeFormat(Timestamp time){
        return  time.toLocaleString().substring(12, 17);
    }

    public static String dateFormat(Timestamp time){
        return  time.toLocaleString().substring(0, 10);
    }

    public static String fullDateFormat(Timestamp time){
        return dateFormat(time) + " " + timeFormat(time);
    }
}
