package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.User;

public interface UserDao {

    User add(User order);

    User get(Long id);

    User update(User newOrder);

    void delete(Long id);
}
