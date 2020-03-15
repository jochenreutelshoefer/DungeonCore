
package figure.monster;

import java.util.LinkedList;

import dungeon.Room;
import dungeon.util.RouteInstruction;
import fight.Slap;
import figure.Figure;
import figure.action.Action;
import figure.attribute.Attribute;
import game.DungeonGame;

/**
 * Endgegner von Sektor 1. Wird herbeigerufen von verletzten Monstern. Ist allwissend.
 * Soll nur einmal vorkommen.
 */
public class DarkMaster extends Monster {

	
	Room base;
	private static final int HEALTH_DAMAGE_BALANCE = 10;
	public static final int CHANCE_TO_HIT = 25;
	protected static final int SCATTER = 3;
	boolean beam;
	LinkedList wayToHero;
	int beamCount = 5;

	public DarkMaster(Room baseRoom) {

		super(3000);
		tumbleValue = 0;
		// TODO Auto-generated constructor stub
		name = "Dunkler Meister";
		int value = 2500;
		int HealthI = value / (50 + (int) (Math.random() * 10));
		this.strength = new Attribute(Attribute.STRENGTH,8);
		value = value / HealthI;
		health = new Attribute((Attribute.HEALTH), HealthI);
		psycho = new Attribute((Attribute.PSYCHO), 20);
		int average = value / (3 + (int) (Math.random() * 4));
		int scatter = 1 + (int) (Math.random() * (average / 4));
		minDamage = average - scatter;
		maxDamage = average + scatter;
		//	value = value / average;
		chanceToHit =
			new Attribute(
				(Attribute.CHANCE_TO_HIT),
				(int) (((float) value / average) * 6));
		//(100));

		base = baseRoom;
	}

	/* (non-Javadoc)
	 * @see monster#hunting()
	 */
	@Override
	public int hunting() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAntiFleeFactor() {
		return 0.5;
	}

	public void printRoomList(LinkedList l) {
		//System.out.println("WEG DES MEISTERS!");
		for (int i = 0; i < l.size(); i++) {
			//System.out.println(
			//	"Raum: " + ((room) l.get(i)).getNumber().toString());
		}

	}

	@Override
	protected boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();
		attack(op);
		attack(op);

		this.specialAttackCounter = 50;
		return false;
	}

	private void getHeroRouteInstruction() {
//		System.out.println("Meister Routing neu push hole HeroRoom!");
//		routing = new Stack();
//		Room heroRoom = getGame().getHero().getRoom();
//		routing.push(new RouteInstruction(heroRoom));
	}

	public void turn(int round) {

		int i = 0;
		if (routing.empty()) {
			getHeroRouteInstruction();
		} else {
			RouteInstruction ri = ((RouteInstruction) routing.peek());
			System.out.println("Meister Routingn : " + ri.toString());
			boolean direction = false;
			if ((ri.getDestination() != null)) {
				if (ri.getDestination() == this.getRoom()) {
					System.out.println("Meister angekommen -> pop");
					routing.pop();
					if (!routing.empty()) {

						ri = (RouteInstruction) routing.peek();
					}
				} else {

					i = ri.getWay(actualDungeon.getRoom(location), this.visibilities);
					System.out.println("Getting way:" + i);
				}
			} else {
				i = ri.getDirection();
				direction = true;

			}

			boolean marched = walk(RouteInstruction.Direction.fromInteger(i));
			if (!marched) {
				this.getHeroRouteInstruction();
			}
			if (direction && marched) {
				routing.pop();
			}

		}
	}
	
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}
	
	@Override
	protected int getSCATTER() {
		return this.SCATTER;
	}


	public void unsetWay() {

		wayToHero = null;
	}
	
	@Override
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}

	@Override
	public Slap slay(Figure m) {
		return null;
//		if (m == null) {
//			//System.out.println("ziel ist null!");
//		}
//		double tumbleFactor = 1;
//		if (this.raiding) {
//			raiding = false;
//			this.double_bonus = true;
//			tumbleFactor = 4;
//		}
//
//		int value;
//		//decFightAP(1);
//		float actual_chance_to_hit = getActual_chance_to_hit(m);
//		if (m == null) {
//			//System.out.println("ziel ist null!");
//		}
//		int elude = m.getElude(this);
//		if(!beam) {
//		
//		int random = (int) (Math.random() * 100);
//		if (actual_chance_to_hit - elude >= random) {
//
//			value = getSlapStrength(m);
//		} else {
//			if (actual_chance_to_hit >= random) {
//				//ausgewichen
//				//getGame().newStatement(Texts.elude(m), 0);
//			} else {
//				//getGame().newStatement(Texts.misses(this, m), 0);
//				//if(!(m instanceof hero) {
//
//				//}
//			}
//
//			value = 0;
//		}
//		} else {
//			value = (int)(m.getAttribute(Attribute.HEALTH).getValue() / 2);
//		}
//		if (elude == -1) {
//			//schildblock
//			value = 0;
//		}
//		if (elude == 0) {
//			//gegner gel�hmt - voller Schlag
//			value = getSlapStrength(m);
//		}
//
//		if (half_bonus) {
//			half_bonus = false;
//			value *= 1.5;
//		}
//		if (double_bonus) {
//			double_bonus = false;
//			value *= 2;
//		}
//
//		return new Slap(
//			this,m,
//			0,
//			Slap.MIXED,
//			value,
//			(int) (tumbleFactor * getTumbleValue(m)),this.getPositionInRoom());
	}
	
	public Action turnElse(int a) {
		return null;
	}

//	public boolean fight(/*Fight w*/) {
//
//		if (!routing.empty()) {
//			routing.pop();
//		}
//
//		if (justGotSlap) {
//			//w.newStatement(getName() + getStatus(), 0);
//			if (Math.random() < 0.5) {
//				break_special_attack();
//			}
//			justGotSlap = false;
//		}
//		//System.out.println("das Monster k�mpft");
//		if (dead) {
//			game.newStatement("stirbt jetzt erstmal gleich!", 2);
//			getKilled();
//			return true;
//		}
//		boolean exitsFight = false;
//		incFightAP(1);
//		System.out.println(getName() + " fightAP:" + fightAP);
//		int level = calcFearLevel();
//		////System.out.println("Angstlevel: "+Integer.toString(level));
//
//		if (blinded > 0) {
//			blinded--;
//			game.newStatement(getName() + " ist geblendet!", 2);
//			break_special_attack();
//			decFightAP(1);
//			return false;
//		}
//
//		if ((fightAP > 0) && (dead == false) && (hasJustFleen == false)) {
//			this.decFightAP(1);
//
//			Fighter op = getTarget();
//			Monster m = base.getSec().getRandomSectorMonster(0);
//		
//			beam = false;
//			if (m != null && m != this && beamCount > 0) {
//				beamCount--;
//				newStatement("HAHAHAHA, stirb sterblicher Eindringling!", 3);
//				beam = true;
//				attack(op);
//				newStatement(
//					"Hmm, ich werde Dich sp�ter t�ten, bis dahin musst Du mit meinen Dienern vorlieb nehmen!\n"
//						+ "Aber ich komme wieder!",
//					3);
//				Room here = getRoom();
//				getHome().getRoom(m.getLocation()).remMonster(m);
//				here.remMonster(this);
//				here.addMonster(m);
//				base.addMonster(this);
//				beam = true;
//
//				//System.out.println("Jetzt in : " + m.getRoom().toString());
//			} else {
//				attack(op);
//			}
//
//		}
//
//		return exitsFight;
//	}

	//	public boolean fight(fight w) {
	//		
	//		if(!routing.empty()) {
	//			routing.pop();
	//		}
	//		if (justGotSlap) {
	//			//w.newStatement(getName() + getStatus(), 0);
	//			justGotSlap = false;
	//		}
	//		//System.out.println("das Monster k�mpft");
	//		if (dead) {
	//			w.newStatement("stirbt jetzt erstmal gleich!", 2);
	//			getKilled(w);
	//			return true;
	//		}
	//		boolean exitsFight = false;
	//		fightAP++;
	//		int level = 0;
	//		////System.out.println("Mut: "+Integer.toString(brave.getValue()));
	//		int handycap = 50;
	//		int k = (int) brave.getValue();
	//		if (k == 0)
	//			k = 1;
	//		for (int i = 0;
	//			i < k;
	//			i++) { //brave gibt den Wert wie oft gepr�ft wird, der beste Wert wird genommen
	//			int a = fleeHelp(handycap);
	//			////System.out.println("Durchlauf: "+Integer.toString(i)+" Wert: "+Integer.toString(a));
	//			if (a > level)
	//				level = a;
	//		}
	//		if (thundered) {
	//			level = 0;
	//			thundered = false;
	//		}
	//
	//		getGame().newStatement(
	//			getName() + " wiedersteht Furcht mit: " + level,
	//			4);
	//		////System.out.println("Angstlevel: "+Integer.toString(level));
	//		if (hasJustBeenThreaten) {
	//			getGame().newStatement(name + ": Wurde gerade bedroht... ", 4);
	//			if (level == 0) {
	//				//System.out.println("getKilled()!");
	//				int v = getKilled(w);
	//				exitsFight = true;
	//				w.giveHeroExp(v, this);
	//				w.h.incKills();
	//				w.newStatement(getName() + texts.flee(0), 0);
	//				getGame().newStatement(" Level ist 0 -> stirbt! ", 4);
	//			} else if (level == 1) {
	//				looseItems();
	//				flee(w);
	//				exitsFight = true;
	//				w.newStatement(getName() + texts.flee(1), 2);
	//				getGame().newStatement(
	//					" Level ist 1 -> Gegenst�nde verlieren & fliehen... ",
	//					4);
	//			} else if (level == 2) {
	//				flee(w);
	//				exitsFight = true;
	//				w.newStatement(getName() + texts.flee(2), 2);
	//				getGame().newStatement(" Level ist 2 -> flieht... ", 4);
	//			} else if (level == 3) {
	//				int r = ((int) (Math.random() * 5)) + 2;
	//				decFightAP(r);
	//				w.newStatement(getName() + texts.flee(3), 2);
	//				getGame().newStatement(" Level ist 3 -> wird gel�hmt:" + r, 4);
	//			} else if (level == 4) {
	//				int r = ((int) (Math.random() * 3)) + 1;
	//				decFightAP(r);
	//				w.newStatement(getName() + texts.flee(4), 2);
	//				getGame().newStatement(" Level ist 4 -> wird gel�hmt:" + r, 4);
	//			} else if (level == 5) {
	//				w.newStatement(getName() + texts.flee(5), 1);
	//				getGame().newStatement(" Level ist 5 -> k�mpft weiter", 4);
	//			}
	//
	//			if (brave.getValue() < brave.getBasic()) {
	//				if ((int) (Math.random() * 1) < 1) {
	//					brave.modValue(1);
	//					getGame().newStatement(" Mut um 1 erh�ht", 4);
	//				}
	//			}
	//		} else {
	//			if (level == 0) {
	//				flee(w);
	//				exitsFight = true;
	//				w.newStatement(getName() + texts.flee(2), 2);
	//				getGame().newStatement(name + " hat level 0 und flieht...", 4);
	//				return exitsFight;
	//			}
	//		}
	//		hasJustBeenThreaten = false;
	//		if (blinded > 0) {
	//			blinded--;
	//			game.newStatement(getName() + " ist geblendet!", 2);
	//			decFightAP(1);
	//			return false;
	//		}
	//
	//		if ((fightAP > 0) && (dead == false) && (hasJustFleen == false)) {
	//			fighter op = null;
	//			boolean otherMonster = false;
	//			if (convinced > 0) {
	//				//System.out.println(getName()+ " convinced > 0");
	//				LinkedList list = w.getMonstersL();
	//				//System.out.println("anzahl monster: "+list.size());
	//				if (list.size() > 1) {
	//					int zahl =
	//						(int) ((Math.random()) * w.getMonstersL().size());
	//					//System.out.println("random:"+zahl);
	//					monster m = (monster) w.getMonstersL().get(zahl);
	//					while (m == this) {
	//						zahl =
	//							(int) ((Math.random()) * w.getMonstersL().size());
	//						m = (monster) w.getMonstersL().get(zahl);
	//
	//					}
	//					op = m;
	//					//System.out.println("gegner: "+op.getName());
	//					otherMonster = true;
	//				} else {
	//					op = (fighter) w.getHeroL().getFirst();
	//				}
	//				convinced -= 1;
	//			} else {
	//				op = (fighter) w.getHeroL().getFirst();
	//			}
	//
	//			monster m = base.getSec().getRandomSectorMonster(0);
	////			System.out.println(
	////				"Auserw�hltes Monster!: "
	////					+ m.toString()
	////					+ "in: "
	////					+ m.getRoom().toString());
	//			beam = false;
	//			if (m != null && m != this) {
	//				w.newStatement("HAHAHAHA, stirb sterblicher Eindringling!", 3);
	//				room here = getRoom();
	//				getHome().getRoom(m.getLocation()).remMonster(m);
	//				here.remMonster(this);
	//				here.addMonster(m);
	//				base.addMonster(this);
	//				beam = true;
	//
	//				//System.out.println("Jetzt in : " + m.getRoom().toString());
	//			}
	//
	//			slap u = slay(op);
	//			if (beam) {
	//
	//				w.newStatement(
	//					"Hmm, ich werde Dich sp�ter t�ten, bis dahin musst Du mit meinen Dienern vorlieb nehmen!\n"
	//						+ "Aber ich komme wieder!",
	//					3);
	//			}
	//			if (u.getValue_standard() == 0) {
	//				//getGame().newStatement(this.getName() + texts.misses(this), 0);
	//			} else {
	//				if (otherMonster) {
	//					getGame().newStatement(
	//						this.getName()
	//							+ " greift "
	//							+ op.getName()
	//							+ " an. ("
	//							+ u.getValue_standard()
	//							+ ")",
	//						2);
	//				} else {
	//					getGame().newStatement(
	//						this.getName()
	//							+ texts.attacks()
	//							+ " ("
	//							+ u.getValue_standard()
	//							+ ") ",
	//						1);
	//				}
	//
	//			}
	//
	//			slapResult res = op.getSlap(u);
	//			if (res.isLethal()) {
	//				op.getKilled();
	//				if (op instanceof hero) {
	//					getGame().newStatement(texts.dies(op), 1);
	//					getGame().gameOver();
	//				}
	//			}
	//			if (otherMonster) {
	//				getGame().getHero().receiveSlapResult(res);
	//			}
	//
	//		} else {
	//			getGame().newStatement(
	//				"Keine AP oder tot oder geflohen! " + getFightAP(),
	//				4);
	//		}
	//		hasJustFleen = false;
	//		return exitsFight;
	//	}

	//	public slap slay(fighter m) {
	//		if (m == null) {
	//			//System.out.println("ziel ist null!");
	//		}
	//		int value;
	//
	//		//		MEGA HIT BEIM WEGBEAMEN
	//		if (beam) {
	//			value = game.getHero().getHealth_Value() * 3 / 5;
	//		} else {
	//
	//			decFightAP(1);
	//			float actual_chance_to_hit = 100; // = getActual_chance_to_hit(m);
	//			if (m == null) {
	//				//System.out.println("ziel ist null!");
	//			}
	//			int elude = m.getElude(this);
	//			if (elude == -1) {
	//				//schildblock
	//				value = 0;
	//			}
	//			if (elude == 0) {
	//				//gegner gel�hmt - voller Schlag
	//				value = getSlapStrength(m);
	//			}
	//			int random = (int) (Math.random() * 100);
	//			if (actual_chance_to_hit - elude >= random) {
	//
	//				value = getSlapStrength(m);
	//			} else {
	//				if (actual_chance_to_hit >= random) {
	//					//ausgewichen
	//					getGame().newStatement(texts.elude(m), 0);
	//				} else {
	//					getGame().newStatement(texts.misses(this, m), 0);
	//					//if(!(m instanceof hero) {
	//
	//					//}
	//				}
	//
	//				value = 0;
	//			}
	//			if (double_bonus) {
	//				double_bonus = false;
	//				value *= 2;
	//			}
	//		}
	//
	//		return new slap(this, (int) (1.5 * value), slap.STANDARD, 0, 0);
	//	}

}
