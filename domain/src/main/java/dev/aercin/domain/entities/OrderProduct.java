package dev.aercin.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderProduct {
    private UUID id;
    private BigDecimal price;
    private Integer quantity;

    public OrderProduct(){
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {  return quantity; }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
