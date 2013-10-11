/*
 * Created on 25.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package applet;


public class ScoreTableLatest extends AbstractScoreTable {
	
	
	
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
	
	
	protected Object[][] filterGames(Object[][]ob) {
		int cnt = 10;
		if(ob[0].length < 10) {
			cnt =  ob[0].length;
		}
		Object[][] res = new Object[getColCount()][10];
		this.sortArray(ob, 10 );
		for(int i = 0; i < getColCount(); i++) {
			int k = 0;
			for(int j = 0; j  < cnt  ; j++) {
//				if(ob[i][j] != null) {
//					
//				}
				res[i][k] = ob[i+1][j];
				k++;
			}
		}
		return res;
	}
	
	protected String getSqlQuery() {
		return "Select * from scores;";
	}
	
	protected int getColCount() {
		return 13;
	}
	
	
	
//	protected Object[] makeObjectArray(String s) {
//		Object ob1[] = new Object[13];
//		Object ob[] = new Object[12];
//		StringTokenizer token = new StringTokenizer(s, seperator1);
//		// System.out.println(s);
//		for (int i = 0; i < ob.length; i++) {
//			String str = token.nextToken();
//			// System.out.println(str);
//			boolean longValue = false;
//			try {
//				long k = Long.parseLong(str);
//				longValue = true;
//			} catch (Exception e) {
//				longValue = false;
//			}
//			if (longValue) {
//				ob[i] = new Long(Long.parseLong(str));
//			} else {
//				ob[i] = str;
//			}
//			// System.out.println("take token "+i+": "+ob[i].toString());
//		}
//
//		ob1[0] = new Long(-1); // für Rang
//		ob1[0] = ob[0]; // Spieler
//		ob1[1] = ob[1]; // Held
//		ob1[2] = ob[2]; // Punkte
//		ob1[3] = ob[4]; // Runden
//		ob1[4] = new Long(
//				(int) (((double) ((Long) ob[2]).longValue()) * 100 / ((Long) ob[4])
//						.longValue()));
//		ob1[5] = new Long((long) (Math.sqrt(((Long) ob1[4]).longValue()
//				* ((Long) ob[2]).longValue())));
//		ob1[6] = ob[5]; // Kills
//		ob1[7] = ob[3];
//		ob1[8] = ob[6];
//		//ob1[10] = ((String) ob[8]);// .substring(8, 20);
//		
//		ob1[9] = new Date(((Long)ob[8]).longValue());
//		ob1[10] = ob[7];
//		ob1[11] = new Time(((((Long)ob[9]).longValue()-((Long)ob[8]).longValue())));
//		if(((Long)ob[10]).longValue()==0) {
//			ob1[12] = new String("nein");
//		} else {
//			ob1[12] = new String("ja");
//		}
//		return ob1;
//	}
	
	protected MyTableModel makeTableModel(Object[][]o2) {
		String[] tabellenKopfZeilen =
		{ "Spieler", "Held", "Punkte", "Runden", "Pkte/Rnd", "Rating", "Kills", "Level","Typ","Datum","Kommentar","Dauer","Tot" };

		return new MyTableModel(o2,tabellenKopfZeilen);
	}

}
