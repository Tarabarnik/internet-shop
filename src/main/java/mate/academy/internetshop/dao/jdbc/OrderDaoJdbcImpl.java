package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;

import mate.academy.internetshop.model.User;
import org.apache.log4j.Logger;

@Dao
public class OrderDaoJdbcImpl extends AbstractDao<Order> implements OrderDao {
    private static Logger logger = Logger.getLogger(OrderDaoJdbcImpl.class);
    @Inject
    private static ItemDao itemDao;
    @Inject
    private static UserDao userDao;

    public OrderDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Order add(Order order) {
        String query = "INSERT INTO orders (user_id) VALUES (?);";
        Long orderId = null;
        try (PreparedStatement statement = connection.prepareStatement(query,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, order.getUser().getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            orderId = generatedKeys.getLong(1);
        } catch (SQLException e) {
            logger.error("Can't create order", e);
        }
        for (Item item : order.getItems()) {
            addItemToOrder(orderId, item.getId());
        }
        return new Order(orderId, order.getUser(), order.getItems());
    }

    @Override
    public Optional<Order> get(Long id) {
        String query = "SELECT user_id FROM orders WHERE order_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            long userId = 0L;
            while (resultSet.next()) {
                userId = resultSet.getLong("user_id");
            }
            List<Item> items = getItemsFromOrder(id);
            User user = userDao.get(userId).get();
            return Optional.of(new Order(id, user, items));
        } catch (SQLException e) {
            logger.error("Can't get order by id=" + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Order update(Order newOrder) {
        String query = "UPDATE orders SET user_id = ? WHERE order_id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, newOrder.getUser().getId());
            preparedStatement.setLong(2, newOrder.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can`t update order with id=" + newOrder.getId(), e);
        }
        return newOrder;
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM orders_items WHERE order_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete order by id=" + id, e);
        }
        query = "DELETE FROM orders WHERE order_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete items from order", e);
        }
    }

    @Override
    public List<Order> getAllOrdersForUser(Long userId) {
        List<Order> orders = new ArrayList<>();
        String getOrdersQuery = "SELECT order_id FROM orders "
                + "WHERE user_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(getOrdersQuery)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("order_id");
                Order order = get(id).get();
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.error("Can't get orders by user_id=" + userId, e);
        }
        return orders;
    }

    private void addItemToOrder(Long orderId, Long itemId) {
        String insertOrderItemQuery = "INSERT INTO orders_items "
                + "(order_id, item_id) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertOrderItemQuery)) {
            statement.setLong(1, orderId);
            statement.setLong(2, itemId);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Can't add item to order", e);
        }
    }

    private List<Item> getItemsFromOrder(Long orderId) {

        String query = "SELECT item_id FROM orders_items WHERE order_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(itemDao.get(resultSet.getLong("item_id")).get());
            }
            return items;
        } catch (SQLException e) {
            logger.error("Can't get items from order", e);
        }
        return Collections.emptyList();
    }
}
