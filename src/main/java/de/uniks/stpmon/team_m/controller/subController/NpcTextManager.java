package de.uniks.stpmon.team_m.controller.subController;

import java.util.HashMap;
import java.util.Map;

public class NpcTextManager {
    private static NpcTextManager instance;
    private Map<String, String[]> npcTexts;

    private NpcTextManager() {
        npcTexts = new HashMap<>();

//        String[] defaultTexts = {"%NPC_DEFAULT0", "%NPC_DEFAULT1","%NPC_DEFAULT2"};
        String[] defaultTexts = {"Hey there, partner!", "How's your adventure going so far?", "I'm having a blast!"};

        String[] ProfAlbert = {"Hello young fella! For the beginning of your adventure, I want to give you a partner.",
                                "Please choose between these three cute, little bois:"};
        String[] ProfAlbertAlreadyEncountered = {"Go into the wide, open world and explore to your hearts content!"};

        npcTexts.put("Default", defaultTexts);

        npcTexts.put("645e32c6866ace359554a802", ProfAlbert);
        npcTexts.put("645e32c6866ace359554a802alreadyEncountered", ProfAlbertAlreadyEncountered);
    }

    public static NpcTextManager getInstance() {
        if (instance == null) {
            instance = new NpcTextManager();
        }

        return instance;
    }

    public String[] getNpcTexts(String npcID) {
        String[] returnTexts = npcTexts.get(npcID);

        if (returnTexts == null) {
            return npcTexts.get("Default");
        } else {
            return returnTexts;
        }
    }
}
