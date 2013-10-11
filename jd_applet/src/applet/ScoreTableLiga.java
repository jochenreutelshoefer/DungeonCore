/*
 * Created on 25.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package applet;

import java.util.HashMap;
import java.util.Iterator;

public class ScoreTableLiga extends AbstractScoreTable {

	protected int[] getBreiten() {
		int []br = { 40, 90, 60, 35, 50, 50, 50, 50, 50, 50, 50,50,50 };
		return br;
	}
	protected  int[] getPrefBreiten() {
		int[] prefBreiten = { 40, 90, 40, 44, 60, 60, 60, 60, 60, 60, 60 ,60,60};
		return prefBreiten;
	}
	protected  int[] minBreiten(){
		int[] minBreiten = { 35, 80, 40, 44, 48, 48, 35, 35, 48, 35, 100,40,40 };
		return minBreiten;
	}
	protected  boolean[] getCenteredCols() {
		boolean[] centered = { true, false, true, true, true, true,
				true, true, true, true,true,true,true};
		return centered;
	}
	
	protected String getSqlQuery() {
		return "Select * from scores where liga=1";
	}
	
	protected MyTableModel makeTableModel(Object[][]o2) {
		String[] tabellenKopfZeilen =
		{ "Rang","Spieler", "Liga Rating","Spiele","Punkte", "Runden", "Pkte/Rnd",  "Kills","Krieger","Dieb","Druide","Zauberer","Zeit" };

		return new MyTableModel(o2,tabellenKopfZeilen);
	}
	
	protected int getColCount() {
		return 13;
	}
	
	protected Object[][] filterGames(Object[][]ob) {
		HashMap map = new HashMap();
		
		for(int i = 0; i < ob[0].length; i++) {
			String playername = (String)ob[1][i];
			Long rating = (Long)ob[6][i];
			Long exp = (Long)ob[3][i];
			Long rounds = (Long)ob[4][i];
			Long kills = (Long)ob[7][i];
			JDTime time = (JDTime)ob[12][i];
			String type = (String)ob[9][i];
			if(map.containsKey(playername)) {
				LigaStatus value = (LigaStatus)map.get(playername);
			
				
				value.addGame(exp.longValue(), time, kills.longValue(), type, rating.longValue(), rounds.longValue());
				
				
			}else {
				LigaStatus status = new LigaStatus();
				status.addGame(exp.longValue(), time, kills.longValue(), type, rating.longValue(), rounds.longValue());
				map.put(playername, status);
			}
		}
		
		int cnt = map.size();
		Object [][]result = new Object[this.getColCount()][cnt];
		int k = 0;
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			LigaStatus status = ((LigaStatus)map.get(element));
			result[1][k] = element;
			result[2][k] = new Long(status.getTotalRating());
			result[3][k] = new Long(status.getGames());
			result[4][k] = new Long(status.getExp());
			result[5][k] = new Long(status.getRounds());
			result[6][k] = new Long((status.getExp()*100) / status.getRounds());
			result[7][k] = new Long(status.getKills());
			result[8][k] = new Long(status.getWarrior());
			result[9][k] = new Long(status.getThief());
			result[10][k] = new Long(status.getDruid());
			result[11][k] = new Long(status.getSorcerer());
			result[12][k] = status.getTime();
			k++;
			
		}
		this.sortArray(result, 2 );
		for (int i = 0; i < result[0].length; i++) {
			result[0][i] = new Long(i+1);
		}
		
		return result;
	}
}
