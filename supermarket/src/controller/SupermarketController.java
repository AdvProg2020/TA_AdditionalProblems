package controller;

import model.Good;
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
}
