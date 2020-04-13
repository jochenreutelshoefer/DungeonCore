package spell;

import java.util.List;

import figure.FigureInfo;
import game.InfoEntity;
import game.RoomInfoEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface TargetScope<TARGET> {

	/**
	 * Class of objects, that are potential targets
	 *
	 * @return
	 */
	Class<TARGET> getTargetClass();

	/**
	 * Determines actual target objects - in the best case just one.
	 *
	 * @param actor figure, that looks for targets
	 * @return the targets within the figures visibility/reachability
	 */
	List<TARGET> getTargetEntitiesInScope(FigureInfo actor, RoomInfoEntity highlightedEntity);
}
