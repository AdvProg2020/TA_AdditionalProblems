package controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import model.Good;
import model.Order;
import model.OrderItem;
import model.Supermarket;

import java.util.Collection;
import java.util.List;

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

    public Good addItemToOrder(Order order, String goodName, int count, double amount) {
        Good requestedGood = supermarket.getGoods().get(goodName);
        if (requestedGood == null)
            return null;
        if (requestedGood.isCountable()) {
            if (count <= requestedGood.getCount()) {
                OrderItem orderItem = new OrderItem(requestedGood, count, 0);
                order.getOrderItems().add(orderItem);
            }
            return requestedGood;
        } else {
            if (amount <= requestedGood.getAmount()) {
                OrderItem orderItem = new OrderItem(requestedGood, 0, amount);
                order.getOrderItems().add(orderItem);
            }
            return requestedGood;
        }
    }
}
