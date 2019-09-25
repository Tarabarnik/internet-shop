package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

public class DeleteItemFromBucketController extends HttpServlet {
    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession(true).getAttribute("userId");
        User user = userService.get(userId);
        Bucket bucket = user.getBucket();
        String itemId = req.getParameter("item_id");
        bucket.getItems().removeIf(item -> item.getId().equals(Long.valueOf(itemId)));
        resp.sendRedirect(req.getContextPath() + "/bucketItems");
    }
}
