package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import javafx.scene.image.Image;

public class ItemData {
    private final Item item;
    private final ItemTypeDto itemTypeDto;
    private final Image itemImage;

    public ItemData(Item item, ItemTypeDto itemTypeDto, Image itemImage) {
        this.item = item;
        this.itemTypeDto = itemTypeDto;
        this.itemImage = itemImage;
    }

    public Item getItem() {
        return item;
    }

    public ItemTypeDto getItemTypeDto() {
        return itemTypeDto;
    }

    public Image getItemImage() {
        return itemImage;
    }
}
