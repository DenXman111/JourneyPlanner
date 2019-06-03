import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@SuppressWarnings({"WeakerAccess"})
public class Formatter {
    public static String timeFormat(Timestamp time){
        Date date = new Date(time.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }

    public static String dateFormat(Timestamp time){
        Date date = new Date(time.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }

    public static String fullDateFormat(Timestamp time){
        return dateFormat(time) + ": " + timeFormat(time);
    }
}
