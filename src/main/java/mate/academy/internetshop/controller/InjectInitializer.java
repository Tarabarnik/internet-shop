package mate.academy.internetshop.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mate.academy.internetshop.lib.InjectorOld;
import org.apache.log4j.Logger;

public class InjectInitializer implements ServletContextListener {
    private static Logger logger = Logger.getLogger(InjectInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            logger.debug("Dependency injection started...");
            InjectorOld.injectDependency();
        } catch (IllegalAccessException e) {
            logger.error(new RuntimeException(e));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
