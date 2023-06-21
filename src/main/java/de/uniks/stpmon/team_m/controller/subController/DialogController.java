package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Provider;

public class DialogController extends Controller {
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    private final String[] npcTexts;
    private int currentTextIndex;
    private final Text currentText;
    private final int amountOfTexts;

    @Inject
    public DialogController(Trainer npc, TextFlow dialogTextFlow, boolean alreadyEncountered) {
        NpcTextManager npcTextManager = NpcTextManager.getInstance();
        if (alreadyEncountered) {
            this.npcTexts = npcTextManager.getNpcTexts(npc._id() + "alreadyEncountered");
        } else {
            this.npcTexts = npcTextManager.getNpcTexts(npc._id());
        }
        this.currentTextIndex = 0;
        this.currentText = new Text(npcTexts[currentTextIndex]);
        this.amountOfTexts = npcTexts.length;

        dialogTextFlow.getChildren().add(currentText);

        System.out.println(npc._id());
    }


    public boolean continueDialog() {
        if (++currentTextIndex == amountOfTexts) {
            return false;
        } else {
            this.currentText.setText(npcTexts[currentTextIndex]);
            return true;
        }
    }
}
