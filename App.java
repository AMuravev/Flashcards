package flashcards;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    private State state;

    private final List<Card> cardCollection;
    private final List<Card> randCards;
    private final List<Logs> logs;

    public App() {

        this.cardCollection = new ArrayList<>();
        this.randCards = new ArrayList<>();
        this.logs = new ArrayList<>();
        setStateDefault();
    }

    public void input(String str) {

        logs.add(new Logs(str));

        switch (getState()) {
            case START:
                command(str);
                break;
            case ADD_TERM:
            case ADD_DEFINITION:
                commandAdd(str);
                break;
            case IMPORT:
                importFile(str);
                break;
            case EXPORT:
                exportFile(str);
                break;
            case EXPORT_LOGS:
                exportLogsFile(str);
                break;
            case REMOVE:
                commandRemove(str);
                break;
            case ASK_PROCESS_QTY:
            case ASK_PROCESS_ASK:
            case ASK_PROCESS_CHECK:
                commandAsk(str);
                break;
            case EXIT:
                commandExit();
                break;
            default:
                setStateDefault();
        }

    }

    private void command(String str) {

        Command command = Command.getCommand(str);

        switch (command) {
            case ADD:
                commandAdd(str);
                break;
            case REMOVE:
                commandRemove(str);
                break;
            case IMPORT:
            case EXPORT:
            case LOGS:
                commandExchange(command);
                break;
            case ASK:
                commandAsk(str);
                break;
            case RESET:
                commandResetStats();
                break;
            case HARDER_CARDS:
                commandShowHarderCards();
                break;
            case EXIT:
                commandExit();
                break;
            default:
                setStateDefault();
        }
    }

    private void commandAdd(String str) {

        switch (getState()) {
            case START:
                printMessage("The card:");
                setState(State.ADD_TERM);
                break;
            case ADD_TERM:
            case ADD_DEFINITION:
                addCard(str);
                break;
            default:
                setStateDefault();
                break;
        }
    }

    private void commandRemove(String str) {

        switch (getState()) {
            case START:
                printMessage("The card:");
                setState(State.REMOVE);
                break;
            case REMOVE:
                if (cardCollection.contains(new Card(str))) {
                    cardCollection.remove(new Card(str));
                    printMessage("The card has been removed");
                } else {
                    printMessage("Can't remove \"" + str + "\": there is no such card.");
                }
                setStateDefault();
                break;
            default:
                setStateDefault();
                break;
        }
    }

    private void commandAsk(String str) {

        switch (getState()) {
            case START:
                printMessage("How many times to ask?");
                setState(State.ASK_PROCESS_QTY);
                break;
            case ASK_PROCESS_QTY:
                setRandomCards(str);
            case ASK_PROCESS_ASK:
                ask();
                break;
            case ASK_PROCESS_CHECK:
                checkAnswer(str);
            default:
                break;
        }
    }

    private void commandExchange(Command command) {

        switch (command) {
            case IMPORT:
                printMessage("File name:");
                setState(State.IMPORT);
                break;
            case EXPORT:
                printMessage("File name:");
                setState(State.EXPORT);
                break;
            case LOGS:
                printMessage("File name:");
                setState(State.EXPORT_LOGS);
                break;
            default:
                setStateDefault();
        }
    }

    private void commandShowHarderCards() {

        try {

            List<Card> cards = cardCollection.stream().flatMap(Stream::ofNullable)
                    .filter(c -> c.getMistake() > 0)
                    .collect(Collectors.groupingBy(Card::getMistake, TreeMap::new, Collectors.toList()))
                    .lastEntry()
                    .getValue();

            if (cards.size() == 1) {
                printMessage("The hardest card is \"" + cards.get(0).getTerm() + "\". You have " + cards.get(0).getMistake() + " errors answering it.");
            } else {
                String terms = cards.stream().map(Card::getTerm).collect(Collectors.joining("\", \"", "\"", "\""));
                printMessage("The hardest cards are " + terms + ". You have " + cards.get(0).getMistake() + " errors answering them.");
            }
            setStateDefault();

        } catch (NullPointerException e) {
            printMessage("There are no cards with errors.");
            setStateDefault();
        }
    }

    private void commandResetStats() {
        cardCollection.forEach(Card::resetMistake);
        printMessage("Card statistics has been reset.");
        setStateDefault();
    }

    public void commandExit() {
        printMessage("Bye bye!");
        setState(State.EXIT);
    }

    private void exportFile(String str) {

        try {
            FileManager.exportFile(cardCollection, str);
            printMessage(cardCollection.size() + " cards have been saved.");
            setStateDefault();
        } catch (IOException e) {
            printMessage(e.getMessage());
            setStateDefault();
        }
    }

    private void exportLogsFile(String str) {

        try {
            FileManager.exportLogs(logs, str);
            printMessage("The log has been saved.");
            setStateDefault();
        } catch (IOException e) {
            printMessage(e.getMessage());
            setStateDefault();
        }
    }

    private void importFile(String str) {

        try {
            List<Card> importCC = FileManager.importFile(str);

            for (Card card : importCC) {

                if (cardCollection.contains(card)) {
                    cardCollection.set(cardCollection.indexOf(card), card);
                } else {
                    cardCollection.add(card);
                }

            }
            printMessage(importCC.size() + " cards have been loaded.");
            setStateDefault();

        } catch (IOException | ClassNotFoundException e) {
            printMessage("File not found.");
            setStateDefault();
        }
    }

    private void setRandomCards(String count) {

        int countInt = Integer.parseInt(count);

        List<Card> _list = new ArrayList<>(cardCollection);

        int[] randomIds = new Random().ints(countInt, 0, _list.size()).toArray();

        for (int randomId : randomIds) {
            randCards.add(_list.get(randomId));
        }

        setState(State.ASK_PROCESS_ASK);
    }

    private void checkAnswer(String str) {

        Card card = randCards.get(randCards.size() - 1);

        Card cardFound = cardCollection.stream().filter(c -> c.getDefinition().equals(str)).findFirst().orElse(null);

        if (card.getDefinition().equals(str)) {
            printMessage("Correct answer");
        } else if (cardFound != null) {
            cardCollection.get(cardCollection.indexOf(card)).setMistake(card.getMistake() + 1);
            printMessage("Wrong answer. The correct one is \"" + card.getDefinition() + "\", you've just written the definition of \"" + cardFound.getTerm() + "\".");
        } else {
            cardCollection.get(cardCollection.indexOf(card)).setMistake(card.getMistake() + 1);
            printMessage("Wrong answer. The correct one is \"" + card.getDefinition() + "\".");
        }

        randCards.remove(randCards.size() - 1);
        setState(State.ASK_PROCESS_ASK);
        ask();
    }

    private void ask() {

        if (!randCards.isEmpty()) {
            printMessage("Print the definition of \"" + randCards.get(randCards.size() - 1).getTerm() + "\":");
            setState(State.ASK_PROCESS_CHECK);
        } else {
            setStateDefault();
        }
    }

    private void addCard(String str) {

        boolean isMath;

        switch (getState()) {
            case ADD_TERM:

                isMath = cardCollection.stream().anyMatch(c -> c.getTerm().equals(str));

                if (isMath) {
                    printMessage("The card \"" + str + "\" already exists.");
                    setStateDefault();
                } else {
                    cardCollection.add(new Card(str));
                    printMessage("The definition of the card");
                    setState(State.ADD_DEFINITION);
                }
                break;
            case ADD_DEFINITION:

                isMath = cardCollection.stream().anyMatch(c -> c.getDefinition().equals(str));

                if (isMath) {
                    cardCollection.remove(cardCollection.size() - 1);
                    printMessage("The definition \"" + str + "\" already exists.");
                } else {
                    cardCollection.get(cardCollection.size() - 1).setDefinition(str);
                    printMessage("The pair (\"" + cardCollection.get(cardCollection.size() - 1).getTerm() + "\":\"" + str + "\") has been added.");
                }
                setStateDefault();
                break;
            default:
                setStateDefault();
                break;
        }
    }

    public void setStateDefault() {
        printMessage("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        setState(State.START);
    }

    public void printMessage(String string) {
        logs.add(new Logs(string));
        System.out.println(string);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}