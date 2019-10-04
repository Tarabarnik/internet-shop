package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;
import mate.academy.internetshop.util.HashUtil;

public class RegistrationController extends HttpServlet {

    private static final int MINIMAL_INPUT_LENGTH = 3;
    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User newUser = new User();
        String login = req.getParameter("login");
        String psw = req.getParameter("psw");
        String pswRepeat = req.getParameter("psw-repeat");
        String name = req.getParameter("user_name");
        String surname = req.getParameter("user_surname");
        if (login.length() >= MINIMAL_INPUT_LENGTH
                && psw.length() >= MINIMAL_INPUT_LENGTH
                && psw.equals(pswRepeat)
                && name.length() >= MINIMAL_INPUT_LENGTH
                && surname.length() >= MINIMAL_INPUT_LENGTH) {
            newUser.setLogin(login);
            newUser.setSalt(HashUtil.getSalt());
            newUser.setPassword(HashUtil.hashPassword(psw, newUser.getSalt()));
            newUser.setName(name);
            newUser.setSurname(surname);
            User user = userService.add(newUser);

            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());

            Cookie cookie = new Cookie("MATE", user.getToken());
            resp.addCookie(cookie);
        }
        resp.sendRedirect(req.getContextPath() + "/index");
    }
}
