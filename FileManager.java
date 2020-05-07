package flashcards;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class FileManager {

    final static String PATH = "";

    public void exportLogs(Logs logs, String filename) throws IOException {

        Properties properties = new Properties();

        properties.putAll(logs.getLogs());

        properties.store(new FileOutputStream(PATH + filename), null);
    }

    public void exportFile(List<Card> cardCollection, String filename) throws IOException {

        FileOutputStream f = new FileOutputStream(new File(PATH + filename));
        ObjectOutputStream o = new ObjectOutputStream(f);

        for (Card card : cardCollection) {
            o.writeObject(card);
        }
    }

    public List<Card> importFile(String filename) throws IOException, ClassNotFoundException {

        List<Card> _cardCollection = new ArrayList<>();
        FileInputStream fi = new FileInputStream(new File(PATH + filename));
        ObjectInputStream oi = new ObjectInputStream(fi);

        try {

            for (; ; ) {
                _cardCollection.add((Card) oi.readObject());
            }

        } catch (EOFException e) {
            return _cardCollection;
        }
    }
}