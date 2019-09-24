package mate.academy.internetshop.service;

import java.util.List;

import mate.academy.internetshop.model.Item;

public interface ItemService {

    Item add(Item item);

    Item get(Long id);

    Item update(Item item);

    void remove(Long id);

    List<Item> getAll();
}
