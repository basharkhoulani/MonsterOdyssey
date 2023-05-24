package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.Region;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextAlignment;

public class RegionCell extends ListCell<Region> {

    private final ToggleGroup regionToggleGroup;

    /**
     * RegionCell is used to handle the region cells in Main Menu.
     * Every region cell has a radio button with the name of the region.
     * The region cell has a regionToggleGroup which contains all region cells.
     * The radio button has the region as user data.
     *
     * @param regionToggleGroup The {@link ToggleGroup} is used to handle the region cells.
     */

    public RegionCell(ToggleGroup regionToggleGroup) {
        this.regionToggleGroup = regionToggleGroup;
    }

    /**
     * The updateItem method is used to update the region cell.
     */

    @Override
    protected void updateItem(Region item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            final RadioButton radioButton = new RadioButton(item.name());
            radioButton.setToggleGroup(regionToggleGroup);
            setGraphic(radioButton);
            radioButton.setTextAlignment(TextAlignment.CENTER);
            radioButton.setUserData(item);
            if (this.isSelected()) {
                radioButton.setSelected(true);
            }
        } else {
            setGraphic(null);
            setText(null);
        }
    }
}
