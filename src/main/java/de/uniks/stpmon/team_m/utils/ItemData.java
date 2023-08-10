package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import javafx.scene.image.Image;

public record ItemData(Item item, ItemTypeDto itemTypeDto, Image itemImage) {
}
