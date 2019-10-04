package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private static Long idGenerator = 0L;
    
    private final Long id;
    private List<Item> items;
    private Long userId;

    public Bucket(Long userId) {
        this.userId = userId;
        this.id = idGenerator++;
        this.items = new ArrayList<>();
    }

    public Bucket(List<Item> items, Long userId) {
        this.id = idGenerator++;
        this.items = items;
        this.userId = userId;
    }

    public Bucket(Long id, List<Item> items, Long userId) {
        this.id = id;
        this.items = items;
        this.userId = userId;
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
