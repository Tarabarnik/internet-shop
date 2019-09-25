package mate.academy.internetshop.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

public class DataInjectInitializer extends HttpServlet {
    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = new User("Bob", "Kelso", "BBKing", "111");
        user.addRole(Role.of("USER"));

        User admin = new User("Pavel", "Kurilyuk", "Tarabarnik", "111");
        admin.addRole(Role.of("ADMIN"));

        userService.add(user);
        userService.add(admin);
    }
}
