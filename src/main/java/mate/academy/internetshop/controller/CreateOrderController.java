package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.OrderService;

public class CreateOrderController extends HttpServlet {
    @Inject
    private static OrderService orderService;
    @Inject
    private static BucketService bucketService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String bucketId = req.getParameter("bucket_id");
        Bucket bucket = bucketService.get(Long.valueOf(bucketId)).get();
        Order newOrder = orderService.completeOrder(bucket.getItems(), bucket.getUser().getId());
        resp.sendRedirect(req.getContextPath() + "/bucketItems?bucket_id=" + bucketId);
    }
}
