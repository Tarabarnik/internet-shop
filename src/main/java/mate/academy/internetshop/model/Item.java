package mate.academy.internetshop.model;

public class Item {
    private static Long idGenerator = 0L;
    private final Long id;
    private String name;
    private Double price;

    private Item() {
        this.id = idGenerator++;
    }

    public Item(String name, Double price) {
        this.id = idGenerator++;
        this.name = name;
        this.price = price;
    }

    public Item(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
