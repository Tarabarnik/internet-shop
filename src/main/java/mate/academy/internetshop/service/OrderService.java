package mate.academy.internetshop.service;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;

public interface OrderService {

    Order add(Order order);

    Optional<Order> get(Long id);

    Order update(Order order);

    void remove(Long id);

    Order completeOrder(List<Item> items, Long userId);

    List<Order> getAllOrdersForUser(Long userId);
}
