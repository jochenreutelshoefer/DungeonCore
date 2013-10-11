/*
 * Created on 25.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package applet;


public class ScoreTableAll extends AbstractScoreTable {
	
	
	protected int[] getBreiten() {
		int []br = { 80, 80, 50, 50, 55, 48, 35, 35, 48, 70, 100,60,40 };
		return br;
	}
	protected  int[] getPrefBreiten() {
		int[] prefBreiten = { 40, 40, 40, 44, 48, 48, 35, 35, 48, 45, 70 ,40,40};
		return prefBreiten;
	}
	protected  int[] minBreiten(){
		int[] minBreiten = { 35, 35, 40, 44, 48, 48, 35, 35, 48, 35, 100,40,40 };
		return minBreiten;
	}
	protected  boolean[] getCenteredCols() {
		boolean[] centered = { false, false, true, true, true, true,
				true, true, true, false, false,true,true};
		return centered;
	}
	
	protected Object[][] filterGames(Object[][] ob) {
		Object[][] ob2 = new Object[ob.length-1][ob[0].length];
		for(int i = 0; i <  ob.length-1; i++) {
			for(int j = 0; j < ob[0].length; j++) {
				ob2[i][j] = ob[i+1][j];
			}
		}
		sortArray(ob2, 5);	
		return ob2;
	}
	
	protected String getSqlQuery() {
		return "Select * from scores;";
	}
	
	protected int getColCount() {
		return 13;
	}
	protected MyTableModel makeTableModel(Object[][]o2) {
		String[] tabellenKopfZeilen =
		{"Spieler", "Held", "Punkte", "Runden", "Pkte/Rnd", "Rating", "Kills", "Level","Typ","Datum","Kommentar","Dauer","Tot" };

		return new MyTableModel(o2,tabellenKopfZeilen);
	}

}
