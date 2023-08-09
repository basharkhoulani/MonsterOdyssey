package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemStorageTest {
    final ItemStorage itemStorage = new ItemStorage();

    @Test
    public void testItemStorage() {
        Item item = new Item("123", "647e15308c1bb6a91fb57321", 10, 1);
        ItemTypeDto itemTypeDto = new ItemTypeDto(10, "ball_normal.png", "MonBall", 15, "Used to catch Monsters", "ball");
        ItemTypeDto itemTypeDto2 = new ItemTypeDto(11, "ball_super.png", "SuperBall", 25, "Used to catch Monsters", "ball");
        assertEquals(itemStorage.getItemDataList().size(), 0);
        itemStorage.updateItemData(item, itemTypeDto, null);
        assertEquals(itemStorage.getItemDataList().size(), 0);

        itemStorage.addItemData(item, itemTypeDto, null);
        assertEquals(itemStorage.getItemDataList().size(), 1);
        itemStorage.addItemData(item, itemTypeDto2, null);
        assertEquals(itemStorage.getItemDataList().size(), 1);
        assertEquals(itemTypeDto, itemStorage.getItemData(item._id()).getItemTypeDto());
        assertNull(itemStorage.getItemData(item._id()).getItemImage());
        itemStorage.updateItemData(item, itemTypeDto2, null);
        assertEquals(itemStorage.getItemDataList().stream().filter(itemData -> itemData.getItem().equals(item)).toList().get(0).getItemTypeDto(), itemTypeDto2);
    }
}
