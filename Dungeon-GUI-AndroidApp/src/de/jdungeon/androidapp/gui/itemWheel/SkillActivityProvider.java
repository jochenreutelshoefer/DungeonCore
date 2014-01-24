package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spell.SpellInfo;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.androidapp.gui.SkillImageManager;
import de.jdungeon.game.Image;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.InfoEntity;

public class SkillActivityProvider implements ItemWheelActivityProvider {

	public static final String ATTACK = "Angreifen";
	public static final String SCOUT = "Spähen";
	public static final String WALK = "Laufen";
	public static final String FLEE = "Fliehen";
	public static final String LOOK = "Ansehen";

	private final HeroInfo info;
	private final GameScreen screen;
	private boolean fightState = false;
	private final List<ItemWheelActivity> activityCache = new ArrayList<ItemWheelActivity>();

	private final ItemWheelActivity attack = new ItemWheelActivity(ATTACK);
	private final ItemWheelActivity flee = new ItemWheelActivity(FLEE);
	private final ItemWheelActivity scout = new ItemWheelActivity(SCOUT);

	private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

	public SkillActivityProvider(HeroInfo info, GameScreen screen) {
		super();
		this.info = info;
		this.screen = screen;
		updateActivityList(false);
	}

	@Override
	public List<ItemWheelActivity> getActivities() {
		Boolean currentFightstate = info.getRoomInfo().fightRunning();
		if (currentFightstate == fightState) {
			return activityCache;
		}
		fightState = currentFightstate;
		updateActivityList(currentFightstate);
		return activityCache;
	}

	private void updateActivityList(Boolean currentFightstate) {
		activityCache.clear();
		if (currentFightstate != null && currentFightstate) {
			activityCache.add(attack);
			activityCache.add(flee);
		} else {
			activityCache.add(scout);
		}

		List<SpellInfo> spells = info.getSpells();
		for (SpellInfo spell : spells) {
			if (currentFightstate && spell.isFight()) {
				activityCache.add(new ItemWheelActivity(spell));
			}
			if (currentFightstate == false && spell.isNormal()) {
				activityCache.add(new ItemWheelActivity(spell));
			}
		}
	}

	@Override
	public Image getActivityImage(ItemWheelActivity a) {
		Image image = imageCache.get(a.getObject());
		if (image != null) {
			return image;
		}
		image = SkillImageManager.getSkillImage(a, screen);
		imageCache.put(a.getObject(), image);
		return image;
	}

	@Override
	public void activityPressed(ItemWheelActivity activity) {
		if (activity == null) {
			return;
		}
		Object o = activity.getObject();
		InfoEntity highlightedEntity = screen.getHighlightedEntity();
		if (o.equals(ATTACK)) {
			List<FigureInfo> figureInfos = info.getRoomInfo().getFigureInfos();
			if (figureInfos.size() == 2
					&& (highlightedEntity == null || !(highlightedEntity instanceof FigureInfo))) {
				// remove player
				figureInfos.remove(info);
				// enemy remains
				FigureInfo target = figureInfos.get(0);
				screen.getControl().getActionAssembler().wannaAttack(target);
				screen.setHighlightedEntity(target);
				screen.setInfoEntity(target);
			}
			if (highlightedEntity != null
					&& highlightedEntity instanceof FigureInfo) {
				screen.getControl().getActionAssembler()
						.wannaAttack((FigureInfo) highlightedEntity);
			}
		} else if (o.equals(SCOUT)) {
			PositionInRoomInfo pos = info.getPos();
			int possibleFleeDirection = pos.getPossibleFleeDirection();
			if (possibleFleeDirection > 0) {
				DoorInfo door = info.getRoomInfo().getDoor(
						possibleFleeDirection);
				if (door != null) {
					screen.getControl().getActionAssembler()
							.wannaScout(possibleFleeDirection);
				}
			} else if (highlightedEntity != null
					&& highlightedEntity instanceof RoomInfo) {
				int directionToScout = Dir.getDirFromToIfNeighbour(
						info.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				screen.getControl().getActionAssembler()
						.wannaScout(directionToScout);
			} else if (highlightedEntity != null
					&& highlightedEntity instanceof DoorInfo) {
				int directionToScout = ((DoorInfo) highlightedEntity)
						.getDir(info.getRoomNumber());
				screen.getControl().getActionAssembler()
						.wannaScout(directionToScout);
			}

		} else if (o.equals(WALK)) {
			if (highlightedEntity != null
					&& highlightedEntity instanceof PositionInRoomInfo) {
				screen.getControl()
						.getActionAssembler()
						.wannaStepToPosition(
								((PositionInRoomInfo) highlightedEntity));
			}
			if (highlightedEntity != null
					&& highlightedEntity instanceof RoomInfo) {
				int directionToWalk = Dir.getDirFromToIfNeighbour(
						info.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				screen.getControl().getActionAssembler()
						.wannaWalk(directionToWalk);
			}
		} else if (o.equals(FLEE)) {
			PositionInRoomInfo pos = info.getPos();
			int possibleFleeDirection = pos.getPossibleFleeDirection();
			if (possibleFleeDirection > 0) {
				screen.getControl().getActionAssembler().wannaFlee();
			}
		} else if (o instanceof SpellInfo) {
			SpellInfo spell = ((SpellInfo) o);
			Class<? extends InfoEntity> targetClass = spell.getTargetClass();
			InfoEntity uniqueTargetEntity = screen.getControl()
					.getUniqueTargetEntity(targetClass);
			if (uniqueTargetEntity != null) {
				screen.setHighlightedEntity(uniqueTargetEntity);
				screen.setInfoEntity(uniqueTargetEntity);
				screen.getControl().getActionAssembler()
						.wannaSpell(spell, uniqueTargetEntity);
			} else {
				if (targetClass.isAssignableFrom(highlightedEntity.getClass())) {
					screen.getControl().getActionAssembler()
							.wannaSpell(spell, highlightedEntity);
				} else {
					// TODO: screen.highlightOptions(targetClass);
				}
			}

		}
	}

}
