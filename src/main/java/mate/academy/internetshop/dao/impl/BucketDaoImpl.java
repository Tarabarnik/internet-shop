package mate.academy.internetshop.dao.impl;

import java.util.Optional;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Bucket;

@Dao
public class BucketDaoImpl implements BucketDao {
    @Override
    public Bucket add(Bucket bucket) {
        Storage.buckets.add(bucket);
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long bucketId) {
        return Storage.buckets
                .stream()
                .filter(b -> b.getId().equals(bucketId))
                .findFirst();
    }

    @Override
    public Bucket update(Bucket newBucket) {
        Bucket bucket = get(newBucket.getId()).get();
        bucket.setItems(newBucket.getItems());
        bucket.setUser(newBucket.getUser());
        return bucket;
    }

    @Override
    public void delete(Long id) {
        Storage.buckets
                .removeIf(bucket -> bucket.getId().equals(id));
    }
}
