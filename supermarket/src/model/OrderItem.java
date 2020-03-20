package model;

public class OrderItem {
    private Good good;
    private int count;
    private double amount;

    public OrderItem(Good good, int count, double amount) {
        this.good = good;
        this.count = count;
        this.amount = amount;
    }

    public Good getGood() {
        return good;
    }

    public int getCount() {
        return count;
    }

    public double getAmount() {
        return amount;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
