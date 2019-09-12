package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;

import mate.academy.internetshop.IdGenerator;

public class Bucket {
    private final Long id;
    private List<Item> items;
    private Long userId;

    public Bucket(Long userId) {
        this.userId = userId;
        this.id = IdGenerator.getGeneratedId();
        this.items = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
