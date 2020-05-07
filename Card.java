package flashcards;

import java.io.Serializable;

public class Card implements Serializable {

    private String term;
    private String definition;
    private int mistake;

    public Card(String term, String definition, int mistake) {
        this.term = term;
        this.definition = definition;
        this.mistake = mistake;
    }

    public Card(String term, String definition) {
        this(term, definition, 0);
    }

    public Card(String term) {
        this(term, "", 0);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        prime += (term == null ? 0 : term.hashCode());
        prime += (definition == null ? 0 : definition.hashCode());

        return prime;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Card) {
            return term.equals(((Card) o).getTerm()) || definition.equals(((Card) o).getDefinition());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Term:" + term + "\nDefinition:" + definition + "\nMistake:" + mistake;
    }

    public void resetMistake() {
        mistake = 0;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getMistake() {
        return mistake;
    }

    public void setMistake(int mistake) {
        this.mistake = mistake;
    }
}