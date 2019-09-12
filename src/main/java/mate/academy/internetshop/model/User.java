package mate.academy.internetshop.model;

import java.util.List;

import mate.academy.internetshop.IdGenerator;

public class User {
    private final Long id;
    private String name;
    private List<Order> orders;
    private Bucket bucket;

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

    public User() {
        this.id = IdGenerator.getGeneratedId();
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
