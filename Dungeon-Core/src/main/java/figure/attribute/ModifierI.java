package figure.attribute;
import java.util.*;

/**
 * Interface fuer Sachen die einen Attributwert veraendern. Bei plug() werden die 
 * Modifikationen gesetzt und bei remove() wieder zurueckgenommen.
 *
 */
public interface ModifierI{

    public LinkedList getModifications();

    public LinkedList getRemoveModifications();


}
