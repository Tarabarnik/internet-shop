package mate.academy.internetshop.dao;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.model.User;

public interface UserDao {

    User add(User user);

    User get(Long id);

    User update(User newUser);

    User delete(Long id);

    User login(String login, String password) throws AuthenticationException;

    Optional<User> getByToken(String token);

    List<User> getAll();
}
