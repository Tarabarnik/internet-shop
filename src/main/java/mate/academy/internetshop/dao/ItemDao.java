package mate.academy.internetshop.dao;

import java.util.List;
import mate.academy.internetshop.model.Item;

public interface ItemDao {

    Item add(Item item);

    Item get(Long id);

    Item update(Item item);

    void delete(Long id);

    List<Item> getAll();
}
