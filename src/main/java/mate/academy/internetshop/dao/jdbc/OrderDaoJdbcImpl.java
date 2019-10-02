package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

@Dao
public class OrderDaoJdbcImpl extends AbstractDao<Order> implements OrderDao {
    private static Logger logger = Logger.getLogger(OrderDaoJdbcImpl.class);
    @Inject
    private static ItemDao itemDao;

    public OrderDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Order add(Order order) {
        PreparedStatement statement = null;
        String query = "INSERT INTO orders (user_id) VALUES (?);";
        Long orderId = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                orderId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertOrderItemQuery = "INSERT INTO orders_items "
                + "(order_id, item_id) VALUES (?, ?);";
        for (Item item : order.getItems()) {
            try {
                statement = connection.prepareStatement(insertOrderItemQuery);
                statement.setLong(1, orderId);
                statement.setLong(2, item.getId());
                statement.execute();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return new Order(orderId, order.getUserId(), order.getItems());
    }

    @Override
    public Order get(Long id) {
        PreparedStatement statement = null;
        String query = "SELECT user_id FROM orders WHERE order_id=?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            long userId = 0L;
            while (resultSet.next()) {
                userId = resultSet.getLong("user_id");
            }
            query = "SELECT item_id FROM orders_items WHERE order_id=?;";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(itemDao.get(resultSet.getLong("item_id")));
            }
            return new Order(id, userId, items);
        } catch (SQLException e) {
            logger.warn("Can't get order by id=" + id, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return null;
    }

    @Override
    public Order update(Order newOrder) {
        delete(newOrder.getId());
        add(newOrder);
        return newOrder;
    }

    @Override
    public void delete(Long id) {
        PreparedStatement statement = null;
        String query = "DELETE FROM orders_items WHERE order_id=?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.executeUpdate();
            query = "DELETE FROM orders WHERE order_id=?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete order by id=" + id, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
    }

    @Override
    public List<Order> getAllOrdersForUser(Long userId) {
        PreparedStatement statement = null;
        List<Order> orders = new ArrayList<>();
        String getOrdersQuery = "SELECT order_id FROM orders "
                + "WHERE user_id = ?;";
        try {
            statement = connection.prepareStatement(getOrdersQuery);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("order_id");
                Order order = get(id);
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.warn("Can't get orders by user_id=" + userId, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return orders;
    }
}
