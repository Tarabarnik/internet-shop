package mate.academy.internetshop.lib;

import java.lang.reflect.Field;

import mate.academy.internetshop.AnnotatedClassMap;
import mate.academy.internetshop.service.impl.BucketServiceImpl;
import mate.academy.internetshop.service.impl.ItemServiceImpl;
import mate.academy.internetshop.service.impl.OrderServiceImpl;
import mate.academy.internetshop.service.impl.UserServiceImpl;

public class Injector {

    public static void injectDependency() throws IllegalAccessException {

        Field[] itemServiceFields = ItemServiceImpl.class.getFields();
        Field[] orderServiceFields = OrderServiceImpl.class.getFields();
        Field[] bucketServiceFields = BucketServiceImpl.class.getFields();

        for (Field field : itemServiceFields) {
            if (field.getDeclaredAnnotation(Inject.class) != null) {
                field.setAccessible(true);
                field.set(null, AnnotatedClassMap.getImplementation(field.getType()));
            }
        }

        for (Field field : orderServiceFields) {
            if (field.getDeclaredAnnotation(Inject.class) != null) {
                field.setAccessible(true);
                field.set(null, AnnotatedClassMap.getImplementation(field.getType()));
            }
        }

        for (Field field : bucketServiceFields) {
            if (field.getDeclaredAnnotation(Inject.class) != null) {
                field.setAccessible(true);
                field.set(null, AnnotatedClassMap.getImplementation(field.getType()));
            }
        }

        Field[] userServiceFields = UserServiceImpl.class.getFields();
        for (Field field : userServiceFields) {
            if (field.getDeclaredAnnotation(Inject.class) != null) {
                field.setAccessible(true);
                field.set(null, AnnotatedClassMap.getImplementation(field.getType()));
            }
        }
    }
}
