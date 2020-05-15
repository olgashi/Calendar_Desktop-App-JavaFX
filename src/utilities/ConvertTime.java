package utilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

//TODO refactor
public class ConvertTime {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

    public static Timestamp convertToLocalTime (String stringTime) {
        LocalDateTime utcTime = LocalDateTime.parse(stringTime, dtf);
        ZonedDateTime zdtTime = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localTime = zdtTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime ldtTime = localTime.toLocalDateTime();

        Timestamp myTimestamp = Timestamp.valueOf(ldtTime);
        return myTimestamp;
//        return ldtTime;
    }

    public static Timestamp convertToUTCTime (LocalDateTime tme) {
        LocalDateTime localTime = tme;
        ZonedDateTime zdtTime = localTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcTime = zdtTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime ldtTime = utcTime.toLocalDateTime();
        Timestamp myTimestamp = Timestamp.valueOf(ldtTime);
        return myTimestamp;
//        return ldtTime;
    }
}
