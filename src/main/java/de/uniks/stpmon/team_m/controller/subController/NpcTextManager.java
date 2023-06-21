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

        npcTexts.put("Default", defaultTexts);
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
