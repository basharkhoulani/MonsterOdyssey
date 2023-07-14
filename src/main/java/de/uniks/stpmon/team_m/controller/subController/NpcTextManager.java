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

        String[] Hans = {
                resources.getString("NPC.Hans0"),
                resources.getString("NPC.Hans1")
        };

        String[] Hannelore = {
                resources.getString("NPC.Hannelore0"),
                resources.getString("NPC.Hannelore1")
        };

        String[] Herman = {
                resources.getString("NPC.Herman0"),
                resources.getString("NPC.Herman1")
        };

        String[] Officer_Gordon = {
                resources.getString("NPC.Officer_Gordon0"),
                resources.getString("NPC.Officer_Gordon1")
        };

        String[] Inspector_Barnaby = {
                resources.getString("NPC.Inspector_Barnaby0"),
                resources.getString("NPC.Inspector_Barnaby1")
        };

        String[] Officer_Simmons = {
                resources.getString("NPC.Officer_Simmons0"),
                resources.getString("NPC.Officer_Simmons1")
        };

        String[] Officer_Duckins = {
                resources.getString("NPC.Officer_Duckins0"),
                resources.getString("NPC.Officer_Duckins1")
        };

        String[] Officer_Steven = {
                resources.getString("NPC.Officer_Steven0"),
                resources.getString("NPC.Officer_Steven1")
        };

        String[] Prisoner_1 = {
                resources.getString("NPC.Prisoner_10"),
                resources.getString("NPC.Prisoner_11")
        };

        String[] Prisoner_2 = {
                resources.getString("NPC.Prisoner_20"),
                resources.getString("NPC.Prisoner_21")
        };

        String[] Prisoner_3 = {
                resources.getString("NPC.Prisoner_30"),
                resources.getString("NPC.Prisoner_31")
        };

        String[] Kuenssi = {
                resources.getString("NPC.Kuenssi0"),
                resources.getString("NPC.Kuenssi1")
        };

        String[] Angry_Gardener = {
                resources.getString("NPC.Angry_Gardener0"),
                resources.getString("NPC.Angry_Gardener1")
        };

        String[] Felix = {
                resources.getString("NPC.Felix0"),
                resources.getString("NPC.Felix1")
        };

        String[] Lady_Lemonade = {
                resources.getString("NPC.Lady_Lemonade0"),
                resources.getString("NPC.Lady_Lemonade1")
        };

        String[] Der_Bewacher_des_heiligen_Eises = {
                resources.getString("NPC.Der_Bewacher_des_heiligen_Eises0"),
                resources.getString("NPC.Der_Bewacher_des_heiligen_Eises1")
        };

        String[] Karla = {
                resources.getString("NPC.Karla0"),
                resources.getString("NPC.Karla1")
        };

        String[] Woody = {
                resources.getString("NPC.Woody0"),
                resources.getString("NPC.Woody1")
        };

        String[] Mysterious_Presence = {
                resources.getString("NPC.Mysterious_Presence0"),
                resources.getString("NPC.Mysterious_Presence1")
        };

        String[] Angler_Joey = {
                resources.getString("NPC.Angler_Joey0"),
                resources.getString("NPC.Angler_Joey1")
        };

        String[] Angler_Joe = {
                resources.getString("NPC.Angler_Joe0"),
                resources.getString("NPC.Angler_Joe1")
        };

        String[] Rusrik = {
                resources.getString("NPC.Rusrik0"),
                resources.getString("NPC.Rusrik1")
        };

        String[] Kenny = {
                resources.getString("NPC.Kenny0"),
                resources.getString("NPC.Kenny1")
        };

        String[] Groschen = {
                resources.getString("NPC.Groschen0"),
                resources.getString("NPC.Groschen1")
        };

        String[] Fred_2 = {
                resources.getString("NPC.Fred_2_0"),
                resources.getString("NPC.Fred_2_1")
        };

        String[] Wulian = {
                resources.getString("NPC.Wulian0"),
                resources.getString("NPC.Wulian1")
        };

        String[] Nessava = {
                resources.getString("NPC.Nessava0"),
                resources.getString("NPC.Nessava1")
        };

        String[] General_Gom = {
                resources.getString("NPC.General_Gom0"),
                resources.getString("NPC.General_Gom1")
        };

        String[] Pixelbauer_Plemens = {
                resources.getString("NPC.Pixelbauer_Plemens0"),
                resources.getString("NPC.Pixelbauer_Plemens1")
        };

        String[] Badeboy_Bimon = {
                resources.getString("NPC.Badeboy_Bimon0"),
                resources.getString("NPC.Badeboy_Bimon1")
        };

        String[] Gio_Vanni = {
                resources.getString("NPC.Gio_Vanni0"),
                resources.getString("NPC.Gio_Vanni1")
        };

        String[] Saltrian = {
                resources.getString("NPC.Saltrian0"),
                resources.getString("NPC.Saltrian1")
        };

        String[] Kraehenfluesterin_Katasch = {
                resources.getString("NPC.Kraehenfluesterin_Katasch0"),
                resources.getString("NPC.Kraehenfluesterin_Katasch1")
        };

        String[] Blip = {
                resources.getString("NPC.Blip0"),
                resources.getString("NPC.Blip1")
        };

        String[] Nascha = {
                resources.getString("NPC.Nascha0"),
                resources.getString("NPC.Nascha1")
        };

        String[] Bernd = {
                resources.getString("NPC.Bernd0"),
                resources.getString("NPC.Bernd1")
        };

        String[] Rob = {
                resources.getString("NPC.Rob0"),
                resources.getString("NPC.Rob1")
        };

        String[] Clair = {
                resources.getString("NPC.Clair0"),
                resources.getString("NPC.Clair1")
        };

        String[] Stan = {
                resources.getString("NPC.Stan0"),
                resources.getString("NPC.Stan1")
        };

        String[] Reek = {
                resources.getString("NPC.Reek0"),
                resources.getString("NPC.Reek1")
        };

        String[] Golkian_Talmann = {
                resources.getString("NPC.Golkian_Talmann0"),
                resources.getString("NPC.Golkian_Talmann1")
        };

        String[] Herink = {
                resources.getString("NPC.Herink0"),
                resources.getString("NPC.Herink1")
        };

        String[] Wonas = {
                resources.getString("NPC.Wonas0"),
                resources.getString("NPC.Wonas1")
        };

        String[] Dan_the_Man = {
                resources.getString("NPC.Dan_the_Man0"),
                resources.getString("NPC.Dan_the_Man1")
        };

        String[] Corpo = {
                resources.getString("NPC.Corpo0"),
                resources.getString("NPC.Corpo1")
        };

        String[] Corpa = {
                resources.getString("NPC.Corpa0"),
                resources.getString("NPC.Corpa1")
        };

        String[] Lil_CaLa = {
                resources.getString("NPC.Lil_CaLa0"),
                resources.getString("NPC.Lil_CaLa1")
        };

        String[] Timon = {
                resources.getString("NPC.Timon0"),
                resources.getString("NPC.Timon1")
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

        npcTexts.put("649ee2086cec1b8f0f8fea2d", Hans);

        npcTexts.put("649ee2086cec1b8f0f8fea32", Hannelore);

        npcTexts.put("649ee2086cec1b8f0f8fea37", Herman);

        npcTexts.put("649ee2096cec1b8f0f8fea7f", Officer_Gordon);

        npcTexts.put("649ee2096cec1b8f0f8fea8c", Inspector_Barnaby);

        npcTexts.put("649ee2096cec1b8f0f8fea96", Officer_Simmons);

        npcTexts.put("649ee2096cec1b8f0f8feaa3", Officer_Duckins);

        npcTexts.put("649ee2096cec1b8f0f8feab0", Officer_Steven);

        npcTexts.put("649ee2096cec1b8f0f8feabb", Prisoner_1);

        npcTexts.put("649ee2096cec1b8f0f8feac8", Prisoner_2);

        npcTexts.put("649ee2096cec1b8f0f8fead7", Prisoner_3);

        npcTexts.put("649ee20a6cec1b8f0f8febff", Kuenssi);

        npcTexts.put("649ee20a6cec1b8f0f8fec08", Angry_Gardener);

        npcTexts.put("649ee20a6cec1b8f0f8fec13", Felix);

        npcTexts.put("649ee20a6cec1b8f0f8fec1a", Lady_Lemonade);

        npcTexts.put("649ee20a6cec1b8f0f8fec23", Der_Bewacher_des_heiligen_Eises);

        npcTexts.put("649ee20a6cec1b8f0f8fec34", Karla);

        npcTexts.put("649ee20a6cec1b8f0f8fec3d", Woody);

        npcTexts.put("649ee20b6cec1b8f0f8fec47", Mysterious_Presence);

        npcTexts.put("649ee20b6cec1b8f0f8fecc4", Angler_Joey);

        npcTexts.put("649ee20b6cec1b8f0f8feccb", Angler_Joe);

        npcTexts.put("649ee20b6cec1b8f0f8fecd2", Rusrik);

        npcTexts.put("649ee20b6cec1b8f0f8fecd9", Kenny);

        npcTexts.put("649ee20b6cec1b8f0f8fece0", Groschen);

        npcTexts.put("649ee20b6cec1b8f0f8fece7", Fred_2);

        npcTexts.put("649ee20b6cec1b8f0f8fecf1", Wulian);

        npcTexts.put("649ee20b6cec1b8f0f8fecfa", Nessava);

        npcTexts.put("649ee20b6cec1b8f0f8fed0f", General_Gom);

        npcTexts.put("649ee20b6cec1b8f0f8fed16", Pixelbauer_Plemens);

        npcTexts.put("649ee20b6cec1b8f0f8fed1d", Badeboy_Bimon);

        npcTexts.put("649ee20b6cec1b8f0f8fed24", Gio_Vanni);

        npcTexts.put("649ee20b6cec1b8f0f8fed2b", Saltrian);

        npcTexts.put("649ee20b6cec1b8f0f8fed32", Kraehenfluesterin_Katasch);

        npcTexts.put("649ee20b6cec1b8f0f8fed3a", Blip);

        npcTexts.put("649ee20c6cec1b8f0f8fedd0", Nascha);

        npcTexts.put("649ee20c6cec1b8f0f8fedd3", Bernd);

        npcTexts.put("649ee20c6cec1b8f0f8fedd6", Rob);

        npcTexts.put("649ee20c6cec1b8f0f8fedd9", Clair);

        npcTexts.put("649ee20c6cec1b8f0f8feddc", Stan);

        npcTexts.put("649ee20c6cec1b8f0f8fede1", Reek);

        npcTexts.put("649ee20c6cec1b8f0f8fedea", Golkian_Talmann);

        npcTexts.put("649ee20c6cec1b8f0f8fedf3", Herink);

        npcTexts.put("649ee20c6cec1b8f0f8fedfc", Wonas);

        npcTexts.put("649ee20c6cec1b8f0f8fee0a", Dan_the_Man);

        npcTexts.put("649ee20c6cec1b8f0f8fee0f", Corpo);

        npcTexts.put("649ee20c6cec1b8f0f8fee12", Corpa);

        npcTexts.put("649ee20c6cec1b8f0f8fee1a", Lil_CaLa);

        npcTexts.put("649ee20c6cec1b8f0f8fee20", Timon);
    }
}
