package utilities;

import model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AuditLog {
private final static String logFileName = "audit-log.txt";
    public static void createFile(){
        try {
            File logingFile = new File(logFileName);
            logingFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLog(User user) {
            try {
                String userName = user.getUserName();
                String userId = user.getUserId();
                LocalDateTime currentTimeDate = LocalDateTime.now();
                Timestamp timeLogged = ConvertTime.convertToUTCTime(currentTimeDate);
                FileWriter fileLogger = new FileWriter(logFileName, true);
                fileLogger.write("User name: " + userName + ", user id: " + userId + ", logged in " + timeLogged + "\n");
                fileLogger.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
