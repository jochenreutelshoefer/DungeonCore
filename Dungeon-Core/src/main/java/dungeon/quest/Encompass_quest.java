package dungeon.quest;
import figure.monster.Monster;
import game.DungeonGameLoop;
import item.Item;
import item.ItemPool;
import location.InfoShrine;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.AbstractDungeonFiller;

/**
 * Ein Quest, bei dem starke Monster einen Schatz umzingeln, bis auf eine Luecke,
 * die besetzt wird, wenn der Held den Schatz nimmt.
 *
 */
public class Encompass_quest extends Quest {
	
	public static int size = 5;
	
	public Encompass_quest(Room r, Dungeon d, int level, DungeonGameLoop game) {
		int [][] a = getArray();
		fillRooms(d,r,a,level, game);
	}

	/**
	 * @see Quest#turn()
	 */
	@Override
	public void turn() {
	}

	/**
	 * @see Quest#action()
	 */
	@Override
	public void action() {
	}

	private void fillRooms(Dungeon d, Room r, int [][]a, int level, DungeonGameLoop game) {
		JDPoint p = r.getNumber();
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				int c = a[i][j];
				Room raum = d.getRoomNr(p.getX() + i, p.getY() +j);
				r.addQuest(this);
				if(c == 0) {
					Monster m = AbstractDungeonFiller.getBigMonster(12000,game);
					AbstractDungeonFiller.equipAMonster(m);
					raum.figureEnters(m,0, -1);
				}
				if(c == 1) {
					
				}
				if(c == 2) {
					String s = "Hier befindet sich die letzte Ruhest�tte des";
					s += " grossen Merianor, dem einstigen Herrscher des Dungeon.";
					s += " Seine Rache wird die treffen, die seine Ruhe st�ren.";
					raum.setShrine((new InfoShrine(s, raum)),true);
				}
				if(c == 3) {
					String s = "Dies ist das Grab des Merianor, und all derer";
					s += " die es wagen hierher zu kommen und seine Ruhe zu st�ren.";
					//s += " Seine Rache wird die Treffen, die sein Ruhe st�ren.";
					raum.setShrine(new InfoShrine(s, raum),true);
					int k = (int)(Math.random()* 4) + 2;
					for(int o = 0; o < 5; o++) {
						Item it = ItemPool.getRandomItem((int)(Math.random() * 100), 2.1);
						if(it != null) {
							raum.takeItem(it);
						}
					}
				}
					
				
			}
		}
		
		
		
	} 	
	private int[][] getArray() {
		int a [][] = new int[5][5];
		int k = (int)(Math.random()*3);
		if(k == 0) {
			a[0][1] = 1;
			a[0][2] = 1;
			a[1][2] = 1;
			
			int l = (int)(Math.random()*3);
			a[1+l][0] = 1;
			int m = (int)(Math.random()*3);
			a[4][1+m] = 1;
			int n = (int)(Math.random()*3);
			a[1+n][4] = 1;
		}
		else if(k == 1) {
			a[0][1] = 1;
			a[0][2] = 1;
			a[1][1] = 1;
			a[2][1] = 1;
			
			
			int m = (int)(Math.random()*3);
			a[4][1+m] = 1;
			int n = (int)(Math.random()*3);
			a[1+n][4] = 1;
		}
		
		else if(k == 2) {
			a[0][1] = 1;
			a[1][1] = 1;
			a[1][2] = 1;
			
			int l = (int)(Math.random()*3);
			a[1+l][0] = 1;
			int m = (int)(Math.random()*3);
			a[4][1+m] = 1;
			int n = (int)(Math.random()*3);
			a[1+n][4] = 1;
		}
		a[2][2] = 3;
		a[0][0] = 2;
		a[0][4] = 2;
		a[4][0] = 2;
		a[4][4] = 2;
		//System.out.println("Einer von 3 Wegen: ");
		//printArray(a);
		
		if(Math.random() < 0.5) {
			
			a = mirrorArrayH(a);
			//System.out.println("gespiegelt;");
			//printArray(a);
		}
		
		int r = (int)(Math.random()*4);
		for(int i = 0; i < r; i++) {
			
			a = turnArray(a);
			//System.out.println("gedreht;");
			//printArray(a);
		}
		//System.out.println("fertiges: ");
		printArray(a);
		return a;
			
		
	}
	
	private int[][] turnArray(int [][] a){
		//System.out.println("drehen");
		int [] [] b = a;
		int [] [] c = new int[5][5];
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				int dx = i - 2;
				int dy = j - 2;
				
				int dx2 = dy;
				int dy2 = -dx;
				
				int x2 = 2+dx2;
				int y2 = 2+dy2;
				//System.out.println("Punkt:"+i+" "+j+" kommt auf: "+x2+" "+y2);
				
				c[x2][y2] = b[i][j];
				
				
			}
			
		}
		return c;
	}


	private int [][] mirrorArrayH(int [][] a) {
		//System.out.println("spiegeln");	
		int [] [] b = a;
		int [] [] c = new int[5][5];
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				int dx = i - 2;
				c[2-dx][j] = b[i][j];
				
			}
			
		}
		return c;
		
	}

	private void printArray(int [][] a) {
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) { 
				int k = a[i][j];
				//if(a[i][j] == null) {
				//	k = 1;
				//}
				//System.out.print(k+" ");
			}
			//System.out.println();
		}
	}
}
