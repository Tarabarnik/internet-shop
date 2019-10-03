package mate.academy.internetshop.service;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Item;

public interface ItemService {

    Item add(Item item);

    Optional<Item> get(Long id);

    Item update(Item item);

    void remove(Long id);

    List<Item> getAll();
}
