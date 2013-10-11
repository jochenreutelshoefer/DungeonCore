/*
 * Created on 25.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package applet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScoreTableTop extends AbstractScoreTable {
	
	protected  int[] getBreiten() {
		return breiten;
	}
	protected  int[] getPrefBreiten() {
		return prefBreiten;
	}
	protected  int[] minBreiten() {
		return minBreiten;
	}
	protected  boolean[] getCenteredCols() {
		return centered;
	}

	protected Object[][] filterGames(Object[][]ob) {
		Map map = new HashMap();
		for(int i = 0; i < ob[0].length; i++) {
			String playername = (String)ob[1][i];
			if(map.containsKey(playername)) {
				Integer value = (Integer)map.get(playername);
				Long rating = (Long)ob[6][value.intValue()];
				Long thisRating = (Long)ob[6][i];
				if(thisRating.longValue() > rating.longValue()) {
					map.put(playername, new Integer(i));
				}
			}else {
				map.put(playername, new Integer(i));
			}
		}
		
		int cnt = map.size();
		Object [][]result = new Object[ob.length][cnt];
		int k = 0;
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			int index = ((Integer)map.get(element)).intValue();
			for(int j = 0; j <  ob.length; j++) {
				result[j][k] = ob[j][index];
			}
			k++;
			
		}
		this.sortArray(result, 6 );
		Object [][]result2 = new Object[ob.length][10];
		int count = 10;
		if(map.size() < 10) {
			count = map.size();
		}
		for(int i = 0; i < ob.length; i++) {
			for(int j = 0; j < count; j++) {
				result2[i][j] = result[i][j];
			}
		}
		for (int i = 0; i < result2[0].length; i++) {
			result2[0][i] = new Long(i+1);
		}
		
		
		return result2;
	}
	
	protected String getSqlQuery() {
		return "Select * from scores where registered=1;";
	}
	
	protected int getColCount() {
		return 14;
	}
	
	protected MyTableModel makeTableModel(Object[][]o2) {
		String[] tabellenKopfZeilen =
		{ "Rang","Spieler", "Held", "Punkte", "Runden", "Pkte/Rnd", "Rating", "Kills", "Level","Typ","Datum","Kommentar","Dauer","Tot" };

		return new MyTableModel(o2,tabellenKopfZeilen);
	}
}
