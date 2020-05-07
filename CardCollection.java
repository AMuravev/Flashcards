package flashcards;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CardCollection {

    private final Map<String, String> collection = new HashMap<>();
    private String lastTerm;

    public void put(String key, String value) {
        collection.put(key, value);
        lastTerm = key;
    }

    public String get(String key) {
        return collection.get(key);
    }

    public boolean containsKey(String key) {
        return collection.containsKey(key);
    }

    public boolean containsValue(String value) {
        return collection.containsValue(value);
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return collection.entrySet();
    }

    public String findTermByDefinition(String definition) {
        for (Map.Entry<String, String> pair : collection.entrySet()) {
            if (definition.equals(pair.getValue())) {
                return pair.getKey();
            }
        }
        return null;
    }

    public int size() {
        return collection.size();
    }

    public Set<String> keySet() {
        return collection.keySet();
    }

    public void remove(String key) {
        collection.remove(key);
    }

    public String getLastTerm() {
        return lastTerm;
    }

    public void setLastTerm(String lastTerm) {
        this.lastTerm = lastTerm;
    }

    public Map<String, String> getCollection() {
        return collection;
    }
}
