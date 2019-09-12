package mate.academy.internetshop.model;

import mate.academy.internetshop.IdGenerator;

public class Item {
    private final Long id;
    private String name;
    private Double price;

    public Item() {
        this.id = IdGenerator.getGeneratedId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
