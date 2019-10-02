package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao {

    Order add(Order order) throws SQLException;

    Order get(Long id);

    Order update(Order newOrder);

    void delete(Long id);

    List<Order> getAllOrdersForUser(Long userId);
}
