//import java.util.*;
package applet;
import javax.swing.table.*;


public class MyTableModel extends AbstractTableModel {

	
	private Object[][] data;

	private String[] tabellenKopfZeilen; //=
		//{ "Rang","Spieler", "Held", "Punkte", "Runden", "Pkte/Rnd", "Rating", "Kills", "Level","Typ","Uhrzeit","Kommentar","Dauer","Tot" };

	/**
	 * Method MyTableModel.
	 * @param data
	 */
	public MyTableModel(Object[][] data, String[]head) {
		tabellenKopfZeilen = head;
		//System.out.println("Lege TableModel an: "+data.length);
		this.data = data;
	}

	/**
	 * Method setData.
	 * @param newDat
	 * 
	 */
	public void setData(Object[][] newDat) {
		data = newDat;
		fireTableDataChanged();
	}

	/**
	 * Method getData.
	 * @return Object[][]
	 * 
	 */
	public Object[][] getData() {
		return data;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
		return tabellenKopfZeilen[col].toString();
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		return data[col][row];
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return data.length;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return data[0].length;
	}

}