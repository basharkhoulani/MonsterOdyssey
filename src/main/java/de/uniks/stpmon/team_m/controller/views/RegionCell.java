package de.uniks.stpmon.team_m.controller.views;

import de.uniks.stpmon.team_m.dto.Region;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextAlignment;

public class RegionCell extends ListCell<Region> {

    private final ToggleGroup regionToggleGroup;

    public RegionCell(ToggleGroup regionToggleGroup) {
        this.regionToggleGroup = regionToggleGroup;
    }

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
