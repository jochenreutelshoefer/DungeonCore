package de.jdungeon.skill;

import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.VisibilityModifier;

public class EagleOwl implements VisibilityModifier {
    @Override
    public int getVisibilityStatus() {
        return RoomObservationStatus.VISIBILITY_FIGURES;
    }

    @Override
    public boolean stillValid() {
        return true;
    }
}
