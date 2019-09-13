package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static Long idGenerator = 0L;
    private final Long id;
    private String name;
    private List<Order> orders;
    private Bucket bucket;

    public User() {
        this.id = idGenerator++;
        orders = new ArrayList<>();
        this.bucket = new Bucket(this.id);
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
