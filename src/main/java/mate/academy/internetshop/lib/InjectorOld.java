package mate.academy.internetshop.lib;

import com.sun.tools.javac.Main;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mate.academy.internetshop.Factory;
import mate.academy.internetshop.controller.AddItemToBucketController;
import mate.academy.internetshop.controller.BucketItemsController;
import mate.academy.internetshop.controller.CreateOrderController;
import mate.academy.internetshop.controller.DeleteItemController;
import mate.academy.internetshop.controller.DeleteOrderController;
import mate.academy.internetshop.controller.DeleteUserController;
import mate.academy.internetshop.controller.GetAllItemsController;
import mate.academy.internetshop.controller.GetAllUsersController;
import mate.academy.internetshop.controller.GetUserOrdersController;
import mate.academy.internetshop.controller.RegistrationController;
import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import mate.academy.internetshop.service.impl.BucketServiceImpl;
import mate.academy.internetshop.service.impl.ItemServiceImpl;
import mate.academy.internetshop.service.impl.OrderServiceImpl;
import mate.academy.internetshop.service.impl.UserServiceImpl;

public class InjectorOld {

    public static Map<Class, Object> classMap = new HashMap<>();
    private static List<Field[]> fieldsOfServices = new ArrayList<>();

    static {
        classMap.put(BucketDao.class, Factory.getBucketDao());
        classMap.put(ItemDao.class, Factory.getItemDao());
        classMap.put(OrderDao.class, Factory.getOrderDao());
        classMap.put(UserDao.class, Factory.getUserDao());
        classMap.put(BucketService.class, Factory.getBucketService());
        classMap.put(ItemService.class, Factory.getItemService());
        classMap.put(OrderService.class, Factory.getOrderService());
        classMap.put(UserService.class, Factory.getUserService());
    }

    public static void injectDependency() throws IllegalAccessException {

        fieldsOfServices.add(ItemServiceImpl.class.getDeclaredFields());
        fieldsOfServices.add(OrderServiceImpl.class.getDeclaredFields());
        fieldsOfServices.add(BucketServiceImpl.class.getDeclaredFields());
        fieldsOfServices.add(UserServiceImpl.class.getDeclaredFields());
        fieldsOfServices.add(Main.class.getDeclaredFields());
        fieldsOfServices.add(GetAllUsersController.class.getDeclaredFields());
        fieldsOfServices.add(GetAllItemsController.class.getDeclaredFields());
        fieldsOfServices.add(RegistrationController.class.getDeclaredFields());
        fieldsOfServices.add(AddItemToBucketController.class.getDeclaredFields());
        fieldsOfServices.add(DeleteUserController.class.getDeclaredFields());
        fieldsOfServices.add(BucketItemsController.class.getDeclaredFields());
        fieldsOfServices.add(DeleteItemController.class.getDeclaredFields());
        fieldsOfServices.add(CreateOrderController.class.getDeclaredFields());
        fieldsOfServices.add(GetUserOrdersController.class.getDeclaredFields());
        fieldsOfServices.add(DeleteOrderController.class.getDeclaredFields());

        for (Field[] fields : fieldsOfServices) {
            inject(fields);
        }
    }

    private static void inject(Field[] fields) throws IllegalAccessException {
        for (Field field : fields) {
            if (field.getDeclaredAnnotation(Inject.class) != null) {
                field.setAccessible(true);
                field.set(null, classMap.get(field.getType()));
            }
        }
    }
}
