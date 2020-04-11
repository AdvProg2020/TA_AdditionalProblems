package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String consumerName;
    private Date date;
    private OrderStatus orderStatus;
    private boolean isCash;
    private List<OrderItem> orderItems;

    public Order(String consumerName) {
        this.consumerName = consumerName;
        this.orderItems = new ArrayList<>();
        this.date = new Date();
        this.orderStatus = OrderStatus.CREATED;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCash(boolean cash) {
        isCash = cash;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public Date getDate() {
        return date;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public boolean isCash() {
        return isCash;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem item : orderItems) {
            if (item.getCount() != 0) {
                totalPrice += item.getCount() * item.getGood().getSellPrice();
            } else if (item.getAmount() != 0) {
                totalPrice += item.getAmount() * item.getGood().getSellPrice();
            }
        }
        return totalPrice;
    }

    public int getTotalProfit() {
        int totalProfit = 0;
        for (OrderItem item : orderItems) {
            if (item.getCount() != 0) {
                totalProfit += item.getCount() * (item.getGood().getSellPrice() - item.getGood().getBuyPrice());
            } else if (item.getAmount() != 0) {
                totalProfit += item.getAmount() * (item.getGood().getSellPrice() - item.getGood().getBuyPrice());
            }
        }
        return totalProfit;
    }
}
