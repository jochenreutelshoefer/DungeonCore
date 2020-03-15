package ai;

import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.Path;
import dungeon.util.DungeonUtils;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.percept.Percept;

public class ChaserAI extends DefaultMonsterIntelligence {

	private final HeroPositionLog heroLog;

	public ChaserAI() {
		heroLog = new HeroPositionLog(this.monster);
	}

	@Override
	public Action chooseFightAction() {

		Action a = this.stepToEnemy();
		if (a != null && Math.random() > 0.3) {
			return a;
		}

		a = this.wannaSpell();
		if (a != null) {
			return a;
		}

		return Action.makeActionAttack(getHeroIndex());
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
			int dir = DungeonUtils.getNeighbourDirectionFromTo(monster.getRoomNumber(), nextRoomToGo.getNumber()).getValue();
			return walk(dir);
		}

		return new EndRoundAction();
	}
}
