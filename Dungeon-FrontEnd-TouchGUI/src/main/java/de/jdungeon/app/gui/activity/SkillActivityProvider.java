package de.jdungeon.app.gui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeon.Dir;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.InfoEntity;
import game.RoomInfoEntity;
import spell.SpellInfo;
import spell.TargetScope;

import de.jdungeon.app.ActionController;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.app.gui.smartcontrol.SmartControl;
import de.jdungeon.app.gui.smartcontrol.UIFeedback;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

public class SkillActivityProvider implements ActivityProvider {

	public static final String ATTACK = "Angreifen";
	public static final String SCOUT = "Spähen";
	public static final String WALK = "Laufen";
	public static final String FLEE = "Fliehen";
	public static final String LOOK = "Ansehen";

	private final HeroInfo info;
	private final ActionController guiControl;
	private final FocusManager focusManager;
	private final SmartControl smartControl;
	private boolean fightState = false;
	private final List<Activity> activityCache = new ArrayList<>();

	private final Activity attack = new DefaultActivity(ATTACK);
	private final Activity flee = new DefaultActivity(FLEE);
	//private final Activity scout = new DefaultActivity(SCOUT);

	private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

	private final SkillImageManager skillImageManager;

	public SkillActivityProvider(HeroInfo info, Game game, ActionController actionAssembler, FocusManager focusManager, SmartControl smartControl) {
		super();
		this.info = info;
		this.guiControl = actionAssembler;
		this.focusManager = focusManager;
		this.smartControl = smartControl;
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

	private void updateActivityList(final Boolean currentFightState) {
		activityCache.clear();
		if (currentFightState != null && currentFightState) {
			activityCache.add(attack);
			activityCache.add(flee);
		}
		else {
			activityCache.add(new AbstractExecutableActivity() {
				@Override
				public void execute() {
					guiControl.scoutingActivity(getTarget());
				}

				@Override
				public boolean isCurrentlyPossible() {
					return !currentFightState;
				}

				@Override
				public Object getObject() {
					return SCOUT;
				}

				@Override
				public RoomInfoEntity getTarget() {
					return focusManager.getWorldFocusObject();
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
			if (highlightedEntity instanceof FigureInfo) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				guiControl.plugActions(guiControl.getActionAssembler().wannaAttack((FigureInfo) highlightedEntity));
			} else if (hostileFigures.size() == 1) {
				FigureInfo target = hostileFigures.get(0);
				guiControl.plugActions(guiControl.getActionAssembler().wannaAttack(target));
				focusManager.setWorldFocusObject(target);
			} else {
				smartControl.setMessage(UIFeedback.SelectEnemy);
			}

		}
		else if (o.equals(SCOUT)) {
			guiControl.scoutingActivity(highlightedEntity);

		}
		else if (o.equals(WALK)) {
			if (highlightedEntity instanceof PositionInRoomInfo) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				guiControl.plugActions(guiControl.getActionAssembler().wannaStepToPosition(
								((PositionInRoomInfo) highlightedEntity)));
			}
			if (highlightedEntity instanceof RoomInfo) {
				int directionToWalk = Dir.getDirFromToIfNeighbour(
						info.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				guiControl.plugActions(guiControl.getActionAssembler().wannaWalk(directionToWalk));
			}
		}
		else if (o.equals(FLEE)) {
			PositionInRoomInfo pos = info.getPos();
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
			if (possibleFleeDirection != null) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				guiControl.plugActions(guiControl.getActionAssembler().wannaFlee());
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
						guiControl.plugActions(guiControl.getActionAssembler().wannaSpell(spell, highlightedEntity));
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
						guiControl.plugActions(guiControl.getActionAssembler().wannaSpell(spell, targetEntity));
					}
					else {
						// we leave the message handling up to the core action handling triggering
						// an action with target null
						guiControl.plugActions(guiControl.getActionAssembler().wannaSpell(spell, null));

					}
				}

			} else {
				// no target required
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				guiControl.plugActions(guiControl.getActionAssembler()
						.wannaSpell(spell, null));
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

	@Override
	public boolean isCurrentlyPossible(Activity infoEntity) {
		// TODO : implement
		return true;
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
