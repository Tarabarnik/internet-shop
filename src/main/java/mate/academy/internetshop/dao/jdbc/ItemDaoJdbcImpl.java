package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
        Locale.setDefault(Locale.US);
        String query = "INSERT INTO items (name, price) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, item.getName());
            statement.setDouble(2, item.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't add new item with id=" + item.getId(), e);
        }
        return item;
    }

    @Override
    public Optional<Item> get(Long id) {
        String query = "SELECT * FROM items WHERE item_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long itemId = resultSet.getLong("item_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                return Optional.of(new Item(itemId, name, price));
            }
        } catch (SQLException e) {
            logger.error("Can't get item by id=" + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Item update(Item item) {
        Locale.setDefault(Locale.US);
        String query = "UPDATE items SET name= ?, price=? WHERE item_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getName());
            statement.setDouble(2, item.getPrice());
            statement.setLong(3, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't update item with id=" + item.getId(), e);
        }
        return item;
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM items WHERE item_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete item by id=" + id, e);
        }
    }

    @Override
    public List<Item> getAll() {
        String query = "SELECT * FROM items;";
        List<Item> items = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long itemId = resultSet.getLong("item_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                items.add(new Item(itemId, name, price));
            }
        } catch (SQLException e) {
            logger.error("Can't get items", e);
        }
        return items;
    }
}
