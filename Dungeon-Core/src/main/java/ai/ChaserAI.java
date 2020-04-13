package ai;

import dungeon.JDPoint;
import dungeon.Path;
import dungeon.RoomInfo;
import dungeon.util.DungeonUtils;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.percept.Percept;
import skill.AttackSkill;

public class ChaserAI extends DefaultMonsterIntelligence {

	private HeroPositionLog heroLog;

	public ChaserAI() {

	}

	@Override
	public void setFigure(FigureInfo info) {
		super.setFigure(info);
		heroLog = new HeroPositionLog(info);
	}

	@Override
	public Action chooseFightAction() {

		Action a = this.stepToEnemy();
		if (a != null && Math.random() > 0.3) {
			return a;
		}
		FigureInfo enemy = getHero();
		if (enemy == null) {
			enemy = getEnemy();
		}
		if (enemy == null) {
			return new EndRoundAction();
		}
		return this.info.getSkill(AttackSkill.class)
				.newActionFor(info)
				.target(enemy)
				.get();
	}

	@Override
	public void processPercept(Percept p) {
		heroLog.tellPecept(p);
	}

	@Override
	public Action chooseMovementAction() {

		heroLog.processPercepts();

		if (!actionQueue.isEmpty()) {
			Action a = actionQueue.remove(0);
			lastAction = a;
			return a;
		}

		JDPoint lastHeroLocation = heroLog.getLastHeroPosition();
		if (lastHeroLocation == null) {
			return new EndRoundAction();
		}
		if (lastHeroLocation.equals(monster.getRoomNumber())) {
			return new EndRoundAction();
		}
		Path l = monster.getShortestWayFromTo(monster.getRoomNumber(), lastHeroLocation);
		if (l != null) {
			RoomInfo nextRoomToGo = l.get(1);
			int dir = DungeonUtils.getNeighbourDirectionFromTo(monster.getRoomNumber(), nextRoomToGo.getNumber())
					.getValue();
			return walk(dir);
		}

		return new EndRoundAction();
	}
}
