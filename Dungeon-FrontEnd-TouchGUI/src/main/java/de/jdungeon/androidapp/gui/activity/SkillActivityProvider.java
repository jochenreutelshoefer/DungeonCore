package de.jdungeon.androidapp.gui.activity;

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
import event.ExitUsedEvent;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.InfoEntity;
import game.RoomInfoEntity;
import gui.Paragraph;
import spell.SpellInfo;
import spell.TargetScope;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.gui.FocusManager;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.skillselection.SkillImageManager;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

public class SkillActivityProvider implements ActivityProvider {

	public static final String ATTACK = "Angreifen";
	public static final String SCOUT = "Spähen";
	public static final String WALK = "Laufen";
	public static final String FLEE = "Fliehen";
	public static final String LOOK = "Ansehen";

	private final HeroInfo info;
	private final Control actionAssembler;
	private final FocusManager focusManager;
	private boolean fightState = false;
	private final List<Activity> activityCache = new ArrayList<>();

	private final Activity attack = new DefaultActivity(ATTACK);
	private final Activity flee = new DefaultActivity(FLEE);
	//private final Activity scout = new DefaultActivity(SCOUT);

	private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

	private final SkillImageManager skillImageManager;

	public SkillActivityProvider(HeroInfo info, Game game, Control actionAssembler, FocusManager focusManager) {
		super();
		this.info = info;
		this.actionAssembler = actionAssembler;
		this.focusManager = focusManager;
		updateActivityList(false);
		skillImageManager = new SkillImageManager(new GUIImageManager(game.getFileIO().getImageLoader()));
	}

	@Override
	public List<Activity> getActivities() {
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

	private void updateActivityList(Boolean currentFightState) {
		activityCache.clear();
		if (currentFightState != null && currentFightState) {
			activityCache.add(attack);
			activityCache.add(flee);
		}
		else {
			activityCache.add(new AbstractExecutableActivity() {
				@Override
				public void execute() {
					doScout(focusManager.getWorldFocusObject());
				}

				@Override
				public Object getObject() {
					return SCOUT;
				}

				@Override
				public RoomInfoEntity getTarget() {
					return null;
				}

			});
		}

		List<SpellInfo> spells = info.getSpells();
		for (SpellInfo spell : spells) {
			if (currentFightState && spell.isFight()) {
				activityCache.add(new DefaultActivity(spell));
			}
			if (!currentFightState && spell.isNormal()) {
				activityCache.add(new DefaultActivity(spell));
			}
		}
	}

	@Override
	public Image getActivityImage(Activity a) {
		Image image = imageCache.get(a.getObject());
		if (image != null) {
			return image;
		}
		image = skillImageManager.getSkillImage(a);
		imageCache.put(a.getObject(), image);
		return image;
	}

	@Override
	public void activityPressed(Activity activity) {
		if (activity == null) {
			return;
		}

		if(activity instanceof ExecutableActivity) {
			((ExecutableActivity)activity).execute();
			return;
		}

		Object o = activity.getObject();
		RoomInfoEntity highlightedEntity = focusManager.getWorldFocusObject();

		if (o.equals(ATTACK)) {
			if(highlightedEntity instanceof FigureInfo && !((FigureInfo)highlightedEntity).getRoomInfo().getPoint().equals(info.getRoomInfo().getLocation())) {
				// moved out of room since last figure focus hence reset focus
				focusManager.setWorldFocusObject(null);
				highlightedEntity = null;
			}
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
				actionAssembler.wannaAttack(target);
				focusManager.setWorldFocusObject(target);
			}
			if (highlightedEntity instanceof FigureInfo) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actionAssembler
						.wannaAttack((FigureInfo) highlightedEntity);
			}
		}
		else if (o.equals(SCOUT)) {
			doScout(highlightedEntity);

		}
		else if (o.equals(WALK)) {
			if (highlightedEntity instanceof PositionInRoomInfo) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actionAssembler
						.wannaStepToPosition(
								((PositionInRoomInfo) highlightedEntity));
			}
			if (highlightedEntity instanceof RoomInfo) {
				int directionToWalk = Dir.getDirFromToIfNeighbour(
						info.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actionAssembler
						.wannaWalk(directionToWalk);
			}
		}
		else if (o.equals(FLEE)) {
			PositionInRoomInfo pos = info.getPos();
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
			if (possibleFleeDirection != null) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actionAssembler.wannaFlee();
			}
		}
		else if (o instanceof SpellInfo) {
			SpellInfo spell = ((SpellInfo) o);
			if (spell.needsTarget()) {
				TargetScope targetScope = spell.getTargetScope();
				List<? extends RoomInfoEntity> targetEntitiesInScope = targetScope.getTargetEntitiesInScope(info);
				Set<Class<? extends InfoEntity>> classes = getEntityClasses(targetEntitiesInScope);
				if (highlightedEntity != null && !classes.contains(highlightedEntity.getClass())) {
					// something completely wrong for this spell is selected by the user in the gui
					// we discard the selection and see whether auto target detection will work
					// or otherwise the user will be informed
					focusManager.setWorldFocusObject(null);
				}
				if (highlightedEntity != null) {
					// some target selected
					if (spell.getTargetClass().isAssignableFrom(highlightedEntity.getClass())) {
						// target has matching object class
						AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
						actionAssembler
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
						RoomInfoEntity targetEntity = targetEntitiesInScope.get(0);
						focusManager.setWorldFocusObject(targetEntity);
						actionAssembler
								.wannaSpell(spell, targetEntity);
					}
					else {
						// we leave the message handling up to the core action handling triggering
						// an action with target null
						actionAssembler
								.wannaSpell(spell, null);
					}
				}

			} else {
				// no target required
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actionAssembler
						.wannaSpell(spell, null);
			}

			/*
			Class<? extends InfoEntity> targetClass = spell.getTargetClass();

			InfoEntity uniqueTargetEntity = screen.getActionAssembler()
					.getUniqueTargetEntity(targetClass);
			if (uniqueTargetEntity != null) {
				screen.setHighlightedEntity(uniqueTargetEntity);
				screen.setInfoEntity(uniqueTargetEntity);

				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				screen.getActionAssembler().getActionAssembler()
						.wannaSpell(spell, uniqueTargetEntity);
			}
			else {
				if (highlightedEntity != null && targetClass.isAssignableFrom(highlightedEntity.getClass())) {

					AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
					screen.getActionAssembler().getActionAssembler()
							.wannaSpell(spell, highlightedEntity);
				}
				else {
					// TODO: screen.highlightOptions(targetClass);
				}

			}
*/
		}
	}

	private void doScout(RoomInfoEntity highlightedEntity) {
		PositionInRoomInfo pos = info.getPos();
		RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
		if (possibleFleeDirection != null) {
			DoorInfo door = info.getRoomInfo().getDoor(
					possibleFleeDirection);
			if (door != null) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actionAssembler
						.wannaScout(possibleFleeDirection);
			}
		}
		else if (highlightedEntity instanceof RoomInfo) {
			int directionToScout = Dir.getDirFromToIfNeighbour(
					info.getRoomNumber(),
					((RoomInfo) highlightedEntity).getNumber());
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			actionAssembler
					.wannaScout(directionToScout);
		}
		else if (highlightedEntity instanceof DoorInfo) {
			int directionToScout = ((DoorInfo) highlightedEntity)
					.getDir(info.getRoomNumber());
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			actionAssembler
					.wannaScout(directionToScout);
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
