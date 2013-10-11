package test;
public class ScoutTest{

    public static void main(String[]args){
	
	int versuche = 20;

	int psychoW = 8;
	int scoutW = 3;
	int []valuesW = new int[versuche];
	int []verteilungW = new int[6];
	int psychoH = 10;
	int scoutH = 4;
	int []valuesH = new int[versuche];
	int []verteilungH = new int[6];
	int psychoM = 13;
	int scoutM = 6;
	int []valuesM = new int[versuche];
	int []verteilungM = new int[6];

	for(int i = 0; i < versuche;i++){
	    int W = scout(psychoW, scoutW);
	    verteilungW[W]++;

	    int H = scout(psychoH, scoutH);
	    verteilungH[H]++;
	    
	    int M = scout(psychoM, scoutM);
	    verteilungM[M]++;
	}
	//System.out.println();
	//System.out.println("0  1  2  3  4  5");
	//System.out.println(verteilungW[0]+"  "+verteilungW[1]+"  "+verteilungW[2]+"  "+verteilungW[3]+"  "+verteilungW[4]+"  "+verteilungW[5]+"  "+"Warrior"+ Integer.toString(psychoW)+"  "+Integer.toString(scoutW));
													       //System.out.println(verteilungH[0]+"  "+verteilungH[1]+"  "+verteilungH[2]+"  "+verteilungH[3]+"  "+verteilungH[4]+"  "+verteilungH[5]+"  "+"Hunter"+ Integer.toString(psychoH)+"  "+Integer.toString(scoutH));
																										      //System.out.println(verteilungM[0]+"  "+verteilungM[1]+"  "+verteilungM[2]+"  "+verteilungM[3]+"  "+verteilungM[4]+"  "+verteilungM[5]+"  "+"Mage"+ Integer.toString(psychoM)+"  "+Integer.toString(scoutM));
    }

  static int scout(int psycho, int Scout){
	int level = 0;
	int handycap = 80;
	for(int i = 0; i < Scout; i++){
	    int a = scoutHelp(handycap, psycho);
	    if(a > level) level = a;
	}
	
 	return level;
    }

    static int scoutHelp(int handycap, int Psycho){
	int value = ((int)(Math.random()*handycap));
	////System.out.print(value+"  ");
	if(value < Psycho - 5){return 5;}
	else if(value < Psycho){return 4;}
	else if(value < Psycho * 2){return 3;}
	else if(value < Psycho * 3){return 2;}
	else if(value < Psycho * 4){return 1;}
	else return 0;
    }
}
