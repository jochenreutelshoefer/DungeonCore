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

	@Override
	public void insert(Dungeon dungeon, int x, int y) {
		Hero npc = DefaultNPCFactory.createDefaultNPC("Willi", Hero.HEROCODE_MAGE);
		npc.takeItem(new Wolfknife(25, false));
		npc.createVisibilityMap(dungeon);
		FigureInfo npcInfo = FigureInfo.makeFigureInfo(npc, npc.getViwMap());
		RescuedNPCAI ai = new RescuedNPCAI();
		ai.setFigure(npcInfo);
		npc.setControl(new FigureControl(npcInfo, ai));
		dungeon.getRoom(x,y).figureEnters(npc, 0, -1);
		JDPoint exitPosition = exit.getRoomPosition();
		Room exitRoom = dungeon.getRoom(exitPosition);
		Location location = exitRoom.getLocation();
		if(! (location instanceof LevelExit)) {
			Log.severe("No level exit here!");
		} else {
			((LevelExit)location).setRequiredFigure(npc);
		}
	}
}
