package spell;

import java.util.List;

import figure.FigureInfo;
import game.InfoEntity;
import game.RoomEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface TargetScope {

	List<? extends RoomEntity> getTargetEntitiesInScope(FigureInfo actor);

}
