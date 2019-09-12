package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Order;

public interface OrderDao {

    Order add(Order order);

    Order get(Long id);

    Order update(Order newOrder);

    void delete(Long id);
}
