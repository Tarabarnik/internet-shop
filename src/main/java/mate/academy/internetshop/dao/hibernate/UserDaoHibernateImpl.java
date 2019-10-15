package mate.academy.internetshop.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.util.HashUtil;
import mate.academy.internetshop.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class UserDaoHibernateImpl implements UserDao {
    private Logger logger = Logger.getLogger(UserDaoHibernateImpl.class);

    @Override
    public User add(User user) {
        Long userId = null;
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            userId = (Long) session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        user.setId(userId);
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            User user = session.get(User.class, id);
            return Optional.of(user);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public User update(User newUser) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(newUser);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return newUser;
    }

    @Override
    public User delete(Long id) {
        User deletedUser = get(id).get();
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(deletedUser);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return deletedUser;
    }

    @Override
    public User login(String login, String password)
            throws AuthenticationException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM User WHERE login=:login");
            query.setParameter("login", login);
            User user = (User) query.uniqueResult();
            if (user.getPassword().equals(HashUtil.hashPassword(password, user.getSalt()))) {
                return user;
            }
        } catch (Exception e) {
            logger.error("Can't login by login and password", e);
        }
        return null;
    }

    @Override
    public Optional<User> getByToken(String token) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM User WHERE token=:token");
            query.setParameter("token", token);
            List<User> list = query.list();
            return list.stream().findFirst();
        } catch (Exception e) {
            logger.error("Can't get user by token", e);
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            users = session.createQuery("FROM User").list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return users;
    }
}
