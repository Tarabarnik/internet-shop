package mate.academy.internetshop.model;

import java.util.List;

import mate.academy.internetshop.IdGenerator;

public class Order {

    private final Long id;
    private Long userId;
    private List<Item> items;

    public  Order(Long userId, List<Item> items) {
        this.id = IdGenerator.getGeneratedId();
        this.userId = userId;
        this.items = items;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<Item> getItems() {
        return items;
    }
}