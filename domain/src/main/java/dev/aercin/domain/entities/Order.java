package dev.aercin.domain.entities;

import dev.aercin.domain.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private UUID userId;
    private List<OrderProduct> products;
    private OrderStatus status;

    public Order() {
        this.status = OrderStatus.Suspend;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
