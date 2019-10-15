package mate.academy.internetshop.dao.hibernate;

import java.util.Optional;
import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class BucketDaoHibernateImpl implements BucketDao {
    @Override
    public Bucket add(Bucket bucket) {
        Long bucketId = null;
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            bucketId = (Long) session.save(bucket);
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
        bucket.setId(bucketId);
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long bucketId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Bucket bucket = session.get(Bucket.class, bucketId);
            return Optional.of(bucket);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Bucket update(Bucket bucket) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(bucket);
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
        return bucket;
    }

    @Override
    public void delete(Long id) {
        Bucket deletedBucket = get(id).get();
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(deletedBucket);
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
    }
}
