package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;

import org.apache.log4j.Logger;

@Dao
public class BucketDaoJdbcImpl extends AbstractDao<Bucket> implements BucketDao {
    private static Logger logger = Logger.getLogger(BucketDaoJdbcImpl.class);
    @Inject
    private static ItemDao itemDao;
    @Inject
    private static UserDao userDao;

    public BucketDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Bucket add(Bucket bucket) {
        String query = "INSERT INTO buckets (user_id) VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(query,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, bucket.getUser().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long dbBucketId = 0L;
            while (resultSet.next()) {
                dbBucketId = resultSet.getLong("bucket_id");
            }
            for (Item item : bucket.getItems()) {
                addItemToBucket(dbBucketId, item.getId());
            }
            bucket = new Bucket(dbBucketId, bucket.getItems(), bucket.getUser());
        } catch (SQLException e) {
            logger.error("Can't add new bucket with id=" + bucket.getId(), e);
        }
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long bucketId) {
        String query = "SELECT user_id FROM buckets WHERE bucket_id=?;";
        long userId = 0L;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, bucketId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getLong("user_id");
            }
            List<Item> items = getItemsFromBucket(bucketId);
            return Optional.of(new Bucket(bucketId, items, userDao.get(userId).get()));
        } catch (SQLException e) {
            logger.error("Can't get user id by bucket id=" + bucketId, e);
            return Optional.empty();
        }
    }

    @Override
    public Bucket update(Bucket bucket) {
        String query = "UPDATE buckets SET user_id = ? WHERE bucket_id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bucket.getUser().getId());
            preparedStatement.setLong(2, bucket.getId());
            preparedStatement.executeUpdate();
            for (Item item : bucket.getItems()) {
                addItemToBucket(bucket.getId(), item.getId());
            }
        } catch (SQLException e) {
            logger.error("Can`t update bucket with id=" + bucket.getId(), e);
        }
        return bucket;
    }

    @Override
    public void delete(Long id) {
        deleteItemsFromBucket(id);

        String query = String.format("DELETE FROM buckets WHERE bucket_id=?;", id);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete bucket by id", e);
        }
    }

    private void deleteItemsFromBucket(Long id) {
        String query = "DELETE FROM buckets_items WHERE bucket_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete bucket item by id=" + id, e);
        }
    }

    private void addItemToBucket(Long bucketId, Long itemId) {
        String query = "INSERT INTO buckets_items (bucket_id, item_id) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, bucketId);
            statement.setLong(2, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't add item to bucket");
        }
    }

    private List<Item> getItemsFromBucket(Long bucketId) {
        String query = "SELECT item_id FROM buckets_items WHERE bucket_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, bucketId);
            ResultSet resultSet = statement.executeQuery();
            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(itemDao.get(resultSet.getLong("item_id")).get());
            }
            return items;
        } catch (SQLException e) {
            logger.error("Can't get items from bucket", e);
        }
        return null;
    }
}
