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

        this.putAllNpcTextsInHashMap();
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

    private void putAllNpcTextsInHashMap() {
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

        String[] Clerk = {
                resources.getString("NPC.Clerk0"),
                resources.getString("NPC.Clerk1")
        };

        String[] Bob = {
                resources.getString("NPC.Bob0"),
                resources.getString("NPC.Bob1")
        };

        String[] Adam = {
                resources.getString("NPC.Adam0"),
                resources.getString("NPC.Adam1")
        };

        String[] Amelia = {
                resources.getString("NPC.Amelia0"),
                resources.getString("NPC.Amelia1")
        };

        String[] Kid_Tim = {
                resources.getString("NPC.Kid_Tim0"),
                resources.getString("NPC.Kid_Tim1")
        };

        String[] Dan = {
                resources.getString("NPC.Dan0"),
                resources.getString("NPC.Dan1")
        };

        String[] Lucy = {
                resources.getString("NPC.Lucy0"),
                resources.getString("NPC.Lucy1")
        };

        String[] Alex = {
                resources.getString("NPC.Alex0"),
                resources.getString("NPC.Alex1")
        };

        String[] Kid_Oscar = {
                resources.getString("NPC.Kid_Oscar0"),
                resources.getString("NPC.Kid_Oscar1")
        };

        String[] Fred = {
                resources.getString("NPC.Fred0"),
                resources.getString("NPC.Fred1")
        };

        String[] Duke = {
                resources.getString("NPC.Duke0"),
                resources.getString("NPC.Duke1")
        };

        String[] Mr_Donald = {
                resources.getString("NPC.Mr._Donald0"),
                resources.getString("NPC.Mr._Donald1")
        };

        String[] Harold = {
                resources.getString("NPC.Harold0"),
                resources.getString("NPC.Harold1"),
                resources.getString("NPC.Harold2"),
                resources.getString("NPC.Harold3")
        };

        String[] Timmy = {
                resources.getString("NPC.Timmy0"),
                resources.getString("NPC.Timmy1")
        };

        String[] Coupee = {
                resources.getString("NPC.Coupee0"),
                resources.getString("NPC.Coupee1")
        };

        String[] Clime = {
                resources.getString("NPC.Clime0"),
                resources.getString("NPC.Clime1")
        };

        String[] Sondra = {
                resources.getString("NPC.Sondra0"),
                resources.getString("NPC.Sondra1")
        };

        String[] Hinrek = {
                resources.getString("NPC.Hinrek0"),
                resources.getString("NPC.Hinrek1")
        };

        String[] Somin = {
                resources.getString("NPC.Somin0"),
                resources.getString("NPC.Somin1")
        };

        String[] Child = {
                resources.getString("NPC.Child0"),
                resources.getString("NPC.Child1")
        };

        String[] Mother = {
                resources.getString("NPC.Mother0"),
                resources.getString("NPC.Mother1")
        };

        String[] Karli = {
                resources.getString("NPC.Karli0"),
                resources.getString("NPC.Karli1")
        };

        String[] Alberto_Brandolini = {
                resources.getString("NPC.Alberto_Brandolini0"),
                resources.getString("NPC.Alberto_Brandolini1")
        };

        String[] Andria = {
                resources.getString("NPC.Andria0"),
                resources.getString("NPC.Andria1")
        };

        String[] Clementine = {
                resources.getString("NPC.Clementine0"),
                resources.getString("NPC.Clementine1")
        };

        String[] Max_Mustermann = {
                resources.getString("NPC.Max_Mustermann0"),
                resources.getString("NPC.Max_Mustermann1")
        };

        String[] Harald_Toepfer = {
                resources.getString("NPC.Harald_Toepfer0"),
                resources.getString("NPC.Harald_Toepfer1")
        };

        String[] Herr_Saelzer = {
                resources.getString("NPC.Herr_Saelzer0"),
                resources.getString("NPC.Herr_Saelzer1")
        };

        String[] Gio = {
                resources.getString("NPC.Gio0"),
                resources.getString("NPC.Gio1")
        };

        String[] Herik = {
                resources.getString("NPC.Herik0"),
                resources.getString("NPC.Herik1")
        };

        String[] Woerner = {
                resources.getString("NPC.Woerner0"),
                resources.getString("NPC.Woerner1")
        };

        String[] Worker = {
                resources.getString("NPC.Worker0"),
                resources.getString("NPC.Worker1")
        };

        String[] Fisherman = {
                resources.getString("NPC.Fisherman0"),
                resources.getString("NPC.Fisherman1")
        };

        String[] Fisherwomen = {
                resources.getString("NPC.Fisherwomen0"),
                resources.getString("NPC.Fisherwomen1")
        };

        String[] Klara = {
                resources.getString("NPC.Klara0"),
                resources.getString("NPC.Klara1")
        };

        String[] Santa = {
                resources.getString("NPC.Santa0"),
                resources.getString("NPC.Santa1")
        };

        String[] Mr_Nice_Guy = {
                resources.getString("NPC.Mr._Nice_Guy0"),
                resources.getString("NPC.Mr._Nice_Guy1")
        };

        String[] Average_Soap_Enjoyer = {
                resources.getString("NPC.Average_Soap_Enjoyer0"),
                resources.getString("NPC.Average_Soap_Enjoyer1")
        };

        String[] Local_Crackhead = {
                resources.getString("NPC.Local_Crackhead0")
        };

        String[] Benny_Bouncer = {
                resources.getString("NPC.Benny_Bouncer0"),
                resources.getString("NPC.Benny_Bouncer1")
        };

        String[] Dead_Hitchhiker = {
                resources.getString("NPC.Dead_Hitchhiker0"),
                resources.getString("NPC.Dead_Hitchhiker1")
        };


        npcTexts.put("Default", defaultTexts);

        npcTexts.put("645e32c6866ace359554a802", ProfAlbert);
        npcTexts.put("645e32c6866ace359554a802alreadyEncountered", ProfAlbertAlreadyEncountered);

        npcTexts.put("Nurse", Nurse);
        npcTexts.put("NurseNoMons", NurseNoMons);

        npcTexts.put("645e32c6866ace359554a7f8", Clerk);

        npcTexts.put("645e32c6866ace359554a7fc", Bob);

        npcTexts.put("645e32c6866ace359554a7fe", Adam);

        npcTexts.put("645e32c6866ace359554a800", Amelia);

        npcTexts.put("645e32c6866ace359554a80a", Clerk);

        npcTexts.put("645e32c7866ace359554a80f", Kid_Tim);

        npcTexts.put("645e32c7866ace359554a811", Dan);

        npcTexts.put("645e32c7866ace359554a813", Lucy);

        npcTexts.put("645e32c7866ace359554a815", Alex);

        npcTexts.put("645e32c7866ace359554a817", Kid_Oscar);

        npcTexts.put("648c7a25866ace3595acbdf8", Clerk);

        npcTexts.put("648c7a26866ace3595acbe00", Fred);

        npcTexts.put("648c7a26866ace3595acbe03", Duke);

        npcTexts.put("648c7a26866ace3595acbe06", Mr_Donald);

        npcTexts.put("648c7a26866ace3595acbe09", Harold);

        npcTexts.put("648c7a26866ace3595acbe0c", Timmy);

        npcTexts.put("648c7a26866ace3595acbe2b", Coupee);

        npcTexts.put("648c7a26866ace3595acbe32", Clime);

        npcTexts.put("648c7a26866ace3595acbe37", Sondra);

        npcTexts.put("648c7a26866ace3595acbe3c", Hinrek);

        npcTexts.put("648c7a26866ace3595acbe43", Somin);

        npcTexts.put("648c7a27866ace3595acbe4e", Child);

        npcTexts.put("648c7a27866ace3595acbe56", Mother);

        npcTexts.put("648c7a27866ace3595acbe5f", Karli);

        npcTexts.put("648c7a27866ace3595acbe66", Alberto_Brandolini);

        npcTexts.put("648c7a27866ace3595acbe6d", Andria);

        npcTexts.put("648c7a28866ace3595acbe79", Clementine);

        npcTexts.put("648c7a28866ace3595acbe83", Max_Mustermann);

        npcTexts.put("648c7a28866ace3595acbe8c", Harald_Toepfer);

        npcTexts.put("648c7a28866ace3595acbe95", Herr_Saelzer);

        npcTexts.put("648c7a28866ace3595acbe9e", Gio);

        npcTexts.put("648c7a28866ace3595acbea7", Herik);

        npcTexts.put("648c7a28866ace3595acbeb0", Woerner);

        npcTexts.put("648c7a28866ace3595acbebb", Worker);

        npcTexts.put("648c7a28866ace3595acbec0", Fisherman);

        npcTexts.put("648c7a28866ace3595acbec6", Fisherwomen);

        npcTexts.put("648c7a29866ace3595acbed5", Klara);

        npcTexts.put("648c7a29866ace3595acbed8", Santa);

        npcTexts.put("648c7a29866ace3595acbedf", Mr_Nice_Guy);

        npcTexts.put("648c7a29866ace3595acbee6", Average_Soap_Enjoyer);

        npcTexts.put("648c7a29866ace3595acbeed", Local_Crackhead);

        npcTexts.put("648c7a29866ace3595acbef4", Benny_Bouncer);

        npcTexts.put("648c7a29866ace3595acbefb", Dead_Hitchhiker);
    }
}
