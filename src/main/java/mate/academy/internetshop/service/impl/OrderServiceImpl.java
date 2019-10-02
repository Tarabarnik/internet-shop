package mate.academy.internetshop.service.impl;

import java.sql.SQLException;
import java.util.List;

import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.service.OrderService;
import org.apache.log4j.Logger;

@Service
public class OrderServiceImpl implements OrderService {
    private static Logger logger = Logger.getLogger(OrderServiceImpl.class);
    @Inject
    private static OrderDao orderDao;
    @Inject
    private static UserDao userDao;

    @Override
    public Order add(Order order) {
        try {
            return orderDao.add(order);
        } catch (SQLException e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    public Order get(Long id) {
        return orderDao.get(id);
    }

    @Override
    public Order update(Order order) {
        return orderDao.update(order);
    }

    @Override
    public void remove(Long id) {
        orderDao.delete(id);
    }

    @Override
    public Order completeOrder(List<Item> items, Long userId) {
        Order order = new Order(userId, items);
        try {
            orderDao.add(order);
        } catch (SQLException e) {
            logger.error(e);
        }
        userDao.get(userId).getOrders().add(order);
        return order;
    }

    @Override
    public List<Order> getAllOrdersForUser(Long userId) {
        return orderDao.getAllOrdersForUser(userId);
    }
}
