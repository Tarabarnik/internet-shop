package mate.academy.internetshop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", columnDefinition = "INTEGER")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price", columnDefinition = "DECIMAL")
    private Double price;

    private Item() {}

    public Item(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Item(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "{"
                + "name='" + name + '\''
                + ", price=" + price
                + '}';
    }
}
