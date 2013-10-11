package applet;

import io.ResourceLoader;

import java.util.*;
import javax.swing.*;



import figure.hero.Hero;

import java.awt.event.*;
import java.awt.*;
//import java.sql.*;
//import javax.swing.table.*;
//import java.io.*;
//import javax.swing.table.TableModel;
//import javax.swing.event.TableModelEvent;
import java.net.*;



//a00d164
public class DungeonMaster extends JApplet {


	boolean ready = false;
	int filterMode = 0;
	String master;

	/**
	 * Das TableModel zur Tabele, enthaelt die Daten
	 * 
	 * @uml.property name="panel"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JPanel Panel = new JPanel();

	//String[] lueckenNummern = { "0", "1", "2", "3", "4" };
	Container cp;

	
	public void init() {
		//super("PharPhrasViewer");
		//connectToDataBase();
		//ready = readKapitelMap();
		filterMode = Integer.parseInt(getParameter("filter"));
		cp = this.getContentPane();
		cp.add(Panel);
		
		String dateiname = new String("highscores.dat");
		Object[][] o = getDataArray(dateiname);
		sortArray(o, 6);
		for (int i = 0; i < o[0].length; i++) {
			o[0][i] = new Integer(i + 1);
		}
		
		
		if(o[0].length >0) {
			
			master = (String)o[1][0];
		}
		else {
			master = "Niemand";	
		}
		
		

		this.setSize(150, 50);

	}
	
	
	public void paint(Graphics g) {
		g.setColor(Color.yellow);
		g.drawRect(1,1,148,48);
		g.drawRect(2,2,146,46);
		g.setColor(Color.black);
		g.drawRect(0,0,150,50);
		g.drawRect(3,3,144,44);
		g.setFont(new Font("Serif",Font.TRUETYPE_FONT,12));
		g.drawString("Aktueller Dungeonmaster:",10,20);
		g.setFont(new Font("Serif",Font.BOLD,16));
		g.drawString(this.master,15,40);	
		
	}
	private LinkedList readFile(String dateiname) {
		//StringBuffer temp = new StringBuffer();
		LinkedList Strings = new LinkedList();
		String text = null;
		try {
			URL file = new URL(this.getCodeBase().toString() + dateiname);
			text =
				ResourceLoader.getDefaultResourceLoader().getResourceString(
					file);
		} catch (Exception e) {
			//System.out.println("fehler...Exception \n"+e.toString());	
		}
		//								try {
		//					BufferedReader reader =
		//						new BufferedReader(new FileReader(file));
		//					String line;
		//					while ((line = reader.readLine()) != null) {
		//						Strings.add(new String(line));
		//						//temp.append(line);
		//					}
		//				} catch (FileNotFoundException fnfe) {
		//					//System.out.println("Fehler, Datei nicht gefunden!");
		//				} catch (IOException ioe) {
		//					//System.out.println("Fehler beim Lesen einer Datei!");
		//				}
		StringTokenizer token = new StringTokenizer(text, "$$$");
		//System.out.println("tokens: "+token.countTokens());
		while (token.hasMoreTokens()) {
			String f = token.nextToken();
			//System.out.println(f);
			Strings.add(f);
		}
		return Strings;
	}

	private Object[][] getDataArray(String dateiname) {

		LinkedList Strings = readFile(dateiname);
		Object[][] o = new Object[11][Strings.size()];
		for (int i = 0; i < Strings.size(); i++) {
			Object[] one = makeObjectArray((String) Strings.get(i));
			for (int j = 0; j < o.length; j++) {
				o[j][i] = one[j];
			}
		}

		return o;
	}

	private Object[] makeObjectArray(String s) {
		Object ob1[] = new Object[11];
		Object ob[] = new Object[8];
		StringTokenizer token = new StringTokenizer(s, "§");
		//System.out.println(s);
		for (int i = 0; i < ob.length; i++) {
			String str = token.nextToken();
			//System.out.println(str);
			boolean integerValue = false;
			try {
				int k = Integer.parseInt(str);
				integerValue = true;
			} catch (Exception e) {
				integerValue = false;
			}
			if (integerValue) {
				ob[i] = new Integer(Integer.parseInt(str));
			} else {
				ob[i] = str;
			}
			//System.out.println("take token "+i+": "+ob[i].toString());
		}

		ob1[0] = new Integer(-1); //für Rang
		ob1[1] = ob[0]; //Spieler
		ob1[2] = ob[1]; //Held
		ob1[3] = ob[2]; //Punkte
		ob1[4] = ob[4]; //Runden
		ob1[5] =
			new Integer(
				(int) (((double) ((Integer) ob[2]).intValue())
					* 100
					/ ((Integer) ob[4]).intValue()));
		ob1[6] =
			new Integer(
				(int) (Math
					.sqrt(
						((Integer) ob1[5]).intValue()
							* ((Integer) ob[2]).intValue())));
		ob1[7] = ob[5]; //Kills
		ob1[8] = ob[3];
		ob1[9] = ob[6];
		ob1[10] = ob[7];

		return ob1;
	}

	//	/**
	//	 * Diese Methode wandelt eine HashMap von Zahlen und Strings in
	//	 * ein String-Array um. Sie wird hier verwendet um die aus der Datenbank
	//	 * ausgelesenen Kapitel zu String zu machen, fuer die Kapitel-Auswahl-Combobox.
	//	 *
	//	 * @return String[] String-Repaesentation der Kapitel mit Nummer
	//	 */
	//	private String[] mapToStrings() {
	//		String[] s = new String[kapitelMap.size() + 1];
	//		Set set = kapitelMap.keySet();
	//		Iterator it = set.iterator();
	//		s[0] = "00 Alle";
	//		int i = 1;
	//		while (it.hasNext()) {
	//			Object o = it.next();
	//			Integer k = ((Integer) o);
	//			String s0 = ((String) (kapitelMap.get(o)));
	//			if (Integer.toString(k.intValue()).length() == 1) {
	//				s[i] = "0" + k.toString() + " " + s0;
	//			} else {
	//				s[i] = k.toString() + " " + s0;
	//			}
	//			i++;
	//		}
	//		return s;
	//	}

	/**
	 *
	 * Verarbeitet ActionEvents von dem "Suche Starten"-Knopf und den
	 * SatzKriterium RadioButtons.
	 * Wird der "Suche Starten"-Knopf gedrueckt, werden die Angegebenen Werte
	 * aus den Komponenten ausgelesen, auf Gueltigkeit ueberprueft, und bei Gueltigkeit
	 * wird dann die Suchanfrage an die Datenbank gestellt, dann werden
	 * die Daten aufbereitet und in der Tabelle angezeigt.
	 *
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		Object o = ae.getSource();
		//		if (o == knopf) {
		//			if (ready) {
		//
		//				//				Object ob[][] =		//Suchanfrage an die Datenbank
		//				//					makeQuery(kap, laeng, haeuf, groesserB, luecken);
		//
		//				if (ob == null) {
		//
		//					////System.out.println("ob ist null!");
		//				} else {
		//					sortArray(ob, 1);
		//					tableModel.setData(ob);
		//				}
		//			} else {
		//
		//			}
		//
		//		}
	}
	private Object[][] filterGames(Object[][] ob) {

		Vector valids = new Vector();
		if (filterMode != 0) {
			String compareString;
			if(filterMode == Hero.HEROCODE_WARRIOR) {
				compareString = "Krieger";	
			}
			else if(filterMode == Hero.HEROCODE_HUNTER) {
				compareString = "Dieb";	
			}
			else if(filterMode == Hero.HEROCODE_DRUID) {
				compareString = "Druide";	
			}
			else  {
				compareString = "Magier";	
			}			
			for (int i = 0; i < ob[0].length; i++) {
				Object[] temp = new Object[ob.length];
				boolean add = false;
				//System.out.println("Vergleiche: "+(String) ob[9][i]+" mit: "+compareString);
				if (((String) ob[9][i]).equals(compareString)) {
					for (int j = 0; j < ob.length; j++) {

						temp[j] = ob[j][i];
						add = true;

					}

				}
				if (add) {
					valids.add(temp);
					add = false;
				}

			}
			Object[][] ob2 = new Object[ob.length][valids.size()];

			for (int i = 0; i < valids.size(); i++) {
				Object[] temp = ((Object[]) valids.get(i));
				for (int j = 0; j < ob.length; j++) {
					ob2[j][i] = temp[j];

				}
			}
			ob = ob2;
		}
		return ob;
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
					//System.out.println("NULLLLLL!" + i + " " + j);
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Baut in den Phrasenstring die Repraesentation der Variablen Woerter ein.
	 *
	 * @param s	Der String der Phrase
	 * @param pos	Die Positionen fuer die Variablen Woerter.
	 * @return String	Der mit Variablen durchsetzte String
	 */
	private String makeAllString(String s, int[] pos) {
		String s0 = null;
		Vector v = splitToWords(s);
		Object[] arrayS = v.toArray();
		LinkedList list = new LinkedList();
		for (int i = 0; i < pos.length; i++) {
			int k = pos[i];
			String x =
				new String("[.........(" + Integer.toString(i + 1) + ")]");
			arrayS[k] = x;

		}
		for (int i = 0; i < arrayS.length; i++) {
			list.add(arrayS[i]);
		}
		s0 = connectToString(list);

		return s0;
	}
	public static String connectToString(LinkedList v) {
		String s = new String();
		for (int i = 0; i < v.size(); i++) {

			s += ((String) v.get(i));
			if (i != v.size() - 1) {
				s += " ";
			}
		}
		return s;
	}
	public static Vector splitToWords(String s) {
		////System.out.println("in Worte zu splitten: "+s);
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
		////System.out.println("In worte gesplittet: ");
		for (int j = 0; j < v.size(); j++) {
			////System.out.println(((String)v.get(j)));
		}

		return v;
	}

	/**
	 * Diese Methode sortiert das aktuelle Datenfeld der Tabelle nach einer bestimmten Spalte
	 *
	 *
	 * @param ob Datenfeld
	 * @param spalte	Index der Spalte nach der sortiert werden soll
	 */
	private void sortArray(Object[][] ob, int spalte) {
		SortableArray[] sortArray = new SortableArray[ob[0].length];
		for (int i = 0; i < ob[0].length; i++) {
			Object[] tempArray = new Object[ob.length];

			for (int j = 0; j < ob.length; j++) {

				tempArray[j] = ob[j][i];

			}
			sortArray[i] =
				new SortableArray(tempArray, spalte, -1);
		}

		Arrays.sort(sortArray);

		for (int i = 0; i < ob[0].length; i++) {
			Object[] data = sortArray[i].getData();
			for (int j = 0; j < ob.length; j++) {

				ob[j][i] = data[j];

			}
		}

	}

	
	

	

}