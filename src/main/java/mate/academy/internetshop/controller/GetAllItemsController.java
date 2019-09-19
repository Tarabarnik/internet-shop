package mate.academy.internetshop.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.ItemService;

public class GetAllItemsController extends HttpServlet {
    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Item> items = itemService.getAll();
        items.add(new Item("Dima", 12.50));
        items.add(new Item("Vova", 12.50));
        items.add(new Item("Burger", 13.));
        req.setAttribute("items", items);
        req.getRequestDispatcher("WEB-INF/views/allItems.jsp").forward(req, resp);
    }
}
