package model;

public class Good {
    private String name;
    private int sellPrice;
    private int buyPrice;
    private boolean isCountable;
    private int count;
    private double amount;

    public Good(String name, int sellPrice, int buyPrice, boolean isCountable, int count, double amount) {
        this.name = name;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.isCountable = isCountable;
        this.count = count;
        this.amount = amount;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public boolean isCountable() {
        return isCountable;
    }

    public int getCount() {
        return count;
    }

    public double getAmount() {
        return amount;
    }
}
