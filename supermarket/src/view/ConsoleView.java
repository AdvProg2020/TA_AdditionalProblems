package view;

import controller.SupermarketController;
import model.Good;

import java.util.Collection;
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
                if (matcher.find()) {
                    String goodName = matcher.group(2);
                    boolean isCountable = matcher.group(1).equalsIgnoreCase("countable");
                    addGood(goodName, isCountable);
                }
            } else if (ConsoleCommand.NEW_ORDER.getStringMatcher(command).matches()) {
                // TODO: implement
            } else if (ConsoleCommand.GOODS_LIST.getStringMatcher(command).matches()) {
                printGoodList();
            } else if (ConsoleCommand.TOTAL_SALES.getStringMatcher(command).matches()) {
                // TODO: implement
            } else if (ConsoleCommand.TOTAL_PROFIT.getStringMatcher(command).matches()) {
                // TODO: implement
            } else {
                System.out.println("Invalid Command.");
            }
        }
    }

    private static String getInputInFormat(String helpText, String regex) {
        Pattern pattern = Pattern.compile(regex);
        boolean inputIsInvalid;
        String line;
        do {
            System.out.println(helpText);
            line = scanner.nextLine().trim();
            Matcher matcher = pattern.matcher(line);
            inputIsInvalid = !matcher.find();
        } while(inputIsInvalid);
        return line;
    }

    private static void addGood(String goodName, boolean isCountable) {
        int count = 0;
        double amount = 0;
        if (isCountable) {
            String countString = getInputInFormat("Enter count:", "^\\d+$");
            count = Integer.parseInt(countString);
        } else {
            String amountString = getInputInFormat("Enter amount:", "^\\d+(.\\d+)?$");
            amount = Double.parseDouble(amountString);
        }
        String[] priceString = getInputInFormat("Enter sell and buy price:", "^\\d+\\s+\\d+$")
                .split("\\s+");
        int salePrice = Integer.parseInt(priceString[0]);
        int buyPrice = Integer.parseInt(priceString[1]);

        Good good = controller.addGood(goodName, isCountable, count, amount, buyPrice, salePrice);

        int totalCount = good.getCount();
        double totalAmount = good.getAmount();

        if (isCountable) {
            System.out.format("Countable good %s added. Total inventory: %d item\n", goodName, totalCount);
        } else {
            System.out.format("Uncountable good %s added. Total inventory: %f kg\n", goodName, totalAmount);
        }
    }

    private static void printGoodList() {
        Collection<Good> goods = controller.getGoods();
        System.out.print("+-----------------+------------+------------+\n");
        System.out.print("| Good name       | Inventory  | Price(IRR) |\n");
        System.out.print("+-----------------+------------+------------+\n");
        for (Good good : goods) {
            if (good.isCountable()) {
                System.out.format("| %-15s | %-5d item | %-10d |%n", good.getName(), good.getCount(), good.getSellPrice());
            } else {
                System.out.format("| %-15s | %-7.02f kg | %-10d |%n", good.getName(), good.getAmount(), good.getSellPrice());
            }
        }
        System.out.println("+-----------------+------------+------------+");
    }
}
