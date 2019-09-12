package mate.academy.internetshop.service;

import mate.academy.internetshop.model.Item;

public interface ItemService {

    Item add(Item item);

    Item get(Long id);

    Item update(Item item);

    void remove(Long id);

}
