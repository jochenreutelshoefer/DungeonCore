package applet;

import java.io.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public abstract class AbstractScoreTable extends JApplet implements
		ActionListener, MouseMotionListener {

	public static void main(String[] args) {

		int bufferSize = 8192;
		String path = "";
		File f = new File(path + "highscores.dat");
		String result = "problem reading file " + f.toString();
		if (f.exists()) {
			try {
				InputStream in = new FileInputStream(f);
				byte[] buffer = new byte[0];
				byte[] cache = new byte[bufferSize];
				while (true) {
					int len = in.read(cache);
					if (len <= 0) {
						break;
					}
					byte[] newBuffer = new byte[buffer.length + len];
					System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
					System.arraycopy(cache, 0, newBuffer, buffer.length, len);
					buffer = newBuffer;
				}
				in.close();
				result = new String(buffer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				result = "file " + f.toString() + " not found!";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("");
		List<String> createLines = AbstractScoreTable.createLines(result);
		for (String line : createLines) {
			Object[] makeObjectArray = makeObjectArray(line);
		}
		System.out.println("created " + createLines.size() + " lines");
		System.out.println("created " + createLines.size() + " lines");
	}

	int[] vorzeichen;

	boolean ready = false;

	protected static final String seperator1 = "&&&";

	int filterMode = 0;

	// filterMode = 0; --> alles
	// filterMode = 1; --> Krieger
	// filterMode = 2; --> Dieb
	// filterMode = 3; --> Druide
	// filterMode = 4; --> Magier

	int maxAnzahl = 50;

	// Zeilen in der Tabelle angezeigt

	int sortierung = 6;

	String dateiname;

	int monat = 0;

	JTable table;

	MyTableModel tableModel;

	JPanel tabellenPanel = new JPanel();

	// String[] lueckenNummern = { "0", "1", "2", "3", "4" };
	Container cp;

	protected abstract String getSqlQuery();

	protected abstract MyTableModel makeTableModel(Object[][] o);

	protected int[] breiten = { 40, 75, 75, 50, 50, 55, 48, 35, 35, 48, 70,
			100, 60, 35 };
	protected int[] prefBreiten = { 30, 40, 40, 40, 44, 48, 48, 35, 35, 48, 45,
			70, 40, 40 };
	protected int[] minBreiten = { 25, 35, 35, 40, 44, 48, 48, 35, 35, 48, 35,
			100, 40, 40 };
	protected boolean[] centered = { true, false, false, true, true, true,
			true, true, true, true, false, false, true, true };

	protected abstract int[] getBreiten();

	protected abstract int[] getPrefBreiten();

	protected abstract int[] minBreiten();

	protected abstract boolean[] getCenteredCols();

	public void init() {

		filterMode = Integer.parseInt(getParameter("filter"));
		maxAnzahl = Integer.parseInt(getParameter("anzahl"));
		sortierung = Integer.parseInt(getParameter("sortierung"));
		monat = Integer.parseInt(getParameter("monat"));
		int sizeX = Integer.parseInt(getParameter("sizeX"));
		int sizeY = Integer.parseInt(getParameter("sizeY"));
		// dateiname = getParameter("dateiname");

		cp = this.getContentPane();

		makeVorzeichen();
		String query = getSqlQuery();
		Object[][] o = getDataArray(query);
		// int realAnzahl = maxAnzahl;

		o = filterGames(o);

		tableModel = makeTableModel(o);
		// System.out.println("tableModel: Cols: "+tableModel.getColumnCount());
		// System.out.println("tableModel: Rows: "+tableModel.getRowCount());
		table = new JTable(tableModel);
		table.setEnabled(true);

		JTableHeader header = table.getTableHeader();
		table.setPreferredScrollableViewportSize(new Dimension(sizeX, sizeY));
		addMouseListenerToHeaderInTable(table);
		header.setBackground(new Color(116, 49, 9));
		header.setForeground(new Color(204, 165, 49));
		Font font = header.getFont();
		Font font2 = font.deriveFont(Font.BOLD);
		header.setFont(font2);
		header.setPreferredSize(new Dimension(40, 25));

		int defaultHeight = table.getRowHeight();
		table.setRowHeight(defaultHeight + 3);
		TableColumnModel model = table.getColumnModel();

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);

		int[] dieBreiten = this.getBreiten();
		centered = this.getCenteredCols();

		for (int i = 0; i < this.getColCount(); i++) {
			TableColumn col = model.getColumn(i);
			col.setPreferredWidth(dieBreiten[i]);
			// if (dieBreiten[i] < 49) {
			// col.setMaxWidth(dieBreiten[i]);
			// }
			// if (i == 10) {
			// // col.setMinWidth(40);
			// col.setMaxWidth(75);
			// }
			// if (i == 2 || i == 1) {
			// // col.setMinWidth(40);
			// col.setMaxWidth(80);
			// }
			if (centered[i]) {
				col.setCellRenderer(renderer);
			}

			// col.setMinWidth(minBreiten[i]);
			// col.setPreferredWidth(prefBreiten[i]);
		}
		table.addMouseMotionListener(this);
		JViewport v = new JViewport();
		v.setView(table);
		JPanel tabellenPanel = new JPanel();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewport(v);

		scrollPane.setHorizontalScrollBar(scrollPane
				.createHorizontalScrollBar());
		tabellenPanel.add(scrollPane);
		cp.add(tabellenPanel, BorderLayout.CENTER);
		// Dimension d = getToolkit().getScreenSize();
		scrollPane.setPreferredSize(new Dimension(sizeX, sizeY));
		this.setBackground(new Color(234, 220, 125));
		this.getContentPane().setBackground(new Color(234, 220, 125));
		scrollPane.setBackground(new Color(234, 220, 125));
		table.setBackground(new Color(234, 220, 125));
		// table.setForeground(Color.BLUE);
		// 8C4C28
		this.setSize(sizeX, sizeY);

	}

	private List<String> fetchDataFromServer(String sqlQuery) {
		// System.out.println("start fetchData: "+sqlQuery );
		//String url = "http://localhost:8080" + "/GetData.jsp"; // + // file
		String url = "http://denkbares.dyndns.org/DenkDungeon"+ "/GetData.jsp"; // + // file
		// "?string="; // String to be added URL-encoded later
		StringBuffer result = new StringBuffer();
		try {

			URL jd = new URL(url);
			URLConnection uc = jd.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));

			// Read the output (response) to StdOut
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				// System.out.println(inputLine);
				result.append(inputLine);
			}

			in.close();

		} catch (Exception e) { // at least: it's not Throwable ;-)
			System.out.println("Exception while reading data");
			System.out.println(e.getMessage());
		}
		return createLines(result.toString());

	}

	public static List<String> createLines(String result) {
		List<String> strings = new LinkedList<String>();
		String text = result.toString();
		System.out.println("bekommener Text: " + text);
		String htmlHeaderKram = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
		if (text.startsWith(htmlHeaderKram)) {
			text = text.substring(htmlHeaderKram.length());
		}
		System.out.println("bekommener Text dann: " + text);

		String[] tokens = text.split("\\$\\$\\$");
		for (String f : tokens) {
			if (testString(f)) {

				System.out.println("gutes Token!");
				System.out.println(f);
				strings.add(f);
			} else {
				// System.out.println("boeses Token! ---");
			}

		}
		return strings;
	}

	// private LinkedList readFile(String dateiname) {
	// // StringBuffer temp = new StringBuffer();
	// LinkedList Strings = new LinkedList();
	// String text = null;
	// try {
	// URL file = new URL(this.getCodeBase().toString() + dateiname);
	// text = ResourceLoader.getDefaultResourceLoader().getResourceString(
	// file);
	// } catch (Exception e) {
	// // System.out.println("fehler...Exception \n"+e.toString());
	// }
	// // try {
	// // BufferedReader reader =
	// // new BufferedReader(new FileReader(file));
	// // String line;
	// // while ((line = reader.readLine()) != null) {
	// // Strings.add(new String(line));
	// // //temp.append(line);
	// // }
	// // } catch (FileNotFoundException fnfe) {
	// // //System.out.println("Fehler, Datei nicht gefunden!");
	// // } catch (IOException ioe) {
	// // //System.out.println("Fehler beim Lesen einer Datei!");
	// // }
	// StringTokenizer token = new StringTokenizer(text, "$$$");
	// // System.out.println("tokens: "+token.countTokens());
	// while (token.hasMoreTokens()) {
	// String f = token.nextToken();
	// if (testString(f)) {
	//
	// // System.out.println("gutes Token!");
	// // System.out.println(f);
	// Strings.add(f);
	// } else {
	// // System.out.println("boeses Token! ---");
	// }
	// }
	// return Strings;
	// }

	private static boolean testString(String f) {
		String values[] = f.split(seperator1);
		System.out.println("counting tokens: " + values.length);
		if (values.length == 13) {
			return true;
		} else {
			return false;
		}
	}

	private Object[][] getDataArray(String src) {

		// LinkedList Strings = readFile(dateiname);
		List<String> strings = fetchDataFromServer(src);
		System.out.println("Zeilen gefunden: " + strings.size());
		return createTableFromLines(strings);
	}

	public static Object[][] createTableFromLines(List strings) {
		Object[][] o = new Object[14][strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			String aRow = (String) strings.get(i);
			System.out.println("aRow: " + aRow);
			if (aRow.startsWith("$$$")) {
				aRow = aRow.substring(3); // cut off sep
			}
			Object[] one = makeObjectArray(aRow);
			for (int j = 0; j < one.length; j++) {
				o[j][i] = one[j];
			}
		}

		return o;
	}

	protected abstract int getColCount();

	protected static Object[] makeObjectArray(String s) {
		Object ob1[] = new Object[14];
		Object ob[] = new Object[13];
		StringTokenizer token = new StringTokenizer(s, seperator1);
		// System.out.println(s);
		for (int i = 0; i < ob.length; i++) {
			String str = token.nextToken();
			// System.out.println(str);
			boolean longValue = false;
			try {
				long k = Long.parseLong(str);
				longValue = true;
			} catch (Exception e) {
				longValue = false;
			}
			if (longValue) {
				ob[i] = new Long(Long.parseLong(str));
			} else {
				ob[i] = str;
			}
			// System.out.println("take token "+i+": "+ob[i].toString());
		}

		ob1[0] = new Long(-1); // f�r Rang
		ob1[1] = ob[0]; // Spieler
		ob1[2] = ob[1]; // Held
		ob1[3] = ob[2]; // Punkte
		ob1[4] = ob[4]; // Runden
		ob1[5] = new Long(
				(int) (((double) ((Long) ob[2]).longValue()) * 100 / ((Long) ob[4])
						.longValue()));
		ob1[6] = new Long((long) (Math.sqrt(((Long) ob1[5]).longValue()
				* ((Long) ob[2]).longValue())));
		ob1[7] = ob[5]; // Kills
		ob1[8] = ob[3]; // level
		ob1[9] = ob[6]; // type
		// ob1[10] = ((String) ob[8]);// .substring(8, 20);

		ob1[10] = new JDDate(((Long) ob[8]).longValue());
		ob1[11] = ob[7];
		ob1[12] = new JDTime(
				((((Long) ob[9]).longValue() - ((Long) ob[8]).longValue())));
		if (((Long) ob[10]).longValue() == 0) {
			ob1[13] = new String("ne");
		} else {
			ob1[13] = new String("ja");
		}
		return ob1;
	}

	// /**
	// * Diese Methode wandelt eine HashMap von Zahlen und Strings in
	// * ein String-Array um. Sie wird hier verwendet um die aus der Datenbank
	// * ausgelesenen Kapitel zu String zu machen, fuer die
	// Kapitel-Auswahl-Combobox.
	// *
	// * @return String[] String-Repaesentation der Kapitel mit Nummer
	// */
	// private String[] mapToStrings() {
	// String[] s = new String[kapitelMap.size() + 1];
	// Set set = kapitelMap.keySet();
	// Iterator it = set.iterator();
	// s[0] = "00 Alle";
	// int i = 1;
	// while (it.hasNext()) {
	// Object o = it.next();
	// Integer k = ((Integer) o);
	// String s0 = ((String) (kapitelMap.get(o)));
	// if (Integer.toString(k.intValue()).length() == 1) {
	// s[i] = "0" + k.toString() + " " + s0;
	// } else {
	// s[i] = k.toString() + " " + s0;
	// }
	// i++;
	// }
	// return s;
	// }

	/**
	 * 
	 * Verarbeitet ActionEvents von dem "Suche Starten"-Knopf und den
	 * SatzKriterium RadioButtons. Wird der "Suche Starten"-Knopf gedrueckt,
	 * werden die Angegebenen Werte aus den Komponenten ausgelesen, auf
	 * Gueltigkeit ueberprueft, und bei Gueltigkeit wird dann die Suchanfrage an
	 * die Datenbank gestellt, dann werden die Daten aufbereitet und in der
	 * Tabelle angezeigt.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		Object o = ae.getSource();
		// if (o == knopf) {
		// if (ready) {
		//
		// // Object ob[][] = //Suchanfrage an die Datenbank
		// // makeQuery(kap, laeng, haeuf, groesserB, luecken);
		//
		// if (ob == null) {
		//
		// ////System.out.println("ob ist null!");
		// } else {
		// sortArray(ob, 1);
		// tableModel.setData(ob);
		// }
		// } else {
		//
		// }
		//
		// }
	}

	/**
	 * Nur zu TestZwecken, wird vom Algorithmus nicht benutzt.
	 * 
	 * @param ob
	 * @return boolean
	 */
	private boolean assureNotNull(Object[][] ob) {

		for (int i = 0; i < ob[0].length; i++) {
			for (int j = 0; j < ob.length; j++) {
				if (ob[j][i] == null) {
					// System.out.println("NULLLLLL!" + i + " " + j);
					return true;
				}
			}
		}
		return false;

	}

	protected abstract Object[][] filterGames(Object[][] o);

	/**
	 * Baut in den Phrasenstring die Repraesentation der Variablen Woerter ein.
	 * 
	 * @param s
	 *            Der String der Phrase
	 * @param pos
	 *            Die Positionen fuer die Variablen Woerter.
	 * @return String Der mit Variablen durchsetzte String
	 */
	// private String makeAllString(String s, int[] pos) {
	// String s0 = null;
	// Vector v = splitToWords(s);
	// Object[] arrayS = v.toArray();
	// LinkedList list = new LinkedList();
	// for (int i = 0; i < pos.length; i++) {
	// int k = pos[i];
	// String x = new String("[.........(" + Integer.toString(i + 1)
	// + ")]");
	// arrayS[k] = x;
	//
	// }
	// for (int i = 0; i < arrayS.length; i++) {
	// list.add(arrayS[i]);
	// }
	// s0 = connectToString(list);
	//
	// return s0;
	// }

	// public static String connectToString(LinkedList v) {
	// String s = new String();
	// for (int i = 0; i < v.size(); i++) {
	//
	// s += ((String) v.get(i));
	// if (i != v.size() - 1) {
	// s += " ";
	// }
	// }
	// return s;
	// }

	public static Vector splitToWords(String s) {
		// //System.out.println("in Worte zu splitten: "+s);
		String s0 = new String(s);
		Vector v = new Vector();
		int i = 0;
		while (true) {
			if (i >= s0.length()) {
				break;
			}
			while ((!(i >= s0.length())) && (s0.charAt(i) == ' ')) {
				i++;
			}

			String wort = new String();

			while (!(i >= s0.length()) && (s0.charAt(i) != ' ')) {
				wort += s0.charAt(i);
				i++;
			}
			v.add(wort);

		}
		// //System.out.println("In worte gesplittet: ");
		for (int j = 0; j < v.size(); j++) {
			// //System.out.println(((String)v.get(j)));
		}

		return v;
	}

	/**
	 * Diese Methode sortiert das aktuelle Datenfeld der Tabelle nach einer
	 * bestimmten Spalte
	 * 
	 * 
	 * @param ob
	 *            Datenfeld
	 * @param spalte
	 *            Index der Spalte nach der sortiert werden soll
	 */
	protected void sortArray(Object[][] ob, int spalte) {
		SortableArray[] sortArray = new SortableArray[ob[0].length];
		for (int i = 0; i < ob[0].length; i++) {
			Object[] tempArray = new Object[ob.length];

			for (int j = 0; j < ob.length; j++) {

				tempArray[j] = ob[j][i];

			}
			sortArray[i] = new SortableArray(tempArray, spalte,
					vorzeichen[spalte]);
		}

		Arrays.sort(sortArray);

		for (int i = 0; i < ob[0].length; i++) {
			Object[] data = sortArray[i].getData();
			for (int j = 0; j < ob.length; j++) {

				ob[j][i] = data[j];

			}
		}

	}

	/**
	 * Diese Methode setzt einen MouseListener auf die Kopfzeile der Tabelle,
	 * damit die Sortierung auf User-Klicks hin gemacht werden kann.
	 * 
	 * @param table
	 *            Die Tabelle - JTable des Viewers
	 */
	public void addMouseListenerToHeaderInTable(JTable table) {
		// final TableSorter sorter = this;
		final JTable tableView = table;
		tableView.setColumnSelectionAllowed(false);
		MouseAdapter listMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TableColumnModel columnModel = tableView.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = tableView.convertColumnIndexToModel(viewColumn);
				if (e.getClickCount() == 1 && column != -1) {
					// ////System.out.println("Sorting ...");
					Object[][] ob = tableModel.getData();
					sortArray(ob, column);
					tableModel.setData(ob);
					vorzeichen[column] *= (-1);
				}
			}
		};
		JTableHeader th = tableView.getTableHeader();
		th.addMouseListener(listMouseListener);
	}

	public void mousePressed(MouseEvent me) {

	}

	public void mouseDragged(MouseEvent me) {

	}

	public void mouseClicked(MouseEvent me) {

	}

	public void mouseEntered(MouseEvent me) {

	}

	public void mouseReleased(MouseEvent me) {

	}

	public void mouseExited(MouseEvent me) {

	}

	public void mouseMoved(MouseEvent me) {

	}

	/**
	 * Setzt die Vorzeichen fuer die Sortierungen der Spalten
	 */
	private void makeVorzeichen() {
		vorzeichen = new int[14];
		vorzeichen[0] = 1;
		vorzeichen[1] = (-1);
		vorzeichen[2] = (-1);
		vorzeichen[3] = (-1);
		vorzeichen[4] = (1);
		vorzeichen[5] = (1);
		vorzeichen[6] = (-1);
		vorzeichen[7] = (1);
		vorzeichen[8] = (1);
		vorzeichen[9] = (1);
		vorzeichen[10] = (-1);
		vorzeichen[11] = (-1);
		vorzeichen[12] = (-1);
		vorzeichen[13] = (-1);
	}

}