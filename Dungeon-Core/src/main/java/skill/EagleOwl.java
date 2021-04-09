package skill;

import figure.RoomObservationStatus;
import figure.VisibilityModifier;

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
