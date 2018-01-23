package item.interfaces;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 23.01.18.
 */
public interface LocatableItem extends Locatable {

	ItemOwner getOwner();


	void setOwner(ItemOwner o);


	void getsRemoved();

}
