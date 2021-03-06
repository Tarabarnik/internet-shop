import mate.academy.internetshop.Factory;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Injector;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import org.apache.log4j.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    @Inject
    private static ItemService itemService;
    @Inject
    private static UserService userService;

    static {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException e) {
            logger.error(e);
        }
    }

    public static void main(String[] args) {

        Item item1 = new Item("Dima", 1.);
        Item item2 = new Item("Vova", .9);
        itemService.add(item1);
        itemService.add(item2);

        User user = new User();
        userService.add(user);
        Bucket bucket = new Bucket(user.getId());
        BucketService bucketService = Factory.getBucketService();
        bucketService.add(bucket);

        bucketService.addItem(bucket.getId(), item1.getId());
        bucketService.addItem(bucket.getId(), item2.getId());
        bucketService.addItem(bucket.getId(), item1.getId());

        OrderService orderService = Factory.getOrderService();
        Order order = orderService.completeOrder(bucket.getItems(), bucket.getUserId());
        
        order.getItems().forEach(System.out::print);

    }
}
