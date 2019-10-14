package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import mate.academy.internetshop.util.HashUtil;
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
        String query = "INSERT INTO users (name, surname, login, password, token, salt)"
                + " VALUES (?, ?, ?, ?, ?, ?);";
        long dbUserId = 0L;
        try (PreparedStatement statement = connection.prepareStatement(query,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getToken());
            statement.setString(6, user.getSalt().toString());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            dbUserId = resultSet.getLong(1);
        } catch (SQLException e) {
            logger.error("Can't add new user with id=" + user.getId(), e);
        }
        for (Role role : user.getRoles()) {
            Long roleId = getRoleIdByName(role.getRoleName().toString()).get();
            addRoleToUser(dbUserId, roleId);
        }
        User newUser = new User(dbUserId, user);
        return newUser;
    }

    @Override
    public Optional<User> get(Long id) {
        String query = "SELECT * FROM users WHERE user_id=?;";
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String token = resultSet.getString("token");
                String salt = resultSet.getString("salt");

                user = new User(userId, name, surname, login, password);
                user.setToken(token);
                user.setSalt(salt.getBytes());

                List<Order> orders = orderDao.getAllOrdersForUser(userId);
                user.setOrders(orders);
            }

            Set<Role> roles = getRolesFromUser(id);
            user.setRoles(roles);

            addBucketForUser(user);

        } catch (SQLException e) {
            logger.error("Can't get user by id=" + id, e);
        }
        return Optional.of(user);
    }

    @Override
    public User update(User newUser) {
        String query = "DELETE FROM orders WHERE orders.user_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, newUser.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete user orders by id=" + newUser.getId(), e);
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
        User user = get(id).get();

        deleteUserOrders(id);
        deleteUserRoles(id);
        deleteUserBuckets(id);

        String deleteUserQuery = "DELETE FROM users WHERE users.user_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteUserQuery)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete user by id=" + id, e);
        }

        return user;
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        String query = "SELECT user_id FROM users WHERE login=?;";
        Optional<User> user = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                user = get(userId);
                if (!login.equals(user.get().getLogin())
                        || !HashUtil.hashPassword(password, user.get().getSalt())
                        .equals(user.get().getPassword())) {
                    throw new AuthenticationException("incorrect username or password");
                }
                return user.get();
            }
        } catch (SQLException e) {
            logger.error("Can't get item by login=" + login, e);
        }
        return user.get();
    }

    @Override
    public Optional<User> getByToken(String token) {
        String query = "SELECT user_id FROM users WHERE token=?;";
        Optional<User> user = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = get(resultSet.getLong("user_id"));
            }
        } catch (SQLException e) {
            logger.error("Can't get user by token=" + token, e);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT user_id FROM users;";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                User user = this.get(id).get();
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.error("Can't get users", e);
        }
        return null;
    }

    private Optional<Long> getRoleIdByName(String roleName) {
        String query = "SELECT role_id FROM roles WHERE name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roleName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return Optional.of(resultSet.getLong("role_id"));
            }
        } catch (SQLException e) {
            logger.error("Can't get role by name=" + roleName, e);
        }
        return Optional.empty();
    }

    private void addRoleToUser(Long userId, Long roleId) {
        String addRoleQuery = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(addRoleQuery)) {
            statement.setLong(1, userId);
            statement.setLong(2, roleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't add role for user", e);
        }
    }

    private Set<Role> getRolesFromUser(Long userId) {
        String query = "SELECT roles.name FROM roles "
                + "INNER JOIN users_roles "
                + "ON users_roles.role_id=roles.role_id "
                + "INNER JOIN users "
                + "ON  users_roles.user_id=users.user_id "
                + "WHERE users.user_id=?;";
        Set<Role> roles = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roles.add((Role.of(resultSet.getString("name"))));
            }
        } catch (SQLException e) {
            logger.error("Can't get user roles", e);
        }
        return roles;
    }

    private void addBucketForUser(User user) {
        String query = "SELECT bucket_id FROM buckets WHERE user_id=?;";
        Long bucketId = 0L;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bucketId = resultSet.getLong("bucket_id");
            }
        } catch (SQLException e) {
            logger.error("Can't add bucket to user", e);
        }
        if (bucketId.equals(0L)) {
            user.setBucket(bucketDao.add(new Bucket(user)));
        } else {
            user.setBucket(bucketDao.get(bucketId).get());
        }
    }

    private void deleteUserOrders(Long userId) {
        String deleteOrderItemsQuery = "DELETE orders_items.* FROM orders_items "
                + "INNER JOIN orders ON orders_items.order_id = orders.order_id "
                + "INNER JOIN users ON orders.user_id = users.user_id "
                + "WHERE users.user_id=?";
        String deleteOrdersQuery = "DELETE FROM orders WHERE orders.user_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderItemsQuery)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete user order's items by id=" + userId, e);
        }

        try (PreparedStatement statement = connection.prepareStatement(deleteOrdersQuery)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete user orders by id=" + userId, e);
        }
    }

    private void deleteUserRoles(Long userId) {
        String deleteUserRolesQuery = "DELETE FROM users_roles WHERE users_roles.user_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteUserRolesQuery)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete user roles by id=" + userId, e);
        }
    }

    private void deleteUserBuckets(Long userId) {
        String deleteUserBucketItemsQuery = "DELETE buckets_items.* "
                + "FROM buckets_items "
                + "INNER JOIN buckets ON buckets_items.bucket_id = buckets.bucket_id "
                + "INNER JOIN users ON users.user_id = buckets.user_id "
                + "WHERE users.user_id=?";
        try (PreparedStatement statement = connection.prepareStatement(
                deleteUserBucketItemsQuery)) {
            statement.setLong(1,userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete user by id=" + userId, e);
        }

        String deleteUserBucketQuery = "DELETE FROM buckets WHERE user_id=?";
        try (PreparedStatement statement = connection.prepareStatement(deleteUserBucketQuery)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete buckets by user_id=" + userId, e);
        }
    }
}
