package mate.academy.internetshop;

import java.util.HashMap;
import java.util.Map;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;

public class AnnotatedClassMap {
    private static final Map<Class, Object> classMap = new HashMap<>();

    static {
        classMap.put(BucketDao.class, Factory.getItemDao());
        classMap.put(ItemDao.class, Factory.getItemDao());
        classMap.put(OrderDao.class, Factory.getItemDao());
        classMap.put(UserDao.class, Factory.getItemDao());
        classMap.put(BucketService.class, Factory.getItemDao());
        classMap.put(ItemDao.class, Factory.getItemDao());
        classMap.put(OrderService.class, Factory.getItemDao());
        classMap.put(UserService.class, Factory.getItemDao());
    }

    public static Object getImplementation(Class interfaceClass) {
        return classMap.get(interfaceClass);
    }
}
