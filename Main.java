package flashcards;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);
        final App app = new App();

        while (app.getState() != State.EXIT) {

            try {
                app.input(scanner.nextLine());
            } catch (NoSuchElementException e) {
                app.setState(State.EXIT);
                System.out.println("Bye Bye!");
            }
        }
    }
}