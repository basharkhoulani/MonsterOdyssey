package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.utils.TrainerStorage;

import javax.inject.Inject;
import javax.inject.Provider;

public class DialogController extends Controller {
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    private final String npcName;

    @Inject
    public DialogController(Trainer npc) {
        npcName = npc.name();
    }
}
