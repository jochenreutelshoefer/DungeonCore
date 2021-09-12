package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.FigureControl;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.npc.DefaultNPCFactory;
import de.jdungeon.figure.npc.RescuedNPCAI;
import de.jdungeon.item.equipment.weapon.Wolfknife;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;

public class RescueCharacterLocationBuilder extends AbstractLocationBuilder{

	private LocationBuilder exit;

	private int figureIndex;

	public RescueCharacterLocationBuilder(LocationBuilder exit) {
		this.exit = exit;
	}

	@Override
	public String getIdentifier() {
		// as we will have only one per level we can use the class name
		return this.getClass().getSimpleName();
	}

	@Override
	public int getBuildPriority() {
		return 1;
	}

	public int getFigureIndex() {
		return figureIndex;
	}

	@Override
	public void insert(Dungeon dungeon, int x, int y) {
		Hero npc = getRescueFigure(dungeon);
		Room rescueFigureRoom = dungeon.getRoom(x, y);
		rescueFigureRoom.figureEntersDungeonHere(npc, 0, -1);
		this.figureIndex = npc.getFigureID();

		JDPoint exitPosition = exit.getRoomPosition();
		Room exitRoom = dungeon.getRoom(exitPosition);
		Location location = exitRoom.getLocation();
		
		if(! (location instanceof LevelExit)) {
			Log.severe("No level exit here!");
		} else {
			((LevelExit)location).setRequiredFigure(npc);
		}
	}

	private Hero getRescueFigure(Dungeon dungeon) {
		Hero npc = DefaultNPCFactory.createDefaultNPC("Willi", Hero.HEROCODE_MAGE);
		npc.takeItem(new Wolfknife(25, false));
		npc.createVisibilityMap(dungeon);
		FigureInfo npcInfo = FigureInfo.makeFigureInfo(npc, npc.getViwMap());
		RescuedNPCAI ai = new RescuedNPCAI();
		ai.setFigure(npcInfo);
		npc.setControl(new FigureControl(npcInfo, ai));
		return npc;
	}
}
