package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

@Dao
public class BucketDaoJdbcImpl extends AbstractDao<Bucket> implements BucketDao {
    private static Logger logger = Logger.getLogger(BucketDaoJdbcImpl.class);
    @Inject
    private static ItemDao itemDao;

    public BucketDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Bucket add(Bucket bucket) {
        PreparedStatement statement = null;
        String query = "INSERT INTO buckets (user_id) VALUES (?);";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, bucket.getUserId());
            statement.executeUpdate();

            query = "SELECT bucket_id FROM buckets WHERE user_id=?;";
            statement = connection.prepareStatement(query);
            statement.setLong(1, bucket.getUserId());
            ResultSet resultSet = statement.executeQuery();
            long dbBucketId = 0L;
            while (resultSet.next()) {
                dbBucketId = resultSet.getLong("bucket_id");
            }
            query = "INSERT INTO buckets_items (bucket_id, item_id) VALUES ("
                    + dbBucketId + ", ?);";
            statement = connection.prepareStatement(query);
            for (Item item : bucket.getItems()) {
                statement.setLong(1, item.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.warn("Can't add new bucket with id=" + bucket.getId(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return bucket;
    }

    @Override
    public Bucket get(Long bucketId) {
        PreparedStatement statement = null;
        String query = "SELECT user_id FROM buckets WHERE bucket_id=?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, bucketId);
            ResultSet resultSet = statement.executeQuery();
            long userId = 0L;
            while (resultSet.next()) {
                userId = resultSet.getLong("user_id");
            }
            query = "SELECT item_id FROM buckets_items WHERE bucket_id=?;";
            statement = connection.prepareStatement(query);
            statement.setLong(1, bucketId);
            resultSet = statement.executeQuery();
            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(itemDao.get(resultSet.getLong("item_id")));
            }
            return new Bucket(bucketId, items, userId);
        } catch (SQLException e) {
            logger.warn("Can't get bucket by id=" + bucketId, e);
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
    public Bucket update(Bucket bucket) {
        delete(bucket.getId());
        add(bucket);
        return bucket;
    }

    @Override
    public void delete(Long id) {
        PreparedStatement statement = null;
        String query = "DELETE FROM buckets_items WHERE bucket_id=?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.executeUpdate();
            query = String.format("DELETE FROM buckets WHERE bucket_id=?", id);
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete bucket by id=" + id, e);
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
