package view;

import java.util.Scanner;

public class ConsoleView {
    private static Scanner scanner = new Scanner(System.in);

    public static void start() {
        while(true) {
            String command = scanner.nextLine();
            if (ConsoleCommand.EXIT.getStringMatcher(command).matches())
                break;

            System.out.println("Invalid Command.");
        }
    }
}
