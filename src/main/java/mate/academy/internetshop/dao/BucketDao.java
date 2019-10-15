package mate.academy.internetshop.dao;

import java.util.Optional;
import mate.academy.internetshop.model.Bucket;

public interface BucketDao {
    Bucket add(Bucket bucket);

    Optional<Bucket> get(Long bucketId);

    Bucket update(Bucket bucket);

    void delete(Long id);
}
