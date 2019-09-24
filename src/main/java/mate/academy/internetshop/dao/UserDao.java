package mate.academy.internetshop.dao;

import java.util.Optional;

import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.model.User;

public interface UserDao {

    User add(User order);

    User get(Long id);

    User update(User newOrder);

    User delete(Long id);

    User login(String login, String password) throws AuthenticationException;

    Optional<User> getByToken(String token);
}
