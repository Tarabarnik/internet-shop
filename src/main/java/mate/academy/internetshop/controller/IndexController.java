package mate.academy.internetshop.controller;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.ItemService;
import org.apache.log4j.Logger;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexController extends HttpServlet {
    private static Logger logger = Logger.getLogger(IndexController.class);

    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Item item = itemService.get(1L).get();
        logger.info("Get(1): " + item);

        Item newItem = new Item("NS200", 2500D);
        itemService.add(newItem);

        newItem.setPrice(1500D);
        itemService.update(newItem);

        itemService.remove(newItem.getId());

        itemService.getAll();
        req.getRequestDispatcher("WEB-INF/views/index.jsp").forward(req, resp);
    }
}
