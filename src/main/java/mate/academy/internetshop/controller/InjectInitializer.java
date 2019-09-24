package mate.academy.internetshop.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mate.academy.internetshop.lib.Injector;

public class InjectInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
