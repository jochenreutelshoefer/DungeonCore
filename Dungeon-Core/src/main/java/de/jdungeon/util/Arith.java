package de.jdungeon.util;
/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Arith {
	
	public static int exp(int base, int exponent) {
		int erg = 1;
		for(int i = 0;  i < exponent; i++) {
			erg = erg * base;
		}
		return erg;
	}
	

    public static double gauss(double m, double streu){
	////System.out.println("Zufallszahl: "+k);
	double k = Math.random();
	k -= 0.5;
	double j =(((k*2)*(Math.PI/2)));
	////System.out.println("Zufallszahl * 2/pi - pi/4 :" +j);
	double l = streu * Math.tan(j);
	////System.out.println("ergebnis: "+l);
	
	double wert = l+m;
	if(wert < 0.3 * m) {
		return 0.3 * m;
	}
	if(wert > 5 * m) {
		return gauss(m,streu);
	}
	return wert;
    }

    public static void main(String[]args){
//	int i = 30;
//	double v = 50;
//	double str1 = 3;
//	int w = 0;
//	while(w < i){
//	    //System.out.println(" "+gauss(v,str1));
//	    w++;
//	}
    
    for(int i = 0; i < 10; i++) {
    	System.out.println(gauss(100,20));
    }
}

			
		
	
}
