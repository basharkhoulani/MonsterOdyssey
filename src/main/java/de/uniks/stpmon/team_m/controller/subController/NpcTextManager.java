package de.uniks.stpmon.team_m.controller.subController;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class NpcTextManager {
    private final Map<String, String[]> npcTexts;
    private final ResourceBundle resources;

    public NpcTextManager(ResourceBundle resources) {
        this.resources = resources;
        npcTexts = new HashMap<>();

        String[] defaultTexts = {
                resources.getString("NPC.DEFAULT0"),
                resources.getString("NPC.DEFAULT1"),
                resources.getString("NPC.DEFAULT2")};

        String[] ProfAlbert = {
                resources.getString("NPC.ALBERT0"),
                resources.getString("NPC.ALBERT1"),
                resources.getString("NPC.ALBERT2")};
        String[] ProfAlbertAlreadyEncountered = {resources.getString("NPC.ALBERT.ALREADY.ENCOUNTERED")};

        String[] Nurse = {
                resources.getString("NPC.NURSE0"),
                resources.getString("NPC.NURSE1"),
                resources.getString("NPC.NURSE2")};
        String[] NurseNoMons = {
                resources.getString("NPC.NURSE.NO.MONS0")};

        npcTexts.put("Default", defaultTexts);

        npcTexts.put("645e32c6866ace359554a802", ProfAlbert);
        npcTexts.put("645e32c6866ace359554a802alreadyEncountered", ProfAlbertAlreadyEncountered);

        npcTexts.put("Nurse", Nurse);
        npcTexts.put("NurseNoMons", NurseNoMons);
    }

    public String[] getNpcTexts(String npcID) {
        String[] returnTexts = npcTexts.get(npcID);

        if (returnTexts == null) {
            return npcTexts.get("Default");
        } else {
            return returnTexts;
        }
    }

    public String getSingleNpcText(String resourceIdentifier) {
        return resources.getString(resourceIdentifier);
    }
}