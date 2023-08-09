package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import io.reactivex.rxjava3.annotations.NonNull;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ItemStorage {
    final List<ItemData> itemDataList;

    @Inject
    public ItemStorage() {
        itemDataList = new ArrayList<>();
    }

    public List<ItemData> getItemDataList() {
        return itemDataList;
    }

    public void addItemData(@NonNull Item item, ItemTypeDto itemTypeDto, Image itemImage) {
        if (itemDataList.stream().anyMatch(m -> m.item()._id().equals(item._id()))) {
            return;
        }
        itemDataList.add(new ItemData(item, itemTypeDto, itemImage));
    }

    // null as param means that the value should not be updated
    public void updateItemData(@NonNull Item item, ItemTypeDto itemTypeDto, Image itemImage) {
        ItemData oldItemData = itemDataList.stream().filter(m -> m.item()._id().equals(item._id())).findFirst().orElse(null);
        if (oldItemData == null) {
            return;
        }
        itemDataList.set(itemDataList.indexOf(oldItemData), new ItemData(
                item,
                itemTypeDto == null ? oldItemData.itemTypeDto() : itemTypeDto,
                itemImage == null ? oldItemData.itemImage() : itemImage
        ));
    }

    public ItemData getItemData(String itemId) {
        return itemDataList.stream().filter(m -> m.item()._id().equals(itemId)).findFirst().orElse(null);
    }
}
