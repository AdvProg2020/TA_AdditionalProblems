package controller;

import model.Good;
import model.Supermarket;

public class SupermarketController {
    private static SupermarketController ourInstance = new SupermarketController();

    private Supermarket supermarket = Supermarket.getInstance();

    public static SupermarketController getInstance() {
        return ourInstance;
    }

    private SupermarketController() {
    }

    public void addGood(Good good) {
        // TODO: check if good exists.
        supermarket.getGoods().put(good.getName(), good);
    }
}

