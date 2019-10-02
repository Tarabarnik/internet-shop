package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;
import org.apache.log4j.Logger;

@Dao
public class ItemDaoJdbcImpl extends AbstractDao<Item> implements ItemDao {
    private static Logger logger = Logger.getLogger(ItemDaoJdbcImpl.class);

    public ItemDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Item add(Item item) {
        PreparedStatement statement = null;
        Locale.setDefault(Locale.US);
        String query = "INSERT INTO items (name, price) VALUES (?, ?);";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, item.getName());
            statement.setDouble(2, item.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't add new item with id=" + item.getId(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return item;
    }

    @Override
    public Item get(Long id) {
        PreparedStatement statement = null;
        String query = "SELECT * FROM items WHERE item_id = ?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long itemId = resultSet.getLong("item_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                return new Item(itemId, name, price);
            }
        } catch (SQLException e) {
            logger.warn("Can't get item by id=" + id, e);
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
    public Item update(Item item) {
        PreparedStatement statement = null;
        Locale.setDefault(Locale.US);
        String query = "UPDATE items SET name= ?, price=? WHERE item_id=?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, "'"+item.getName()+"'");
            statement.setDouble(2, item.getPrice());
            statement.setLong(3, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't update item with id=" + item.getId(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return item;
    }

    @Override
    public void delete(Long id) {
        PreparedStatement statement = null;
        String query = "DELETE FROM items WHERE item_id=?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete item by id=" + id, e);
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
    public List<Item> getAll() {
        PreparedStatement statement = null;
        String query = "SELECT * FROM items;";
        List<Item> items = new ArrayList<>();
        try {
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long itemId = resultSet.getLong("item_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                items.add(new Item(itemId, name, price));
            }
        } catch (SQLException e) {
            logger.warn("Can't get items", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return items;
    }
}
