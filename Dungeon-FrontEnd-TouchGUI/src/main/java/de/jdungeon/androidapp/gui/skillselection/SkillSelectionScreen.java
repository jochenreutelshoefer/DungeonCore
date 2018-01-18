package de.jdungeon.androidapp.gui.skillselection;

import dungeon.JDPoint;
import spell.Spell;
import user.DungeonSession;
import util.JDDimension;

import de.jdungeon.androidapp.screen.start.MenuScreen;
import de.jdungeon.game.Game;
import de.jdungeon.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class SkillSelectionScreen extends MenuScreen {

	private final DungeonSession session;

	public SkillSelectionScreen(Game game) {
		super(game);
		session = (DungeonSession)game.getSession();

		Pair<Spell, Spell> options = SkillSelectionManager.getInstance().getOptions(session.getCurrentStage());

		this.guiElements.add(addTile(options.getA(), true));
		this.guiElements.add(addTile(options.getB(), false));
	}

	private SkillSelectionTile addTile(Spell option, boolean rightHandSide) {
		int posY = this.game.getScreenHeight() / 4;
		int posX = rightHandSide?this.game.getScreenWidth() / 2 + this.game.getScreenWidth() / 12:this.game.getScreenWidth() / 12;
		int width = this.game.getScreenWidth() /3;
		int height = this.game.getScreenHeight() * 2 / 3;
		return new SkillSelectionTile(new JDPoint(posX, posY), new JDDimension(width,  height), this, this.game, option);
	}

	@Override
	protected String getHeaderString() {
		return "Neue Fertigkeit erlernen";
	}
}