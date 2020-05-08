package flashcards;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logs {

    private final String date;
    private final String message;

    public Logs(String message) {
        this.date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        this.message = message;
    }

    @Override
    public String toString() {
        return "Date:" + date + " Message:" + message;
    }
}