package controller;

import model.Good;
import model.Order;
import model.OrderItem;
import model.Supermarket;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SupermarketController {
    private static SupermarketController instance = new SupermarketController();
    private Supermarket supermarket;

    private SupermarketController() {
        supermarket = Supermarket.getInstance();
    }

    public static SupermarketController getInstance() {
        return instance;
    }

    public Good addGood(String goodName, boolean isCountable, int count, double amount, int buyPrice, int salePrice) {
        Good good = supermarket.getGoods().get(goodName);
        if (good == null) {
            good = new Good(goodName, salePrice, buyPrice, isCountable, count, amount);
            supermarket.getGoods().put(goodName, good);
        } else {
            good.setCount(good.getCount() + count);
            good.setAmount(good.getAmount() + amount);
            good.setBuyPrice(buyPrice);
            good.setSellPrice(salePrice);
        }

        return good;
    }

    public Collection<Good> getGoods() {
        return supermarket.getGoods().values();
    }

    public Order newOrder(String consumerName) {
        Order order = new Order(consumerName);
        supermarket.getOrders().add(order);
        return order;
    }

    public Good addItemToOrder(Order order, String goodName, int count, double amount) throws ItemNotEnoughException, ItemUnavailableException {
        Good requestedGood = supermarket.getGoods().get(goodName);
        if (requestedGood == null)
            throw new ItemUnavailableException("Item is not available");
        if (requestedGood.isCountable()) {
            if (count <= requestedGood.getCount()) {
                OrderItem orderItem = new OrderItem(requestedGood, count, 0);
                order.getOrderItems().add(orderItem);
            } else {
                throw new ItemNotEnoughException("Item not enough.", requestedGood);
            }
        } else {
            if (amount <= requestedGood.getAmount()) {
                OrderItem orderItem = new OrderItem(requestedGood, 0, amount);
                order.getOrderItems().add(orderItem);
            } else {
                throw new ItemNotEnoughException("Item not enough.", requestedGood);
            }
        }
        return requestedGood;
    }

    public void checkoutOrder(Order order, boolean isCash) {
        order.setCash(isCash);
        for (OrderItem item : order.getOrderItems()) {
            if (item.getCount() != 0) {
                item.getGood().setCount(item.getGood().getCount() - item.getCount());
            } else if (item.getAmount() != 0) {
                item.getGood().setAmount(item.getGood().getAmount() - item.getAmount());
            }
        }
    }

    public int getTotalSales(String option) {
        List<Order> orders = supermarket.getOrders();
        int totalSales = 0;
        if (option.equalsIgnoreCase("cash")) {
            orders = orders.stream()
                    .filter(order -> order.isCash())
                    .collect(Collectors.toList());
        } else if (option.equalsIgnoreCase("credit")) {
            orders = orders.stream()
                    .filter(order -> !order.isCash())
                    .collect(Collectors.toList());
        }
        for (Order order : orders) {
            totalSales += order.getTotalPrice();
        }
        return totalSales;
    }

    public static class ItemNotEnoughException extends Exception {
        Good good;

        public Good getGood() {
            return good;
        }

        public ItemNotEnoughException(String message, Good good) {
            super(message);
            this.good = good;
        }
    }

    public static class ItemUnavailableException extends Exception {
        public ItemUnavailableException(String message) {
            super(message);
        }
    }
}
