package mate.academy.internetshop.dao.hibernate;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Dao
public class ItemDaoHibernateImpl implements ItemDao {
    private static Logger logger = Logger.getLogger(ItemDaoHibernateImpl.class);

    @Override
    public Item add(Item item) {
        Long itemId = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            itemId = (Long) session.save(item);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Can't add new item", e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return new Item(itemId, item.getName(), item.getPrice());
    }

    @Override
    public Optional<Item> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<Item> item = Optional.of(session.get(Item.class, id));
            return item;
        } catch (Exception e) {
            logger.error("Can't get item by id=" + id, e);
            return Optional.empty();
        }
    }

    @Override
    public Item update(Item item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(item);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Can't update item by id="+item.getId(), e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return item;
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Item item = get(id).get();
            session.delete(item);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Can't delete item by id=" + id, e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<Item> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Item> items = session.createCriteria(Item.class).list();
            return items;
        } catch (Exception e) {
            logger.error("Can't get all items from DB", e);
            return Collections.emptyList();
        }
    }
}
