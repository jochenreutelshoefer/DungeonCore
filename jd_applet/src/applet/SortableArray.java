package applet;
//import java.util.*;

/**
 *	Diese Klasse stellt ein Array von Comparable-Objects dar, dass
 * wiederum Comparable ist. Es bekommt im Konstruktor mitgeliefert
 * nach welchem Objekt des Arrays denn verglichen werden soll und 
 * ob aufwaerts oder abwaerts sortiert werden soll. 
 * 
 * @author J.R.
 * 
 */
public class SortableArray implements Comparable {

	/**
	 * 
	 * @uml.property name="data" 
	 */
	private Object[] data;

	/**
	 * 
	 * @uml.property name="sortIndex" 
	 */
	private int sortIndex;

	private int vorzeichen;

	/**
	 * Method SortableArray.
	 * Konstruktor erzeugt SortableArray-Objekt
	 * 
	 * @param data	Das Daten-Array, enthaelt Comparables, String bzw. Integer
	 * @param index	Die Stelle nach der verglichen werden soll bei compareTo
	 * @param vorzeichen	Ob auswaerts oder abwaerts verglichen werden soll
	 */
	public SortableArray(Object data[], int index, int vorzeichen) {
		this.data = data;
		this.sortIndex = index;
		this.vorzeichen = vorzeichen;

	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 * Vergelich zwei SortableArrays miteinander und ordnet nach dem
	 * im Konstruktur uebergebenen Index und Vorzeichen
	 */
	public int compareTo(Object o) {
		SortableArray a = (SortableArray) (o);
//		if(sortIndex == 9) {
//			return (vorzeichen)
//			* ((new java.sql.Date((String)data[sortIndex]))
//				.compareTo(new java.sql.Date((String)a.data[a.sortIndex])));
//		}
//		else {
		return (vorzeichen)
			* (((Comparable) data[sortIndex])
				.compareTo(((Comparable) a.data[a.sortIndex])));
//		}
	}

	/**
	 * Method getData.
	 * @return Object[]
	 * 
	 * @uml.property name="data"
	 */
	public Object[] getData() {
		return data;
	}

	/**
	 * Method setSortIndex.
	 * @param k
	 * 
	 * @uml.property name="sortIndex"
	 */
	public void setSortIndex(int k) {
		sortIndex = k;
	}

}