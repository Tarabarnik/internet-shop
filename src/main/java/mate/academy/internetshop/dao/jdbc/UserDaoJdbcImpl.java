package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;

@Dao
public class UserDaoJdbcImpl extends AbstractDao<User> implements UserDao {
    private static Logger logger = Logger.getLogger(UserDaoJdbcImpl.class);
    @Inject
    private static OrderDao orderDao;
    @Inject
    private static ItemDao itemDao;
    @Inject
    private static BucketDao bucketDao;

    public UserDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public User add(User user) {
        PreparedStatement statement = null;
        String query = "INSERT INTO users (name, surname, login, password, token)"
                + " VALUES (?, ?, ?, ?, ?);";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getToken());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't add new user with id=" + user.getId(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        String getUserIdByTokenQuery = "SELECT users.user_id FROM users "
                + "WHERE token=?;";
        Long userId = 0L;
        try {
            statement = connection.prepareStatement(getUserIdByTokenQuery);
            statement.setString(1, user.getToken());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getLong("user_id");
            }
        } catch (SQLException e) {
            logger.warn("Can't find user_id by token=" + user.getToken(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        String addRoleQuery = "INSERT INTO users_roles (user_id, role_id) VALUES ("
                + userId + ", ?);";
        for (Role role : user.getRoles()) {
            try {
                statement = connection.prepareStatement(addRoleQuery);
                Long roleId = getRoleIdByName(role.getRoleName().toString());
                statement.setLong(1, roleId);
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.warn(String.format("Can't add %s role for user by id=%d",
                        role.getRoleName(), userId), e);
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
        return user;
    }

    @Override
    public User get(Long id) {
        PreparedStatement statement = null;
        String query = "SELECT * FROM users WHERE user_id=?;";
        User user = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String token = resultSet.getString("token");
                user = new User(userId, name, surname, login, password);
                user.setToken(token);
                List<Order> orders = orderDao.getAllOrdersForUser(userId);
                user.setOrders(orders);
            }
            query = "SELECT roles.name FROM roles "
                    + "INNER JOIN users_roles "
                    + "ON users_roles.role_id=roles.role_id "
                    + "INNER JOIN users "
                    + "ON  users_roles.user_id=users.user_id "
                    + "WHERE users.user_id=?;";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            Set<Role> roles = new HashSet<>();
            while (resultSet.next()) {
                roles.add((Role.of(resultSet.getString("name"))));
            }
            user.setRoles(roles);
            query = "SELECT bucket_id FROM buckets WHERE user_id=?;";
            Long bucketId = 0L;
            statement = connection.prepareStatement(query);
            statement.setLong(1, user.getId());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bucketId = resultSet.getLong("bucket_id");
            }
            if (bucketId.equals(0L)) {
                user.setBucket(bucketDao.add(new Bucket(user.getId())));
            } else {
                user.setBucket(bucketDao.get(bucketId));
            }
        } catch (SQLException e) {
            logger.warn("Can't get bucket items by user_id=" + id, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return user;
    }

    @Override
    public User update(User newUser) {
        PreparedStatement statement = null;
        String query = "DELETE FROM orders WHERE orders.user_id=?;";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, newUser.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete user orders by id=" + newUser.getId(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        for (Order order : newUser.getOrders()) {
            try {
                orderDao.add(order);
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return newUser;
    }

    @Override
    public User delete(Long id) {
        PreparedStatement statement = null;
        User user = get(id);
        String deleteOrderItemsQuery = "DELETE orders_items.* FROM orders_items "
                + "INNER JOIN orders ON orders_items.order_id = orders.order_id "
                + "INNER JOIN users ON orders.user_id = users.user_id "
                + "WHERE users.user_id=?";
        String deleteOrdersQuery = "DELETE FROM orders WHERE orders.user_id=?;";
        try {
            statement = connection.prepareStatement(deleteOrderItemsQuery);
            statement.setLong(1, id);
            statement.executeUpdate();
            statement = connection.prepareStatement(deleteOrdersQuery);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete user orders by id=" + id, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        String deleteUserRolesQuery = "DELETE FROM users_roles WHERE users_roles.user_id=?;";
        try {
            statement = connection.prepareStatement(deleteUserRolesQuery);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete user roles by id=" + id, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        String deleteUserBucketItemsQuery = "DELETE buckets_items.* "
                + "FROM buckets_items "
                + "INNER JOIN buckets ON buckets_items.bucket_id = buckets.bucket_id "
                + "INNER JOIN users ON users.user_id = buckets.user_id "
                + "WHERE users.user_id=?";
        String deleteUserBucketQuery = "DELETE FROM buckets WHERE user_id=?";
        String deleteUserQuery = "DELETE FROM users WHERE users.user_id=?;";
        try {
            statement = connection.prepareStatement(deleteUserBucketItemsQuery);
            statement.setLong(1,id);
            statement.executeUpdate();
            statement = connection.prepareStatement(deleteUserBucketQuery);
            statement.setLong(1, id);
            statement.executeUpdate();
            statement = connection.prepareStatement(deleteUserQuery);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete user by id=" + id, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return user;
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        PreparedStatement statement = null;
        String query = "SELECT user_id FROM users WHERE login=?;";
        Optional<User> user = Optional.empty();
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                user = Optional.of(get(userId));
                if (user.isEmpty() || !user.get().getPassword().equals(password)) {
                    throw new AuthenticationException("incorrect username or password");
                }
                return user.get();
            }
        } catch (SQLException e) {
            logger.warn("Can't get item by login=" + login, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return user.get();
    }

    @Override
    public Optional<User> getByToken(String token) {
        PreparedStatement statement = null;
        String query = "SELECT user_id FROM users WHERE token=?;";
        Optional<User> user = Optional.empty();
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = Optional.of(get(resultSet.getLong("user_id")));
            }
        } catch (SQLException e) {
            logger.warn("Can't get user by token=" + token, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close statement", e);
                }
            }
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        PreparedStatement statement = null;
        String query = "SELECT user_id FROM users;";
        List<User> users = new ArrayList<>();
        try {
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                User user = this.get(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.warn("Can't get users", e);
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

    private Long getRoleIdByName(String roleName) {
        PreparedStatement statement = null;
        String query = "SELECT role_id FROM roles WHERE name=?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, "'"+roleName+"'");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong("role_id");
            }
        } catch (SQLException e) {
            logger.warn("Can't get role by name=" + roleName, e);
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
}
