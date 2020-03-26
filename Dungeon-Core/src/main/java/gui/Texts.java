package gui;

import java.util.*;

import shrine.*;

import figure.Figure;
import figure.attribute.Attribute;
import figure.hero.*;
import figure.hero.Character;
import figure.monster.Monster;
import game.JDEnv;

public class Texts {

	public static final String begin() {
		String begin = new String(
				"Dem verlassenen zugewachsenem Pfad folgendend, den Du vom Waldweg aus zufällig gefunden hast, und Deiner natürlichen Neugier wegen nicht links liegen lassen konntest, stehst Du nun vor einer hohen von Schlingpflanzen zugewucherten Felswand, und es sieht so aus, als würde dieser Pfad an dieser Wand enden. Plötzlich entdeckst Du höchst merkwürdige Spuren im weichen Waldboden und folgst ihnen entlang der Felswand bis zu einer Stelle, an der sich all diese seltsamen Fußspuren treffen. Du schiebst die Schlingpflanzen am Fels zur Seite und es kommt eine schwere eisenbeschlagenen Tür zum Vorschein. Jetzt erst recht von der Neugier getrieben versuchst Du sie mit aller Macht aufzuziehen, doch sie gibt nicht nach, so fest Du auch ziehst. Erschöpft setzt Du Dich vor der Tür auf den Boden und siehst die Tür an. In diesem Moment beginnnt die Tür sich lautlos langsam zu öffnen. Erstaunt blickst Du in das Dunkel, das sich hinter der Tür erstreckt. Zögerlich machst Du ein paar Schritte in den Raum hinein um bei dem schlechten Licht der beiden Fackeln besser sehen zu können. Du blickst Dich rundum einmal in den viereckigen Raum um, der an jeder Seite eine Tür hat, und mit Schrecken merkst Du, dass die Tür, durch die Du gekommen bist, zu ist. Nach einer Weile gibst Du auch diesmal wieder auf diese Tür von Hand zu öffnen. Neben der Tür entdeckst Du an der Wand Schriftzeichen, und obwohl viele Buchstaben teilweise oder sogar ganz fehlen, kannst Du den Satz mit einiger Mühe entschlüsseln: \"Die Pforte nach draußen öffnet sich erst, wenn das Geheimnis der Runen gelöst ist.\" -- \"Also denn.\" Denkst Du Dir und wendest Dich wieder dem Raum zu. Du wollstest doch Abenteuer erleben, doch die Atmosphäre dieses dunklen Raumes ist so bedrohlich, dass Du nur noch inbrünstig hoffst, dass dieses erste Abenteuer nicht auch Dein letztes ist. Du überlegst ob es nicht vielleicht klüger gewesen wäre Deiner Neugier zu trotzen und den Waldweg nicht in Richtung des Pfades zu verlassen, aber nach kurzer Zeit fasst Du den Entschluss, dass darüber nachzudenken Dich jetzt nicht weiterbringt...");
		return begin;
	}
	
	public static final String STRENGTH_KEY = "attr_strength";
	
	public static final String DEXTERITY_KEY = "attr_dexterity";
		
	public static final String PSYCHO_KEY = "attr_psycho";
		
	public static final String AXE_KEY = "attr_axe";
		
	public static final String CLUB_KEY = "attr_club";
	
	public static final String LANCE_KEY =  "attr_lance";
	
	public static final String SWORD_KEY = "attr_sword";
		
	public static final String WOLFKNIFE_KEY = "attr_wolfknife";
		
	public static final String NATURE_KNOWLEDGE_KEY = "attr_nature";
	
	public static final String CREATURE_KNOWLEDGE_KEY = "attr_creature";
		
	public static final String UNDEAD_KNOWLEDGE_KEY = "attr_undead";
	
	public static final String SCOUT_KEY = "attr_scout";
		
	public static  final String THREAT_KEY = "attr_threat";
		
	public static  final String HEALTH_KEY = "attr_health";

	public static  final String OXYGEN_KEY = "attr_oxygen";

	public static  final String DUST_KEY = "attr_dust";
		
	public static final String DUSTREG_KEY = "attr_dustReg";
		
	//public static String HEALTHREG = "";
	
	public static final String BRAVE_KEY = "attr_brave";
	
	public static final String CHANCE_TO_HIT_KEY = "attr_chance_to_hit";

	public static String[] foundItem;

	public static String[] playerDies;

	public static String[] otherDies;
	
	public static String[] disappears;

	public static String[] otherFlees;

	public static String[] otherFleesPanic;

	public static String[] otherFearDies;

	public static String[] otherFearFrozen;

	public static String[] otherFearFrozenLight;

	public static String[] otherFearNo;

	public static String playerThreats;

	public static String[] otherMissesPlayer;

	public static String otherMissesOther;
	
	public static String otherHitsOther;

	public static String[] playerMisses;

	public static String[] otherHitsPlayer;

	public static String[] doesNotWork;

	public static String[] noDust;

	public static String[] notNow;

	public static String[] emptyRoom;

	public static String[] playerShieldBlock;

	public static String[] otherDodges;

	public static String[] playerDodges;
	
    public static final String POTION_STRENGTH_KEY = "use_potion_strength";
	
	public static final String POTION_DEXTERITY_KEY = "use_potion_dexterity";
		
	public static final String POTION_PSYCHO_KEY = "use_potion_psycho";
		

		
	public static  final String POTION_HEALTH_KEY = "use_potion_health";
	public static  final String POTION_OXYGEN_KEY = "use_potion_oxygen";

	public static  final String POTION_DUST_KEY = "use_potion_dust";
		
	public static final String POTION_DUSTREG_KEY = "use_potion_dustReg";

	public static String noWay;

	public static String [] noAP;

	public static String [] notAlone;

	private static ResourceBundle bundle;
	
	public static String [] otherDroppedItem;
	public static String [] levelUp;
	public static String lockDoor;
	public static String unlockDoor;
	
	public static String [] notWithThat;
	public static String [] noKnowledge;
	public static String [] noItem;
	public static String [] wrongTarget;
	public static String [] noTarget;
	public static String [] wrongPosition;
	
	public static String [] playerHits;
	
	public static String [] fightBegins;
	public static String [] fightEnded;
	
	public static String  playerTumbles;
	public static String otherTumbles;
	
	public static String [] otherFleesNot;
	public static String [] playerFleesNot;
	public static String [] playerFlees;

	public static void init() {
		bundle = JDEnv.getResourceBundle();
		otherHitsOther = bundle.getString("other_hits_other");
		otherFleesNot = makeStrings("other_flees_not");
		playerFleesNot = makeStrings("player_flees_not");
		playerFlees = makeStrings("player_flees");
		playerTumbles = bundle.getString("player_tumbling");
		otherTumbles = bundle.getString("other_tumbling");
		fightBegins = makeStrings("fight_begins");
		fightEnded = makeStrings("fight_ended");
		playerHits = makeStrings("player_hits");
		notWithThat = makeStrings("not_with_that");
		noKnowledge = makeStrings("no_knowledge");
		noItem = makeStrings("no_item");
		wrongTarget = makeStrings("wrong_target");
		noTarget = makeStrings("no_target");
	    wrongPosition = makeStrings("wrong_position");
		unlockDoor = bundle.getString("unlock_door");
		lockDoor = bundle.getString("lock_door");
		levelUp = makeStrings("level_up");
		otherDroppedItem = makeStrings("other_dropped_item");
		noAP = makeStrings("no_ap");
		notAlone = makeStrings("found_figure");
		noWay = bundle.getString("no_way");
		foundItem = makeStrings("foundItem");
		playerDies = makeStrings("player_dies");
		otherDies = makeStrings("other_dies");
		disappears = makeStrings("disappears");
		otherFlees = makeStrings("other_flees");
		otherFleesPanic = makeStrings("other_flees_panic");
		otherFearDies = makeStrings("other_fear_dies");
		otherFearFrozen = makeStrings("other_fear_frozen");
		otherFearFrozenLight = makeStrings("other_fear_frozen_light");
		otherFearNo = makeStrings("other_fear_no");
		playerThreats = bundle.getString("player_threats");
		otherMissesPlayer = makeStrings("other_misses_player");
		otherMissesOther = bundle.getString("other_misses_other");
		playerMisses = makeStrings("player_misses");
		otherHitsPlayer = makeStrings("other_hits_player");
		doesNotWork = makeStrings("does_not_work");
		noDust = makeStrings("no_dust");
		notNow = makeStrings("not_now");
		emptyRoom = makeStrings("empty_room");
		playerShieldBlock = makeStrings("player_shield_block");
		otherDodges = makeStrings("other_dodges");
		playerDodges = makeStrings("player_dodges");

	}
	
	
	
	public static String playerFlees() {
		return playerFlees[((int) (Math.random() * playerFlees.length))];

	}
	public static String playerFleesNot() {
		return playerFleesNot[((int) (Math.random() * playerFleesNot.length))];

	}
	public static String otherFleesNot() {
		return otherFleesNot[((int) (Math.random() * otherFleesNot.length))];

	}
	
	public static String fightEnded() {
		return fightEnded[((int) (Math.random() * fightEnded.length))];

	}
	
	public static String fightBegins() {
		return fightBegins[((int) (Math.random() * fightBegins.length))];

	}
	
	public static String playerHits() {
		return playerHits[((int) (Math.random() * playerHits.length))];

	}
	
	public static String notWithThat() {
		return notWithThat[((int) (Math.random() * notWithThat.length))];

	}
	public static String noKnowledge() {
		return noKnowledge[((int) (Math.random() * noKnowledge.length))];

	}
	public static String noItem() {
		return noItem[((int) (Math.random() * noItem.length))];

	}
	public static String noAP() {
		return noAP[((int) (Math.random() * noAP.length))];

	}
	
	public static String wrongTarget() {
		return wrongTarget[((int) (Math.random() * wrongTarget.length))];

	}

	public static String noTarget() {
		return noTarget[((int) (Math.random() * noTarget.length))];

	}

	public static String wrongPosition() {
		return wrongPosition[((int) (Math.random() * wrongPosition.length))];

	}
	
	public static String otherDroppedItem() {
		return otherDroppedItem[((int) (Math.random() * otherDroppedItem.length))];

	}

	public static String levelUp() {
		return levelUp[((int) (Math.random() * levelUp.length))];

	}

	public static String[] makeStrings(String pattern) {

		boolean hasMore = true;
		List l = new LinkedList();
		int i = 0;
		String s = null;

		while (hasMore) {
			
			
			try {
				s = bundle.getString(pattern + "" + i);
				l.add(s);
			} catch (Exception e) {
				hasMore = false;
			}
			i++;
		}
		if(i == 0) {
			System.out.println("counldn't load propertie: "+pattern);
		}
		String[] res = new String[l.size()];
		i=0;
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			//System.out.println(pattern+" geladen: "+element);
			res[i] = element;
			i++;
		}
		return res;
	}

	public static String foundItem() {
		return foundItem[((int) (Math.random() * foundItem.length))];

	}
	
	public static String foundFigure()  {
		return notAlone[((int) (Math.random() * notAlone.length))];
	}
	
	public static String playerDies() {
		return playerDies[((int) (Math.random() * playerDies.length))];

	}
	public static String otherDies() {
		return otherDies[((int) (Math.random() * otherDies.length))];
	}
	
	public static String disappears() {
		return disappears[((int) (Math.random() * otherDies.length))];
	}
	
	
	public static String otherFlees() {
		return otherFlees[((int) (Math.random() * otherFlees.length))];

	}
	public static String otherFleesPanic() {
		return otherFleesPanic[((int) (Math.random() * otherFleesPanic.length))];

	}
	
	public static String otherFearDies() {
		return otherFearDies[((int) (Math.random() * otherFearDies.length))];

	}
	
	public static String otherFearFrozen() {
		return otherFearFrozen[((int) (Math.random() * otherFearFrozen.length))];

	}
	public static String otherFearFrozenLight() {
		return otherFearFrozenLight[((int) (Math.random() * otherFearFrozenLight.length))];

	}
	public static String otherFearNo() {
		return otherFearNo[((int) (Math.random() * otherFearNo.length))];

	}
	public static String otherMissesPlayer() {
		return otherMissesPlayer[((int) (Math.random() * otherMissesPlayer.length))];

	}
	public static String playerMisses() {
		return playerMisses[((int) (Math.random() * playerMisses.length))];

	}
	public static String otherHitsPlayer() {
		return otherHitsPlayer[((int) (Math.random() * otherHitsPlayer.length))];

	}
	public static String doesNotWork() {
		return doesNotWork[((int) (Math.random() * doesNotWork.length))];

	}
	
	public static String noDust() {
		return noDust[((int) (Math.random() * noDust.length))];

	}
	public static String notNow() {
		return notNow[((int) (Math.random() * notNow.length))];

	}
	public static String emptyRoom() {
		return emptyRoom[((int) (Math.random() * emptyRoom.length))];

	}
	
	public static String playerShieldBlock() {
		return playerShieldBlock[((int) (Math.random() * playerShieldBlock.length))];

	}
	
	public static String otherDodges() {
		return otherDodges[((int) (Math.random() * otherDodges.length))];

	}
	public static String playerDodges() {
		return playerDodges[((int) (Math.random() * playerDodges.length))];

	}
	

	public static String toString(LinkedList l) {
		String s = new String();
		int length = l.size();
		if (length > 0) {
			int k = 0;
			while (k < length) {
				s += (((Figure) l.get(k)).toString() + ", ");
				k++;
			}
			return s;
		} else
			return ("empty list");
	}
	
	public static String getString(String key) {
		return bundle.getString(key);
	}


	private static final String[] wolves = { "Rasjim", "Marabin", "Firin", "Forin",
			"Filkin", "Verin", "Djorin", "Lutjin", "Lojart" };

	private static final String[] bears = { "Balum", "Burmet", "Baril", "Banur",
			"Barnim", "Baribal" };

	private static final String[] orcs = { "Grom", "Silmo", "Kos", "Bafög", "Ruz",
			"Nomos", "Gon", "Orlot", "Gil", "Fom", "Orsil", "Gogol", "Hamon",
			"Fuga", "Komi", "Aargau" };

	private static final String[] ogres = { "Gul`Dan", "Gre`Tol", "Han`Tar",
			"Mre`Halon", "Ma`Negunz", "Gha`Sal", "Khe`Tal", "Khaz`Naleh",
			"Tar`Malkhen", "Mhen`Uor", "Thar`He", "Nir`Bahar" };

	private static final String[] skeletons = { "Karakzun", "Girkonras", "Rakzan",
			"Cknobber", "Razzek", "Urzep", "Venkzar", "Chazdir", "Nogaz",
			"Rutzif", "Chozgori" };

	private static final String[] ghuls = { "Denator", "Anator", "Helator",
			"Kilanor", "Talior", "Tawenor", "Draganor", "Salachor", "Rakumor",
			"Serapor", "Khalcedor" };

	// "Nidwaldon"(wolf)
	public static String getName(String code) {

		String name = new String("fehler...");
		if (code.equals("wolf")) {
			int k = (int) (Math.random() * wolves.length);
			name = wolves[k];
		} else if (code.equals("bear")) {
			int k = (int) (Math.random() * bears.length);
			name = bears[k];
		} else if (code.equals("orc")) {
			int k = (int) (Math.random() * orcs.length);
			name = orcs[k];
		} else if (code.equals("ogre")) {
			int k = (int) (Math.random() * ogres.length);
			name = ogres[k];
		} else if (code.equals("skeleton")) {
			int k = (int) (Math.random() * skeletons.length);
			name = skeletons[k];
		} else if (code.equals("ghul")) {
			int k = (int) (Math.random() * ghuls.length);
			name = ghuls[k];
		}
		return name;
	}

	public static String shrine(ShrineInfo s) {
		String info = new String("");
		if (s.getShrineIndex() == Shrine.SHRINE_BROOD) {
			if (s.getType() == Brood.BROOD_NATURE) {
				info = (bundle.getString("see_brood_nature"));
			}
			if (s.getType() == Brood.BROOD_CREATURE) {
				info = (bundle.getString("see_brood_creature"));
			}
			if (s.getType() == Brood.BROOD_UNDEAD) {
				info = (bundle.getString("see_brood_undead"));
			}
		}

		if (s.getShrineIndex() == Shrine.SHRINE_RUNE) {
			info = (bundle.getString("see_rune_shrine"));
		}
		if (s.getShrineIndex() == Shrine.SHRINE_HEALTH_FOUNTAIN) {
			info = (bundle.getString("see_health_fountain"));
		}

		return info;

	}
	
	public static String getAttributeName(Attribute.Type attrKey) {
	if(attrKey == Attribute.Type.Strength) {
		return getString(STRENGTH_KEY);
	}
	else if(attrKey == Attribute.Type.Dexterity) {
		return getString(DEXTERITY_KEY);
	}
	else if(attrKey == Attribute.Type.Psycho) {
		return getString(PSYCHO_KEY);
	}
	else if(attrKey == Attribute.Type.Dust) {
		return getString(DUST_KEY);
	}
	else if(attrKey == Attribute.Type.DustReg) {
		return getString(DUSTREG_KEY);
	}
	else if(attrKey == Attribute.Type.Health) {
		return getString(HEALTH_KEY);
	}
	else if(attrKey == Attribute.Type.Oxygen) {
		return getString(OXYGEN_KEY);
	}
		return "Attribute name not found!";
	}
	
	public static String getPoitionDrinkString(Attribute.Type attrKey) {
	if(attrKey == Attribute.Type.Strength) {
		return getString(POTION_STRENGTH_KEY);
	}
	else if(attrKey == Attribute.Type.Dexterity) {
		return getString(POTION_DEXTERITY_KEY);
	}
	else if(attrKey == Attribute.Type.Psycho) {
		return getString(POTION_PSYCHO_KEY);
	}
	else if(attrKey == Attribute.Type.Dust) {
		return getString(POTION_DUST_KEY);
	}
	else if(attrKey == Attribute.Type.DustReg) {
		return getString(POTION_DUSTREG_KEY);
	}
	else if(attrKey == Attribute.Type.Health) {
		return getString(POTION_HEALTH_KEY);
	}
	else if(attrKey == Attribute.Type.Oxygen) {
		return getString(POTION_OXYGEN_KEY);
	}
	
		return "Attribute potion String not found!";
	}



	public static String respawnByPrayer() {
		return getString("respawn_prayer");
	}

}
