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

        String[] K_Reuzband = {
                resources.getString("NPC.K_Reuzband0"),
                resources.getString("NPC.K_Reuzband1")
        };

        String[] Noers = {
                resources.getString("NPC.Noers0"),
                resources.getString("NPC.Noers1")
        };

        String[] Nook = {
                resources.getString("NPC.Nook0"),
                resources.getString("NPC.Nook1")
        };

        String[] Snej = {
                resources.getString("NPC.Snej0"),
                resources.getString("NPC.Snej1")
        };

        String[] Museumswaerter_Hinz = {
                resources.getString("NPC.Museumswaerter_Hinz0"),
                resources.getString("NPC.Museumswaerter_Hinz1")
        };

        String[] Museumswaerter_Kunz = {
                resources.getString("NPC.Museumswaerter_Kunz0"),
                resources.getString("NPC.Museumswaerter_Kunz1")
        };

        String[] Museumswaerter_Gogh = {
                resources.getString("NPC.Museumswaerter_Gogh0"),
                resources.getString("NPC.Museumswaerter_Gogh1")
        };

        String[] Snej2 = {
                resources.getString("NPC.Snej20"),
                resources.getString("NPC.Snej21")
        };

        String[] Waerter_S_Original = {
                resources.getString("NPC.Waerter_S_Original0"),
                resources.getString("NPC.Waerter_S_Original1")
        };

        String[] Dr_Loisok = {
                resources.getString("NPC.Dr_Loisok0"),
                resources.getString("NPC.Dr_Loisok1")
        };

        String[] Rob2 = {
                resources.getString("NPC.Rob20"),
                resources.getString("NPC.Rob21")
        };

        String[] Melvin = {
                resources.getString("NPC.Melvin0"),
                resources.getString("NPC.Melvin1")
        };

        String[] Luzius = {
                resources.getString("NPC.Luzius0"),
                resources.getString("NPC.Luzius1")
        };

        String[] Heimke = {
                resources.getString("NPC.Heimke0"),
                resources.getString("NPC.Heimke1")
        };

        String[] Maurizio = {
                resources.getString("NPC.Maurizio0"),
                resources.getString("NPC.Maurizio1")
        };

        String[] Janno = {
                resources.getString("NPC.Janno0"),
                resources.getString("NPC.Janno1")
        };

        String[] Gisbert = {
                resources.getString("NPC.Gisbert0"),
                resources.getString("NPC.Gisbert1")
        };

        String[] Pascal = {
                resources.getString("NPC.Pascal0"),
                resources.getString("NPC.Pascal1")
        };

        String[] Pam = {
                resources.getString("NPC.Pam0"),
                resources.getString("NPC.Pam1")
        };

        String[] Leon = {
                resources.getString("NPC.Leon0"),
                resources.getString("NPC.Leon1")
        };

        String[] Dan2 = {
                resources.getString("NPC.Dan20"),
                resources.getString("NPC.Dan21")
        };

        String[] Mike = {
                resources.getString("NPC.Mike0"),
                resources.getString("NPC.Mike1")
        };

        String[] Old_Witch = {
                resources.getString("NPC.Old_Witch0"),
                resources.getString("NPC.Old_Witch1")
        };

        String[] Ol_McDonald = {
                resources.getString("NPC.Ol_McDonald0"),
                resources.getString("NPC.Ol_McDonald1")
        };

        String[] Getof_Milawn = {
                resources.getString("NPC.Getof_Milawn0"),
                resources.getString("NPC.Getof_Milawn1")
        };

        String[] Huckleberry_Finn = {
                resources.getString("NPC.Huckleberry_Finn0"),
                resources.getString("NPC.Huckleberry_Finn1")
        };

        String[] Owen_Lars = {
                resources.getString("NPC.Owen_Lars0"),
                resources.getString("NPC.Owen_Lars1")
        };

        String[] Kaefersammler_Schmaldrian = {
                resources.getString("NPC.Kaefersammler_Schmaldrian0"),
                resources.getString("NPC.Kaefersammler_Schmaldrian1")
        };

        String[] Schleimsammler_Baldrian = {
                resources.getString("NPC.Schleimsammler_Baldrian0"),
                resources.getString("NPC.Schleimsammler_Baldrian1")
        };

        String[] Steinsammler_Saltrian = {
                resources.getString("NPC.Steinsammler_Saltrian0"),
                resources.getString("NPC.Steinsammler_Saltrian1")
        };

        String[] Paco = {
                resources.getString("NPC.Paco0"),
                resources.getString("NPC.Paco1")
        };

        String[] Clemme = {
                resources.getString("NPC.Clemme0"),
                resources.getString("NPC.Clemme1")
        };

        String[] Jercy_Packson = {
                resources.getString("NPC.Jercy_Packson0"),
                resources.getString("NPC.Jercy_Packson1")
        };

        String[] Charon = {
                resources.getString("NPC.Charon0"),
                resources.getString("NPC.Charon1")
        };

        String[] Fishing_Femens = {
                resources.getString("NPC.Fishing_Femens0"),
                resources.getString("NPC.Fishing_Femens1")
        };

        String[] Mayormon = {
                resources.getString("NPC.Mayormon0"),
                resources.getString("NPC.Mayormon1")
        };

        String[] Elderly_Enrik = {
                resources.getString("NPC.Elderly_Enrik0"),
                resources.getString("NPC.Elderly_Enrik1")
        };

        String[] Camping_Cio = {
                resources.getString("NPC.Camping_Cio0"),
                resources.getString("NPC.Camping_Cio1")
        };

        String[] Grilling_Grulian = {
                resources.getString("NPC.Grilling_Grulian0"),
                resources.getString("NPC.Grilling_Grulian1")
        };

        String[] Picnic_Paesch = {
                resources.getString("NPC.Picnic_Paesch0"),
                resources.getString("NPC.Picnic_Paesch1")
        };

        String[] Lil_Lom = {
                resources.getString("NPC.Lil_Lom0"),
                resources.getString("NPC.Lil_Lom1")
        };

        String[] Hungry_Haterian = {
                resources.getString("NPC.Hungry_Haterian0"),
                resources.getString("NPC.Hungry_Haterian1")
        };

        String[] Jealous_Jandro = {
                resources.getString("NPC.Jealous_Jandro0"),
                resources.getString("NPC.Jealous_Jandro1")
        };

        String[] Keb = {
                resources.getString("NPC.Keb0"),
                resources.getString("NPC.Keb1")
        };

        String[] Balbert = {
                resources.getString("NPC.Balbert0"),
                resources.getString("NPC.Balbert1")
        };

        String[] Zens = {
                resources.getString("NPC.Zens0"),
                resources.getString("NPC.Zens1")
        };

        String[] Nax = {
                resources.getString("NPC.Nax0"),
                resources.getString("NPC.Nax1")
        };

        String[] Timon2 = {
                resources.getString("NPC.Timon20"),
                resources.getString("NPC.Timon21")
        };

        String[] Empfangsdame_Giulia = {
                resources.getString("NPC.Empfangsdame_Giulia0"),
                resources.getString("NPC.Empfangsdame_Giulia1")
        };

        String[] CTO = {
                resources.getString("NPC.CTO0"),
                resources.getString("NPC.CTO1")
        };

        String[] CEO = {
                resources.getString("NPC.CEO0"),
                resources.getString("NPC.CEO1")
        };

        String[] Pom = {
                resources.getString("NPC.Pom0"),
                resources.getString("NPC.Pom1")
        };

        String[] Hiwi = {
                resources.getString("NPC.Hiwi0"),
                resources.getString("NPC.Hiwi1")
        };

        String[] Perik = {
                resources.getString("NPC.Perik0"),
                resources.getString("NPC.Perik1")
        };

        String[] Pandro_Pluschi = {
                resources.getString("NPC.Pandro_Pluschi0"),
                resources.getString("NPC.Pandro_Pluschi1")
        };

        String[] Pio_Pasta = {
                resources.getString("NPC.Pio_Pasta0"),
                resources.getString("NPC.Pio_Pasta1")
        };

        String[] Clemoms = {
                resources.getString("NPC.Clemoms0"),
                resources.getString("NPC.Clemoms1")
        };

        String[] Christian_Love = {
                resources.getString("NPC.Christian_Love0"),
                resources.getString("NPC.Christian_Love1")
        };

        String[] Woerni_von_der_Tanke = {
                resources.getString("NPC.Woerni_von_der_Tanke0"),
                resources.getString("NPC.Woerni_von_der_Tanke1")
        };

        String[] Eis_Eis_Werni = {
                resources.getString("NPC.Eis_Eis_Werni0"),
                resources.getString("NPC.Eis_Eis_Werni1")
        };

        String[] Woerni_Woerstchen = {
                resources.getString("NPC.Woerni_Woerstchen0"),
                resources.getString("NPC.Woerni_Woerstchen1")
        };

        String[] Rockrick = {
                resources.getString("NPC.Rockrick0"),
                resources.getString("NPC.Rockrick1")
        };

        String[] Vernon = {
                resources.getString("NPC.Vernon0"),
                resources.getString("NPC.Vernon1")
        };

        String[] Tony = {
                resources.getString("NPC.Tony0"),
                resources.getString("NPC.Tony1")
        };

        String[] Jakta = {
                resources.getString("NPC.Jakta0"),
                resources.getString("NPC.Jakta1")
        };

        String[] Giovanni = {
                resources.getString("NPC.Giovanni0"),
                resources.getString("NPC.Giovanni1")
        };

        String[] Giorgo = {
                resources.getString("NPC.Giorgo0"),
                resources.getString("NPC.Giorgo1")
        };

        String[] Giolito = {
                resources.getString("NPC.Giolito0"),
                resources.getString("NPC.Giolito1")
        };

        String[] Gioberto = {
                resources.getString("NPC.Gioberto0"),
                resources.getString("NPC.Gioberto1")
        };

        String[] Giordano = {
                resources.getString("NPC.Giordano0"),
                resources.getString("NPC.Giordano1")
        };

        String[] Giovenco = {
                resources.getString("NPC.Giovenco0"),
                resources.getString("NPC.Giovenco1")
        };

        String[] Gioseppe = {
                resources.getString("NPC.Gioseppe0"),
                resources.getString("NPC.Gioseppe1")
        };

        String[] Simone = {
                resources.getString("NPC.Simone0"),
                resources.getString("NPC.Simone1")
        };

        String[] Clemente = {
                resources.getString("NPC.Clemente0"),
                resources.getString("NPC.Clemente1")
        };

        String[] Nataschetta = {
                resources.getString("NPC.Nataschetta0"),
                resources.getString("NPC.Nataschetta1")
        };

        String[] Tommaso = {
                resources.getString("NPC.Tommaso0"),
                resources.getString("NPC.Tommaso1")
        };

        String[] Sandressio = {
                resources.getString("NPC.Sandressio0"),
                resources.getString("NPC.Sandressio1")
        };

        String[] Mr_Worldwide = {
                resources.getString("NPC.Mr_Worldwide0"),
                resources.getString("NPC.Mr_Worldwide1")
        };

        String[] Adriano = {
                resources.getString("NPC.Adriano0"),
                resources.getString("NPC.Adriano1")
        };

        String[] Enrico = {
                resources.getString("NPC.Enrico0"),
                resources.getString("NPC.Enrico1")
        };

        String[] Massimiliano_di_Medici = {
                resources.getString("NPC.Massimiliano_di_Medici0"),
                resources.getString("NPC.Massimiliano_di_Medici1")
        };

        String[] Giuliano = {
                resources.getString("NPC.Giuliano0"),
                resources.getString("NPC.Giuliano1")
        };

        String[] Sebastiano = {
                resources.getString("NPC.Sebastiano0"),
                resources.getString("NPC.Sebastiano1")
        };

        String[] Fisherwoman = {
                resources.getString("NPC.Fisherwoman0"),
                resources.getString("NPC.Fisherwoman1")
        };

        String[] Sleepy_Timon = {
                resources.getString("NPC.Sleepy_Timon0"),
                resources.getString("NPC.Sleepy_Timon1")
        };

        String[] Josh = {
                resources.getString("NPC.Josh0"),
                resources.getString("NPC.Josh1")
        };

        String[] Tyler = {
                resources.getString("NPC.Tyler0"),
                resources.getString("NPC.Tyler1")
        };

        String[] Ferryman_Ferik = {
                resources.getString("NPC.Ferryman_Ferik0"),
                resources.getString("NPC.Ferryman_Ferik1")
        };

        String[] Drifting_Impossible = {
                resources.getString("NPC.Drifting_Impossible0"),
                resources.getString("NPC.Drifting_Impossible1")
        };

        String[] Schmimon = {
                resources.getString("NPC.Schmimon0"),
                resources.getString("NPC.Schmimon1")
        };

        String[] Mlemens = {
                resources.getString("NPC.Mlemens0"),
                resources.getString("NPC.Mlemens1")
        };

        String[] Carie = {
                resources.getString("NPC.Carie0"),
                resources.getString("NPC.Carie1")
        };

        String[] Gio_Chips = {
                resources.getString("NPC.Gio_Chips0"),
                resources.getString("NPC.Gio_Chips1")
        };

        String[] Gio_Tto = {
                resources.getString("NPC.Gio_Tto0"),
                resources.getString("NPC.Gio_Tto1")
        };

        String[] Anonymous = {
                resources.getString("NPC.Anonymous0"),
                resources.getString("NPC.Anonymous1")
        };

        String[] Officer_Dunot = {
                resources.getString("NPC.Officer_Dunot0"),
                resources.getString("NPC.Officer_Dunot1")
        };

        String[] Tax_Office_Agent = {
                resources.getString("NPC.Tax_Office_Agent0"),
                resources.getString("NPC.Tax_Office_Agent1")
        };


        npcTexts.put("Default", defaultTexts);

        npcTexts.put("645e32c6866ace359554a802", ProfAlbert);
        npcTexts.put("645e32c6866ace359554a802alreadyEncountered", ProfAlbertAlreadyEncountered);

        npcTexts.put("Nurse", Nurse);
        npcTexts.put("NurseNoMons", NurseNoMons);
        npcTexts.put("Clerk", Clerk);

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

        npcTexts.put("64a428af6cec1b8f0f0fef93", K_Reuzband);

        npcTexts.put("64a428af6cec1b8f0f0fef98", Noers);

        npcTexts.put("64a428af6cec1b8f0f0fef9d", Nook);

        npcTexts.put("64a820186cec1b8f0f6f9088", Snej);

        npcTexts.put("64a820186cec1b8f0f6f9091", Museumswaerter_Hinz);

        npcTexts.put("64a820186cec1b8f0f6f9098", Museumswaerter_Kunz);

        npcTexts.put("64a820186cec1b8f0f6f90a0", Museumswaerter_Gogh);

        npcTexts.put("64a820186cec1b8f0f6f90ad", Snej2);

        npcTexts.put("64a820186cec1b8f0f6f90bc", Waerter_S_Original);

        npcTexts.put("64a820186cec1b8f0f6f90c3", Dr_Loisok);

        npcTexts.put("64b10f7c6cec1b8f0f5435aa", Rob2);

        npcTexts.put("64b514726cec1b8f0fc27ea3", Melvin);

        npcTexts.put("64b514726cec1b8f0fc27ea8", Luzius);

        npcTexts.put("64b514726cec1b8f0fc27ead", Heimke);

        npcTexts.put("64b514726cec1b8f0fc27eb4", Maurizio);

        npcTexts.put("64b514726cec1b8f0fc27eb9", Janno);

        npcTexts.put("64b514726cec1b8f0fc27ebe", Gisbert);

        npcTexts.put("64b514726cec1b8f0fc27ec5", Pascal);

        npcTexts.put("64b514736cec1b8f0fc27ed3", Pam);

        npcTexts.put("64b514736cec1b8f0fc27ee1", Leon);

        npcTexts.put("64b514736cec1b8f0fc27ee8", Dan2);

        npcTexts.put("64b514736cec1b8f0fc27eef", Mike);

        npcTexts.put("64b514736cec1b8f0fc27ef6", Old_Witch);

        npcTexts.put("64b514756cec1b8f0fc27ffb", Ol_McDonald);

        npcTexts.put("64b514756cec1b8f0fc28002", Getof_Milawn);

        npcTexts.put("64b514756cec1b8f0fc28009", Huckleberry_Finn);

        npcTexts.put("64b514756cec1b8f0fc28010", Owen_Lars);

        npcTexts.put("64b514756cec1b8f0fc28019", Kaefersammler_Schmaldrian);

        npcTexts.put("64b514756cec1b8f0fc28023", Schleimsammler_Baldrian);

        npcTexts.put("64b514756cec1b8f0fc2802c", Steinsammler_Saltrian);

        npcTexts.put("64b514756cec1b8f0fc28034", Paco);

        npcTexts.put("64b514766cec1b8f0fc2803b", Clemme);

        npcTexts.put("64b514766cec1b8f0fc28050", Jercy_Packson);

        npcTexts.put("64b514766cec1b8f0fc28059", Charon);

        npcTexts.put("64b514776cec1b8f0fc280a6", Fishing_Femens);

        npcTexts.put("64b514776cec1b8f0fc280ab", Mayormon);

        npcTexts.put("64b514776cec1b8f0fc280b0", Elderly_Enrik);

        npcTexts.put("64b514776cec1b8f0fc280b5", Camping_Cio);

        npcTexts.put("64b514776cec1b8f0fc280ba", Grilling_Grulian);

        npcTexts.put("64b514776cec1b8f0fc280bf", Picnic_Paesch);

        npcTexts.put("64b514776cec1b8f0fc280c4", Lil_Lom);

        npcTexts.put("64b514776cec1b8f0fc280c9", Hungry_Haterian);

        npcTexts.put("64b514776cec1b8f0fc280d0", Jealous_Jandro);

        npcTexts.put("64b514776cec1b8f0fc280d5", Keb);

        npcTexts.put("64b514776cec1b8f0fc280da", Balbert);

        npcTexts.put("64b514776cec1b8f0fc280df", Zens);

        npcTexts.put("64b514776cec1b8f0fc280e4", Nax);

        npcTexts.put("64b514786cec1b8f0fc2811b", Timon2);

        npcTexts.put("64b514786cec1b8f0fc28128", Empfangsdame_Giulia);

        npcTexts.put("64b514786cec1b8f0fc28131", CTO);

        npcTexts.put("64b514786cec1b8f0fc2813e", CEO);

        npcTexts.put("64b514786cec1b8f0fc2814d", Pom);

        npcTexts.put("64b514786cec1b8f0fc2815a", Hiwi);

        npcTexts.put("64b514786cec1b8f0fc28165", Perik);

        npcTexts.put("64b514786cec1b8f0fc28170", Pandro_Pluschi);

        npcTexts.put("64b514786cec1b8f0fc2817b", Pio_Pasta);

        npcTexts.put("64b514786cec1b8f0fc2818d", Clemoms);

        npcTexts.put("64b514786cec1b8f0fc28190", Christian_Love);

        npcTexts.put("64b514786cec1b8f0fc28195", Woerni_von_der_Tanke);

        npcTexts.put("64b514786cec1b8f0fc28198", Eis_Eis_Werni);

        npcTexts.put("64b514786cec1b8f0fc2819b", Woerni_Woerstchen);

        npcTexts.put("64c41b60fcc75bfbe987c47f", Rockrick);

        npcTexts.put("64c41b60fcc75bfbe987c48c", Vernon);

        npcTexts.put("64c41b60fcc75bfbe987c497", Tony);

        npcTexts.put("64c41b60fcc75bfbe987c4a3", Jakta);

        npcTexts.put("64c41b61fcc75bfbe987c512", Giovanni);

        npcTexts.put("64c41b61fcc75bfbe987c517", Giorgo);

        npcTexts.put("64c41b61fcc75bfbe987c51c", Giolito);

        npcTexts.put("64c41b61fcc75bfbe987c522", Gioberto);

        npcTexts.put("64c41b61fcc75bfbe987c529", Giordano);

        npcTexts.put("64c41b61fcc75bfbe987c52e", Giovenco);

        npcTexts.put("64c41b61fcc75bfbe987c533", Gioseppe);

        npcTexts.put("64c41b61fcc75bfbe987c53a", Simone);

        npcTexts.put("64c41b61fcc75bfbe987c53f", Clemente);

        npcTexts.put("64c41b61fcc75bfbe987c544", Nataschetta);

        npcTexts.put("64c41b61fcc75bfbe987c549", Tommaso);

        npcTexts.put("64c41b61fcc75bfbe987c54c", Sandressio);

        npcTexts.put("64c41b61fcc75bfbe987c551", Mr_Worldwide);

        npcTexts.put("64c41b61fcc75bfbe987c556", Adriano);

        npcTexts.put("64c41b61fcc75bfbe987c561", Enrico);

        npcTexts.put("64c41b61fcc75bfbe987c566", Massimiliano_di_Medici);

        npcTexts.put("64c41b61fcc75bfbe987c56b", Giuliano);

        npcTexts.put("64c41b61fcc75bfbe987c570", Sebastiano);

        npcTexts.put("64c41b65fcc75bfbe987c73a", Fisherwoman);

        npcTexts.put("64c41b66fcc75bfbe987c7d1", Sleepy_Timon);

        npcTexts.put("64c41b67fcc75bfbe987c81f", Josh);

        npcTexts.put("64c41b68fcc75bfbe987c837", Tyler);

        npcTexts.put("64c41b68fcc75bfbe987c84c", Ferryman_Ferik);

        npcTexts.put("64c41b68fcc75bfbe987c84f", Drifting_Impossible);

        npcTexts.put("64c8e9bbfcc75bfbe9409803", Schmimon);

        npcTexts.put("64c8e9bcfcc75bfbe9409867", Mlemens);

        npcTexts.put("64c8e9bcfcc75bfbe9409870", Carie);

        npcTexts.put("64c8e9bdfcc75bfbe94098e0", Gio_Chips);

        npcTexts.put("64c8e9bdfcc75bfbe94098e7", Gio_Tto);

        npcTexts.put("64c8e9bdfcc75bfbe94098ee", Anonymous);

        npcTexts.put("64c8e9c0fcc75bfbe9409a54", Officer_Dunot);

        npcTexts.put("64c8e9c0fcc75bfbe9409a57", Tax_Office_Agent);

    }
}
