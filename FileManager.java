package flashcards;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    final static String PATH = "";

    public void exportLogs(List<Logs> logs, String filename) throws IOException {

        File file = new File(PATH + filename);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            logs.forEach(printWriter::println);
        }
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