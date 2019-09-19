package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;

import mate.academy.internetshop.dao.Storage;

public class User {
    private static Long idGenerator = 0L;
    private final Long id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private List<Order> orders;
    private Bucket bucket;

    public User() {
        this.id = idGenerator++;
        orders = new ArrayList<>();
        this.bucket = new Bucket(this.id);
        Storage.buckets.add(bucket);
    }

    public User(String name) {
        this();
        this.name = name;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
}
