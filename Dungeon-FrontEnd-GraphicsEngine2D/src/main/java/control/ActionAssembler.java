/*
 * Created on 12.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import dungeon.ChestInfo;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import event.ActionEvent;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.WannaMoveEvent;
import event.WannaStepEvent;
import event.WannaTakeItemEvent;
import figure.Figure;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.EquipmentChangeAction;
import figure.action.ExpCodeChangeAction;
import figure.action.LayDownItemAction;
import figure.action.LearnSpellAction;
import figure.action.LockAction;
import figure.action.ScoutAction;
import figure.action.ScoutResult;
import figure.action.ShrineAction;
import figure.action.SkillUpAction;
import figure.action.SpellAction;
import figure.action.StepAction;
import figure.action.TakeItemAction;
import figure.action.UseChestAction;
import figure.action.UseItemAction;
import figure.hero.HeroInfo;
import game.RoomEntity;
import item.ItemInfo;
import item.quest.LuziasBall;
import shrine.ShrineInfo;
import spell.SpellInfo;
import spell.TargetScope;

public class ActionAssembler implements EventListener {

	// TODO: refactor in a way that this ActionAssembler does not refer to JDGUIEngine2D but JDGUI
	// then move to core classes
	private JDGUIEngine2D gui;
	private boolean useWithTarget = false;
	private boolean spellMeta = false;
	private Action lastAction;
	private int repeatActionCounter;

	public ActionAssembler(JDGUIEngine2D gui) {
		this.gui = gui;
		EventManager.getInstance().registerListener(this);
	}

	public void setGui(JDGUIEngine2D gui) {
		this.gui = gui;
	}

	public void wannaAttack(FigureInfo o) {
		Action a = Action.makeActionAttack(o.getFighterID());
		plugAction(a);
	}

	private FigureInfo getFigure() {
		return gui.getFigure();
	}

	public void wannaFlee() {
		Action a = Action.makeActionFlee();
		plugAction(a);
	}

	public void wannaFleePanic(int dir) {
		Action a = Action.makeActionFlee(true);
		plugAction(a);
	}

	public void wannaLernSpell(SpellInfo spell) {
		Action a = new LearnSpellAction(spell);
		plugAction(a);
	}

	public void wannaLockDoor(DoorInfo d) {
		PositionInRoomInfo positionAtDoor = d.getPositionAtDoor(this.getFigure().getRoomInfo(), false);
		if(! getFigure().getPos().equals(positionAtDoor)) {
			// we need to step towards the door first
			wannaStepToPosition(positionAtDoor);
			if (getFigure().getActionPoints() < 1) {
				plugAction(new EndRoundAction());
			}
		}
		FigureInfo f = getFigure();
		RouteInstruction.Direction dir = d.getDirection(f.getRoomNumber());
		if (dir != null) {
			Action a = new LockAction(d);
			plugAction(a);
		}
	}

	public void wannaScout(RouteInstruction.Direction dir) {
		wannaScout(dir.getValue());
	}

	public void wannaScout(int direction) {
		Action a = new ScoutAction(this.getFigure(), direction);
		int index = Figure.getDirPos(direction);
		if (gui.getFigure().getPositionInRoomIndex() != index) {
			doStepTo(index);
		}
		if (gui.getFigure().getActionPoints() < 1) {
			plugAction(new EndRoundAction());
		}
		this.plugAction(a);
	}

	public void sorcButtonClicked(SpellInfo sp) {
		if (gui.getFigure() instanceof HeroInfo) {
			if (sp.needsTarget()) {
				spellMeta = true;
				gui.setSpellMetaDown(true);
			}
			else {
				wannaSpell(sp, null);
			}
		}
	}

	public void useButtonClicked(ItemInfo it, boolean right) {
		if (gui.getFigure() instanceof HeroInfo) {

			if (it.needsTarget()) {
				useWithTarget = true;
				gui.setUseWithTarget(true);
			}
			else {
				gui.setUseWithTarget(false);
				wannaUseItem(it, null, right);
			}
		}
	}

	public void wannaSpell(SpellInfo sp, Object target) {
		Action a = new SpellAction(sp, target);
		plugAction(a);
	}

	public void wannaUseItem(ItemInfo it, RoomEntity target, boolean meta) {
		if (target == null && it.needsTarget()) {
			target = findAndStepTowardsTarget(it);
		}

		Action a = new UseItemAction(it, target, meta);
		plugAction(a);
	}

	private RoomEntity findAndStepTowardsTarget(ItemInfo item) {
		RoomEntity target = null;
		if (item.isUsableWithTarget()) {
			TargetScope targetScope = item.getTargetScope();
			List<? extends RoomEntity> targetEntitiesInScope = targetScope.getTargetEntitiesInScope(this.getFigure());
			if (targetEntitiesInScope.size() == 1) {
				// there is only one possibility
				target = targetEntitiesInScope.get(0);
			}
		}
		if (target != null) {
			Collection<PositionInRoomInfo> interactionPositions = ((RoomEntity) target).getInteractionPositions();
			if (!interactionPositions.isEmpty()) {
				PositionInRoomInfo currentPos = getFigure().getPos();
				if (!interactionPositions.contains(currentPos)) {
					// TODO: filter for position in same room !
					Collection<PositionInRoomInfo> interactionPositionsInCurrentRoom = new HashSet<>();
					for (PositionInRoomInfo interactionPosition : interactionPositions) {
						if (interactionPosition.getLocation().equals(getFigure().getRoomNumber())) {
							interactionPositionsInCurrentRoom.add(interactionPosition);
						}
					}
					PositionInRoomInfo position = null;
					if (!interactionPositionsInCurrentRoom.isEmpty()) {
						position = interactionPositionsInCurrentRoom.iterator().next();
					}
					else {
						if (!interactionPositions.isEmpty()) {
							position = interactionPositions.iterator().next();
						}
					}
					if (position != null) {
						wannaStepToPosition(position);
						plugAction(new EndRoundAction());
					}
				}
			}
		}
		return target;
	}

	public void wannaUseShrine(Object target, boolean right) {
		if (this.getFigure().getPositionInRoomIndex() != Position.Pos.NE.getValue()) {
			wannaStepToPosition(Position.Pos.NE);
		}
		Action a = new ShrineAction(target, right);
		plugAction(a);

	}

	public void wannaSwitchEquipmentItem(int type, int index) {
		Action a = new EquipmentChangeAction(type, index);
		plugAction(a);
	}

	public void wannaLayDownItem(ItemInfo it) {
		Action a = new LayDownItemAction(it);
		plugAction(a);
	}

	@Deprecated
	public void wannaLayDownEquipmentItem(int type) {

		Action a = new LayDownItemAction(true, type);
		plugAction(a);

	}

	@Deprecated
	public void wannaLayDownItem(int index) {

		Action a = new LayDownItemAction(false, index);
		plugAction(a);

	}

	public void wannaUseChest(boolean right) {

		if (this.getFigure().getPositionInRoomIndex() != Position.Pos.NW.getValue()) {
			wannaStepToPosition(Position.Pos.NW);
		}
		Action a = new UseChestAction(right);
		plugAction(a);

	}

	public void wannaTakeItem(ItemInfo item) {
		plugAction(new TakeItemAction(item));
	}

	public void wannaWalk(int dir) {

		FigureInfo f = getFigure();
		int index = Figure.getDirPos(dir);
		if (f.getPositionInRoomIndex() != index) {
			doStepTo(index);
		}
		if (f.getActionPoints() < 1) {
			plugAction(new EndRoundAction());
		}
		Action a = Action.makeActionMove(f.getFighterID(), dir);
		plugAction(a);

	}

	private void doStepTo(int i) {
		if (getFigure().getActionPoints() >= 1) {

			plugAction(new StepAction(i));
			if (getFigure().getActionPoints() == 1) {
				plugAction(new EndRoundAction());
			}

		}
		else {
			plugAction(new EndRoundAction());
			plugAction(new StepAction(i));
		}
	}

	public void monsterClicked(FigureInfo o, boolean right) {
		SpellInfo sp = gui.getSelectedSpellInfo();
		if (spellMeta) {
			spellMeta = false;
			gui.setSpellMetaDown(false);

			if (!right) {
				wannaSpell(sp, o);
			}
		}
		else if (useWithTarget) {
			useWithTarget = false;
			gui.setUseWithTarget(false);
			if (!right) {
				int index = gui.getSelectedItemIndex();
				HeroInfo hero = (HeroInfo) gui.getFigure();
				ItemInfo it = (ItemInfo) hero.getAllItems().get(index);
				wannaUseItem(it, o, right);
			}
		}
		else {

			if (right) {
				wannaSpell(sp, o);

			}
			else {

				wannaAttack((o));

			}
		}
	}

	public void itemClicked(ItemInfo o, boolean right) {
		SpellInfo sp = gui.getSelectedSpellInfo();
		if (spellMeta) {
			spellMeta = false;
			gui.setSpellMetaDown(false);
			if (!right) {
				wannaSpell(sp, o);
			}
		}
		else if (useWithTarget) {
			useWithTarget = false;
			gui.setUseWithTarget(false);

			ItemInfo it = gui.getSelectedItem();
			if (!right) {
				wannaUseItem(it, o, right);
			}
		}
		else {

			if (right) {

				wannaSpell(sp, o);
			}
			else {

				Action a = new TakeItemAction(o);
				plugAction(a);
			}
		}
	}

	public void roomClicked(Object o, boolean right) {
		FigureInfo f = getFigure();

		int dir = -1;
		if (o instanceof RoomInfo) {
			dir = Dir.getDirFromToIfNeighbour(f.getRoomNumber(), ((RoomInfo) o)
					.getNumber());
		}
		if (o instanceof JDPoint) {
			dir = Dir.getDirFromToIfNeighbour(f.getRoomNumber(), (JDPoint) o);
		}
		if (dir == -1) {
			return;
		}
		if (right) {
			if (f.getRoomInfo().fightRunning().booleanValue()) {

				wannaFleePanic(dir);
			}
			else {
				wannaScout(dir);
			}
		}
		else {
			if (dir == -1) {

			}
			else {

				if (f.getRoomInfo().fightRunning() != null && f.getRoomInfo().fightRunning().booleanValue()) {
					int pos = getFigure().getPositionInRoomIndex();
					if ((dir == 1 & pos == 1) || (dir == 2 && pos == 3)
							|| (dir == 3 && pos == 5) || (dir == 4 && pos == 7)) {
						this.wannaFlee();
					}
				}
				else {
					this.wannaWalk(dir);
				}
			}
		}
	}

	public void spotClicked(Object o) {

	}

	public void shrineClicked(boolean right) {
		ShrineInfo sh = gui.getFigure().getRoomInfo().getShrine();
		if (spellMeta) {
			SpellInfo sp = gui.getSelectedSpellInfo();
			spellMeta = false;
			gui.setSpellMetaDown(false);
			if (!right) {
				wannaSpell(sp, sh);
			}
		}
		else if (useWithTarget) {
			useWithTarget = false;
			gui.setUseWithTarget(false);
			if (!right) {
				ItemInfo it = gui.getSelectedItem();
				wannaUseItem(it, sh, right);
			}
		}
		else {
			wannaUseShrine(null, right);
		}

	}

	public void chestClicked(Object o, boolean right) {
		FigureInfo f = getFigure();
		if (o instanceof ChestInfo) {
			ChestInfo chest = ((ChestInfo) o);
			if (chest.getLocation().equals(f.getRoomNumber())) {
				wannaUseChest(right);

			}
		}
	}

	public void positionClicked(PositionInRoomInfo pos, boolean right) {
		if (useWithTarget) {
			ItemInfo sp = gui.getSelectedItem();
			useWithTarget = false;
			gui.setSpellMetaDown(false);
			if (!right) {
				wannaUseItem(sp, pos, right);
				gui.setUseWithTarget(false);
			}
		}
		else {
			// assume want to move normally
			wannaStepToPosition(pos);
		}
	}

	public void wannaStepToPosition(Position.Pos pos) {
		wannaStepToPosition(gui.getFigure().getRoomInfo().getPositionInRoom(pos.getValue()));
	}

	public void wannaStepToPosition(PositionInRoomInfo pos) {
		wannaStepToPosition(pos, false);
	}

	public void wannaStepToPosition(PositionInRoomInfo pos, boolean unanimated) {

		FigureInfo f = getFigure();
		Action a = null;

		a = new StepAction(pos.getIndex());
		if (unanimated) {
			a.setUnanimated();
		}

		plugAction(a);

	}

	public void wannaSkillUp(int key) {
		plugAction(new SkillUpAction(key));
	}

	public void doorClicked(Object o, boolean right) {
		DoorInfo d = ((DoorInfo) o);
		if (spellMeta) {
			SpellInfo sp = gui.getSelectedSpellInfo();
			spellMeta = false;
			gui.setSpellMetaDown(false);
			if (!right) {
				wannaSpell(sp, d);
			}
		}
		else if (useWithTarget) {
			useWithTarget = false;
			gui.setUseWithTarget(false);
			if (!right) {
				ItemInfo it = gui.getSelectedItem();
				wannaUseItem(it, d, right);
			}
		}
		else {

			if (right) {
				SpellInfo sp = gui.getSelectedSpellInfo();
				wannaSpell(sp, o);
			}
			else {
				if (d.hasLock().booleanValue()) {
					wannaLockDoor(d);
				}
			}

		}

	}

	public void wannaEndRound() {
		plugAction(new EndRoundAction());
	}

	public void wannaChangeExpCode(int i) {
		Action a = new ExpCodeChangeAction(i);
		plugAction(a);

	}

	private void plugAction(Action a) {
		if (getFigure().getActionPoints() < 1) {
			gui.plugAction(new EndRoundAction());
		}
		if(lastAction == a) {
			repeatActionCounter++;
		} else {
			repeatActionCounter = 0;
		}
		lastAction = a;
		gui.plugAction(a);
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(WannaMoveEvent.class);
		events.add(WannaStepEvent.class);
		events.add(WannaTakeItemEvent.class);
		events.add(ActionEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if (event instanceof WannaMoveEvent) {
			WannaMoveEvent moveEvent = ((WannaMoveEvent) event);
			if (this.getFigure().getRoomInfo().fightRunning()) {
				this.wannaFlee();
			}
			else {
				this.wannaWalk(moveEvent.getDirection().getValue());
			}
		}
		if (event instanceof WannaStepEvent) {
			WannaStepEvent moveEvent = ((WannaStepEvent) event);
			this.wannaStepToPosition(moveEvent.getTarget());
		}
		if (event instanceof WannaTakeItemEvent) {
			WannaTakeItemEvent takeEvent = ((WannaTakeItemEvent) event);
			this.wannaTakeItem(takeEvent.getItem());
		}
		if (event instanceof ActionEvent) {
			plugAction(((ActionEvent) event).getAction());
		}
	}

	public void triggerPlannedActions() {

		// we repeat scout actions until scout was successful
			if(lastAction instanceof ScoutAction) {
				int direction = ((ScoutAction) lastAction).getDirection();
				RoomInfo scoutedRoom = this.getFigure().getRoomInfo().getNeighbourRoom(direction);
				if((scoutedRoom != null) && scoutedRoom.getVisibilityStatus() < RoomObservationStatus.VISIBILITY_FIGURES) {
					if(!getFigure().getRoomInfo().fightRunning() && repeatActionCounter < 10) {
						plugAction(lastAction);
					}
				}
			}
	}
}
