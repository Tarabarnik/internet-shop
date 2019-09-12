package mate.academy.internetshop.service;

import java.util.List;

import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;

public interface OrderService {

    Order add(Order order);

    Order get(Long id);

    Order update(Order order);

    void remove(Long id);

    Order completeOrder(List<Item> items, Long userId);

    Order completeOrder(Bucket bucket);

    List<Order> getAllOrdersForUser(Long userId);
}
