package view;

import controller.SupermarketController;
import model.Good;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleView {
    private static Scanner scanner = new Scanner(System.in);
    private static SupermarketController controller = SupermarketController.getInstance();

    public static void start() {
        while(true) {
            String command = scanner.nextLine().trim();
            if (ConsoleCommand.EXIT.getStringMatcher(command).matches()) {
                break;
            } else if (ConsoleCommand.ADD_GOOD.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.ADD_GOOD.getStringMatcher(command);
                addGood(matcher);
            } else if (ConsoleCommand.NEW_ORDER.getStringMatcher(command).matches()) {
                // TODO: implement
            } else if (ConsoleCommand.GOODS_LIST.getStringMatcher(command).matches()) {
                // TODO: implement
            } else if (ConsoleCommand.TOTAL_SALES.getStringMatcher(command).matches()) {
                // TODO: implement
            } else if (ConsoleCommand.TOTAL_PROFIT.getStringMatcher(command).matches()) {
                // TODO: implement
            } else {
                System.out.println("Invalid Command.");
            }
        }
    }

    private static void addGood(Matcher matcher) {
        String countable = matcher.group(1);
        String goodName = matcher.group(2);
        int count = -1;
        double amount = -1;
        int buyPrice = -1, sellPrice = -1;
        boolean isCountable = countable.equalsIgnoreCase("countable");
        if (isCountable) {
            do {
                System.out.println("Enter count:");
                String countString = scanner.nextLine().trim();
                if (countString.matches("\\d+"))
                    count = Integer.valueOf(countString);
            } while(count != -1);
        } else {
            do {
                System.out.println("Enter amount:");
                String amountString = scanner.nextLine().trim();
                if (amountString.matches("\\d+(\\.\\d+)?"))
                    amount = Double.valueOf(amountString);
            } while(amount != -1);
        }
        do {
            System.out.println("Enter sell and buy price:");
            String priceLine = scanner.nextLine();
            Pattern pricePattern = Pattern.compile("(\\d+)\\s+(\\d+)");
            Matcher priceMatcher = pricePattern.matcher(priceLine);
            if (matcher.matches()) {
                sellPrice = Integer.valueOf(priceMatcher.group(1));
                buyPrice = Integer.valueOf(priceMatcher.group(2));
            }
        } while(sellPrice == -1 || buyPrice == -1);
        Good good = new Good(goodName, sellPrice, buyPrice, isCountable, count, amount);
        controller.addGood(good);
    }
}
