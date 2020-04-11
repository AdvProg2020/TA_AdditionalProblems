package view;

import controller.SupermarketController;
import model.Good;
import model.Order;

import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SupermarketController controller = SupermarketController.getInstance();

    public static void start() {
        while (true) {
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
                Matcher matcher = ConsoleCommand.NEW_ORDER.getStringMatcher(command);
                if (matcher.find()) {
                    String consumerName = matcher.group(1);
                    newOrder(consumerName);
                }
            } else if (ConsoleCommand.GOODS_LIST.getStringMatcher(command).matches()) {
                printGoodList();
            } else if (ConsoleCommand.TOTAL_SALES.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.TOTAL_SALES.getStringMatcher(command);
                if (matcher.find()) {
                    String option = matcher.group(2); // option is cash or credit or null
                    printTotalSales(option);
                }
            } else if (ConsoleCommand.TOTAL_PROFIT.getStringMatcher(command).matches()) {
                printTotalProfit();
            } else {
                System.out.println("Invalid Command.");
            }
        }
    }

    private static String getInputInFormat(String helpText, String regex) {
        return getInputInFormatWithError(helpText, regex, "");
    }

    private static String getInputInFormatWithError(String helpText, String regex, String error) {
        Pattern pattern = Pattern.compile(regex);
        boolean inputIsInvalid;
        String line;
        do {
            System.out.println(helpText);
            line = scanner.nextLine().trim();
            Matcher matcher = pattern.matcher(line);
            inputIsInvalid = !matcher.find();
            if (inputIsInvalid)
                System.out.print(error);
        } while (inputIsInvalid);
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

    private static void newOrder(String consumerName) {
        Order order = controller.newOrder(consumerName);
        int totalPrice;
        boolean isCash;
        while (true) {
            String inputLine = getInputInFormatWithError(
                    "Enter item:",
                    "^(?i)((\\d+(.\\d+)?)\\s+(item|kg),\\s+(\\w+)|end\\s+order)$",
                    "Invalid input format. currect format is: \"1 item, juice\" or \"1.2 kg, corn\"\n"
            );
            if (inputLine.equalsIgnoreCase("end order"))
                break;
            Matcher matcher = Pattern.compile("^(?i)((\\d+(.\\d+)?)\\s+(item|kg),\\s+(\\w+)|end\\s+order)$").matcher(inputLine);
            if (matcher.find()) {
                boolean isCountable = matcher.group(4).equalsIgnoreCase("item");
                String goodName = inputLine.split("\\s+")[2];
                Good good;
                if (isCountable) {
                    int count = Integer.parseInt(matcher.group(2));
                    try {
                        good = controller.addItemToOrder(order, goodName, count, 0);
                        System.out.format("Item added. total price: %d\n", good.getSellPrice() * count);
                    } catch (SupermarketController.ItemNotEnoughException e) {
                        System.out.format("Only %d item of %s is available.\n", e.getGood().getCount(), e.getGood().getName());
                    } catch (SupermarketController.ItemUnavailableException e) {
                        System.out.format("%s is unavailable\n", goodName);
                    }
                } else {
                    double amount = Double.parseDouble(matcher.group(2));
                    try {
                        good = controller.addItemToOrder(order, goodName, 0, amount);
                        System.out.format("Item added. total price: %d\n", Math.round(good.getSellPrice() * amount));
                    } catch (SupermarketController.ItemNotEnoughException e) {
                        System.out.format("Only %f item of %s is available.\n", e.getGood().getAmount(), e.getGood().getName());
                    } catch (SupermarketController.ItemUnavailableException e) {
                        System.out.format("%s is unavailable\n", goodName);
                    }
                }
            }
        }
        totalPrice = order.getTotalPrice();
        System.out.format("Total price: %d IRR\n", totalPrice);
        isCash = getInputInFormat("Do you want to pay with cash or credit?", "^(cash|credit)$").equalsIgnoreCase("cash");
        controller.checkoutOrder(order, isCash);
        System.out.println("Tanks for your order");
    }

    public static void printTotalSales(String option) {
        int totalSales = controller.getTotalSales(option);
        System.out.format("%d IRR\n", totalSales);
    }

    private static void printTotalProfit() {
        int totalProfit = controller.getTotalProfit();
        System.out.format("%d IRR\n", totalProfit);
    }
}
