package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.UserService;

public class AddItemToBucketController extends HttpServlet {
    @Inject
    private static BucketService bucketService;
    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession(true).getAttribute("userId");
        User user = userService.get(userId).get();
        Bucket bucket = user.getBucket();
        if (bucket == null) {
            user.setBucket(bucketService.add(new Bucket(user)));
            bucket = user.getBucket();
        }
        Long itemId = Long.valueOf(req.getParameter("item_ID"));
        bucketService.addItem(bucket.getId(), itemId);
        resp.sendRedirect(req.getContextPath() + "/items");
    }
}
