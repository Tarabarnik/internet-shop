package mate.academy.internetshop.service;

import mate.academy.internetshop.model.Bucket;

import java.util.Optional;

public interface BucketService {

    Bucket add(Bucket bucket);

    Optional<Bucket> get(Long id);

    void remove(Long id);

    Bucket update(Bucket bucket);

    Bucket addItem(Long bucketId, Long itemId);
}
