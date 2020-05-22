package utilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static Timestamp convertToLocalTime (String stringTime) {
        LocalDateTime convertToUTC = LocalDateTime.parse(stringTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Timestamp.valueOf(convertToUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
    }

    public static Timestamp convertToUTCTime (LocalDateTime tme) {
        return Timestamp.valueOf(tme.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }
}
