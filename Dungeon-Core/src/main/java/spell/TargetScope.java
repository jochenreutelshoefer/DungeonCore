package spell;

import java.util.List;

import figure.FigureInfo;
import game.InfoEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public interface TargetScope {

	List<? extends InfoEntity> getTargetEntitiesInScope(FigureInfo actor);

}
