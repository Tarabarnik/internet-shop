package mate.academy.internetshop.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Order;

public interface OrderDao {

    Order add(Order order) throws SQLException;

    Optional<Order> get(Long id);

    Order update(Order newOrder);

    void delete(Long id);

    List<Order> getAllOrdersForUser(Long userId);
}
