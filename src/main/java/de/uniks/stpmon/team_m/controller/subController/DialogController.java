package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Objects;
import java.util.Random;

public class DialogController extends Controller {
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    private final Trainer npc;
    private final boolean alreadyEncountered;
    private String[] npcTexts;
    private int currentTextIndex;
    private final Text currentText;
    private final int amountOfTexts;

    @Inject
    public DialogController(Trainer npc, TextFlow dialogTextFlow, boolean alreadyEncountered, NpcTextManager npcTextManager, Trainer player) {
        this.npc = npc;
        this.alreadyEncountered = alreadyEncountered;

        try {
            // check if player has monsters
            if (npc.npc().canHeal()) {
                if (player.team().size() == 0) {
                    this.npcTexts = npcTextManager.getNpcTexts("NurseNoMons");
                } else {
                    this.npcTexts = npcTextManager.getNpcTexts("Nurse");
                }
            } else {
                // check if player already encountered player
                if (alreadyEncountered) {
                    this.npcTexts = npcTextManager.getNpcTexts(npc._id() + "alreadyEncountered");
                } else {
                    this.npcTexts = npcTextManager.getNpcTexts(npc._id());
                }
            }

        } catch (Error e) {
            System.err.println("NPC does not have canHeal() attribute..");
            this.npcTexts = npcTextManager.getNpcTexts(npc._id());
        }

        this.currentTextIndex = 0;
        assert this.npcTexts != null;
        this.currentText = new Text(this.npcTexts[currentTextIndex]);
        this.amountOfTexts = npcTexts.length;

        dialogTextFlow.getChildren().add(currentText);
    }

    /**
     * Method to continue the dialog.
     * There are 5 possible return values:
     *  @-1: Dialog is finished and the npc wasn't Prof. Albert, or he was already encountered
     *  @0: Talked to Prof. Albert and selected the first monster  TODO
     *  @1: Talked to Prof. Albert and selected the second monster  TODO
     *  @2: Talked to Prof. Albert and selected the third monster  TODO
     *  @3: Dialog isn't finished yet
     * @return An int based on some factors, see method description
     */
    public int continueDialog() {
        if (++currentTextIndex == amountOfTexts) {
            if (Objects.equals(this.npc._id(), "645e32c6866ace359554a802") && !this.alreadyEncountered) {
                return new Random().nextInt(0, 2);
            } else {
                return -1;
            }
        } else {
            this.currentText.setText(npcTexts[currentTextIndex]);
            return 3;
        }
    }
}
