package game;

/**
 * Interface fuer Sachen, die jede Runde getriggert werden muessen weil
 * sie zeitabhaengig sind.
 *
 */
public interface Turnable {

	void turn(int round);
}
