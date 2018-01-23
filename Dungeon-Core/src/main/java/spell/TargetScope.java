package spell;

import java.util.List;

import figure.FigureInfo;
import game.RoomInfoEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface TargetScope {

	List<? extends RoomInfoEntity> getTargetEntitiesInScope(FigureInfo actor);

}
