package flashcards;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Logs {

    private final TreeMap<String, String> logs;

    public Logs() {
        this.logs = new TreeMap<>();
    }

    public String write(String string) {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        logs.put(strDate, string);
        return string;
    }

    public TreeMap<String, String> getLogs() {
        return logs;
    }
}