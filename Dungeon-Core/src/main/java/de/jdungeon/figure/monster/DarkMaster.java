
package de.jdungeon.figure.monster;

import java.util.LinkedList;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.game.GameLoopMode;

/**
 * Endgegner von Sektor 1. Wird herbeigerufen von verletzten Monstern. Ist allwissend.
 * Soll nur einmal vorkommen.
 */
@Deprecated
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
		// TODO Auto-generated constructor stub
		name = "Dunkler Meister";
		int value = 2500;
		int HealthI = value / (50 + (int) (Math.random() * 10));
		this.strength = new Attribute(Attribute.Type.Strength,8);
		value = value / HealthI;
		health = new Attribute((Attribute.Type.Health), HealthI);
		psycho = new Attribute((Attribute.Type.Psycho), 20);
		int average = value / (3 + (int) (Math.random() * 4));
		int scatter = 1 + (int) (Math.random() * (average / 4));
		minDamage = average - scatter;
		maxDamage = average + scatter;
		//	value = value / average;
		chanceToHit =
			new Attribute(
				(Attribute.Type.OtherDeprecatedAttributeType),
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
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.DarkDwarf;
	}

	@Override
	public double getAntiFleeFactor() {
		return 0.5;
	}

	private void getHeroRouteInstruction() {
//		System.out.println("Meister Routing neu push hole HeroRoom!");
//		routing = new Stack();
//		Room heroRoom = getGame().getHero().getRoom();
//		routing.push(new RouteInstruction(heroRoom));
	}

	@Override
	public void turn(int round, GameLoopMode mode) {
		super.turn(round, mode);
		int i = 0;
		if (routing.empty()) {
			getHeroRouteInstruction();
		} else {
			RouteInstruction ri = ((RouteInstruction) routing.peek());
			System.out.println("Meister Routingn : " + ri.toString());
			boolean direction = false;
			if ((ri.getDestination() != null)) {
				if (ri.getDestination() == this.getRoom().getNumber()) {
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

			boolean marched = walk(RouteInstruction.Direction.fromInteger(i), round);
			if (!marched) {
				this.getHeroRouteInstruction();
			}
			if (direction && marched) {
				routing.pop();
			}

		}
	}
	
	@Override
	protected int getHealthDamageBalance() {
		return this.HEALTH_DAMAGE_BALANCE;
	}
	
	@Override
	protected int getDamageVariance() {
		return this.SCATTER;
	}


	public void unsetWay() {

		wayToHero = null;
	}
	
	@Override
	public int getChangeToHit() {
		return CHANCE_TO_HIT;
	}


}
