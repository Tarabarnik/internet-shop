package mate.academy.internetshop.dao.impl;

import java.util.List;
import java.util.NoSuchElementException;

import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Order;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Storage.orders.add(order);
        return order;
    }

    @Override
    public Order get(Long id) {
        return Storage.orders.stream()
                .filter(element -> element.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Can't find order with id " + id));
    }

    @Override
    public Order update(Order newOrder) {
        Order order = get(newOrder.getId());
        order.setItems(newOrder.getItems());
        order.setUserId(newOrder.getUserId());
        return order;
    }

    @Override
    public void delete(Long id) {
        Storage.orders
                .removeIf(order -> order.getId().equals(id));
    }

    @Override
    public List<Order> getAllOrdersForUser(Long userId) {
        return null;
    }
}
