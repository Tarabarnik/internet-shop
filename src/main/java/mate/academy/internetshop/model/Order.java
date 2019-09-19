package mate.academy.internetshop.model;

import java.util.List;

public class Order {
    private static Long idGenerator = 0L;
    private final Long id;
    private Long userId;
    private List<Item> items;

    public Order(Long userId, List<Item> items) {
        this.id = idGenerator++;
        this.userId = userId;
        this.items = items;
    }

    public Double totalPrice() {
        return items.stream().mapToDouble(Item::getPrice).sum();
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
