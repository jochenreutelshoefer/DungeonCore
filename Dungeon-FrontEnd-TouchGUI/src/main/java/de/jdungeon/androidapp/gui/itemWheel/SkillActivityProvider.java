package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.InfoEntity;
import spell.SpellInfo;
import spell.TargetScope;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.gui.SkillImageManager;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Image;

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

	private final SkillImageManager skillImageManager;

	public SkillActivityProvider(HeroInfo info, GameScreen screen) {
		super();
		this.info = info;
		this.screen = screen;
		updateActivityList(false);
		skillImageManager = new SkillImageManager(screen.getGuiImageManager());
	}

	@Override
	public List<ItemWheelActivity> getActivities() {
		Boolean currentFightstate = info.getRoomInfo().fightRunning();
		if (currentFightstate != null && currentFightstate == fightState) {
			return activityCache;
		}
		if (currentFightstate == null) {
			fightState = false;
		}
		else {
			fightState = currentFightstate;
		}
		updateActivityList(fightState);
		return activityCache;
	}

	private void updateActivityList(Boolean currentFightstate) {
		activityCache.clear();
		if (currentFightstate != null && currentFightstate) {
			activityCache.add(attack);
			activityCache.add(flee);
		}
		else {
			activityCache.add(scout);
		}

		List<SpellInfo> spells = info.getSpells();
		for (SpellInfo spell : spells) {
			if (currentFightstate && spell.isFight()) {
				activityCache.add(new ItemWheelActivity(spell));
			}
			if (!currentFightstate && spell.isNormal()) {
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
		image = skillImageManager.getSkillImage(a);
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
			List<FigureInfo> hostileFigures = new ArrayList<>();
			for (FigureInfo figureInfo : figureInfos) {
				if (figureInfo.isHostile(info)) {
					hostileFigures.add(figureInfo);
				}
			}
			if (hostileFigures.size() == 1
					&& (!(highlightedEntity instanceof FigureInfo))) {
				FigureInfo target = hostileFigures.get(0);
				screen.getControl().getActionAssembler().wannaAttack(target);
				screen.setHighlightedEntity(target);
				screen.setInfoEntity(target);
			}
			if (highlightedEntity instanceof FigureInfo) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getControl().getActionAssembler()
						.wannaAttack((FigureInfo) highlightedEntity);
			}
		}
		else if (o.equals(SCOUT)) {
			PositionInRoomInfo pos = info.getPos();
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
			if (possibleFleeDirection != null) {
				DoorInfo door = info.getRoomInfo().getDoor(
						possibleFleeDirection);
				if (door != null) {
					AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
					screen.getControl().getActionAssembler()
							.wannaScout(possibleFleeDirection);
				}
			}
			else if (highlightedEntity instanceof RoomInfo) {
				int directionToScout = Dir.getDirFromToIfNeighbour(
						info.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getControl().getActionAssembler()
						.wannaScout(directionToScout);
			}
			else if (highlightedEntity instanceof DoorInfo) {
				int directionToScout = ((DoorInfo) highlightedEntity)
						.getDir(info.getRoomNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getControl().getActionAssembler()
						.wannaScout(directionToScout);
			}

		}
		else if (o.equals(WALK)) {
			if (highlightedEntity instanceof PositionInRoomInfo) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getControl()
						.getActionAssembler()
						.wannaStepToPosition(
								((PositionInRoomInfo) highlightedEntity));
			}
			if (highlightedEntity instanceof RoomInfo) {
				int directionToWalk = Dir.getDirFromToIfNeighbour(
						info.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getControl().getActionAssembler()
						.wannaWalk(directionToWalk);
			}
		}
		else if (o.equals(FLEE)) {
			PositionInRoomInfo pos = info.getPos();
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
			if (possibleFleeDirection != null) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getControl().getActionAssembler().wannaFlee();
			}
		}
		else if (o instanceof SpellInfo) {
			SpellInfo spell = ((SpellInfo) o);
			if (spell.needsTarget()) {
				TargetScope targetScope = spell.getTargetScope();
				List<? extends InfoEntity> targetEntitiesInScope = targetScope.getTargetEntitiesInScope(info);
				Set<Class<? extends InfoEntity>> classes = getEntityClasses(targetEntitiesInScope);
				if (highlightedEntity != null && !classes.contains(highlightedEntity.getClass())) {
					// something completely wrong for this spell is selected by the user in the gui
					// we discard the selection and see whether auto target detection will work
					// or otherwise the user will be informed
					screen.setHighlightedEntity(null);
				}
				if (highlightedEntity != null) {
					// some target selected
					if (spell.getTargetClass().isAssignableFrom(highlightedEntity.getClass())) {
						// target has matching object class
						screen.setInfoEntity(highlightedEntity);
						AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
						screen.getControl().getActionAssembler()
								.wannaSpell(spell, highlightedEntity);
					}
					else {

						//TODO: what should happen if something wrong is selected
						// and a unique target is available? Change target or wrong target feedback ?
						// should the gui remember the last selected entity for each entity class ? (Figure, Door, Item, etc)
						// NOTE: currently if a different entity class is selected, that selection is cleared, but if an object
						// of correct entity class is selected but not being in scope, we process and core will generated a message
						// for the user accordingly
					}
				}
				else {
					// no target selected
					if (targetEntitiesInScope.size() == 1) {
						InfoEntity targetEntity = targetEntitiesInScope.get(0);
						screen.setInfoEntity(targetEntity);
						screen.setHighlightedEntity(targetEntity);
						screen.getControl().getActionAssembler()
								.wannaSpell(spell, screen.getHighlightedEntity());
					}
					else {
						// we leave the message handling up to the core action handling triggering
						// an action with target null
						screen.getControl().getActionAssembler()
								.wannaSpell(spell, null);
					}
				}

			}

			/*
			Class<? extends InfoEntity> targetClass = spell.getTargetClass();

			InfoEntity uniqueTargetEntity = screen.getControl()
					.getUniqueTargetEntity(targetClass);
			if (uniqueTargetEntity != null) {
				screen.setHighlightedEntity(uniqueTargetEntity);
				screen.setInfoEntity(uniqueTargetEntity);

				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getControl().getActionAssembler()
						.wannaSpell(spell, uniqueTargetEntity);
			}
			else {
				if (highlightedEntity != null && targetClass.isAssignableFrom(highlightedEntity.getClass())) {

					AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
					screen.getControl().getActionAssembler()
							.wannaSpell(spell, highlightedEntity);
				}
				else {
					// TODO: screen.highlightOptions(targetClass);
				}

			}
*/
		}
	}

	private Set<Class<? extends InfoEntity>> getEntityClasses(List<? extends InfoEntity> targetEntitiesInScope) {
		Set<Class<? extends InfoEntity>> result = new HashSet<>();
		if(targetEntitiesInScope == null) {
			return result;
		}
		for (InfoEntity infoEntity : targetEntitiesInScope) {
			if(infoEntity != null) {
				result.add(infoEntity.getClass());
			}
		}
		return result;
	}

}
