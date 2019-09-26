package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;
import org.apache.log4j.Logger;

@Dao
public class ItemDaoJdbcImpl extends AbstractDao<Item> implements ItemDao {
    private static Logger logger = Logger.getLogger(ItemDaoJdbcImpl.class);
    private static final String DB_NAME = "items";

    public ItemDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Item add(Item item) {
        Statement statement = null;
        Locale.setDefault(Locale.US);
        String query = String.format("INSERT INTO %s (name, price) VALUES ('%s', %f);"
                , DB_NAME, item.getName(), item.getPrice());
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
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
        Statement statement = null;
        String query = String.format("SELECT * FROM %s WHERE item_id=%d;", DB_NAME, id);
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
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
        Statement statement = null;
        Locale.setDefault(Locale.US);
        String query = String.format("UPDATE %s SET name='%s', price=%f WHERE item_id=%d;"
                , DB_NAME, item.getName(), item.getPrice(), item.getId());
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
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
        Statement statement = null;
        String query = String.format("DELETE FROM %s WHERE item_id=%d;", DB_NAME, id);
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
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
}
