package mate.academy.internetshop.dao;

import java.util.ArrayList;
import java.util.List;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

public class Storage {
    public static final List<Item> items = new ArrayList<>();
    public static final List<Bucket> buckets = new ArrayList<>();
    public static final List<Order> orders = new ArrayList<>();
    public static final List<User> users = new ArrayList<>();

    static {
        items.add(new Item("Dima", 12.50));
        items.add(new Item("Vova", 12.50));
        items.add(new Item("Burger", 13.));
        //userService.add(new User("Pavel", "Kurilyuk", "Tarabarnik", "111"));
    }
}
