package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supermarket {
    private static Supermarket ourInstance = new Supermarket();
    private Map<String, Good> goods;
    private List<Order> orders;

    public static Supermarket getInstance() {
        return ourInstance;
    }

    public Map<String, Good> getGoods() {
        return goods;
    }

    public List<Order> getOrders() {
        return orders;
    }

    private Supermarket() {
        goods = new HashMap<>();
        orders = new ArrayList<>();
    }
}
