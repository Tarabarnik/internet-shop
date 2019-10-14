package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "buckets")
public class Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucket_id", columnDefinition = "INTEGER")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "buckets_items",
            joinColumns = @JoinColumn(name = "bucket_id", referencedColumnName = "bucket_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "item_id"))
    private List<Item> items = new ArrayList<>();

    public Bucket() {}

    public Bucket(User user) {
        this.user = user;
    }

    public Bucket(List<Item> items, User user) {
        this.items = items;
        this.user = user;
    }

    public Bucket(Long id, List<Item> items, User user) {
        this.id = id;
        this.items = items;
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
