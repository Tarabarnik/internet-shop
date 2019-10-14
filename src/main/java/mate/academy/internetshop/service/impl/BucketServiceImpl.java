package mate.academy.internetshop.service.impl;

import java.util.Optional;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;

@Service
public class BucketServiceImpl implements BucketService {

    @Inject
    private static BucketDao bucketDao;
    @Inject
    private static ItemDao itemDao;

    @Override
    public Bucket add(Bucket bucket) {
        return bucketDao.add(bucket);
    }

    @Override
    public Optional<Bucket> get(Long id) {
        return bucketDao.get(id);
    }

    @Override
    public void remove(Long id) {
        bucketDao.delete(id);
    }

    @Override
    public Bucket update(Bucket bucket) {
        return bucketDao.update(bucket);
    }

    @Override
    public Bucket addItem(Long bucketId, Long itemId) {
        Bucket bucket = bucketDao.get(bucketId).get();
        Item item = itemDao.get(itemId).get();
        bucket.getItems().add(item);
        return bucketDao.update(bucket);
    }
}
