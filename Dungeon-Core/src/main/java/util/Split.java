package util;
public class Split {


    //zerteilt value zufällig in pieces Teile
   public static int [] split_equal(int value, int pieces) {
	int a [] = new int[pieces];
	int b;

	for(int i = 0; i < value; i++) {
	     b = (int)(Math.random() * pieces);
	     a[b] = a[b] + 1;
	}
	
	return a;
	
    }

    //teilt value ganz zufällig in 2 Teile
    public static int[] split2_random(int value) {
	int [] a = new int[2];
	a[0] = (int)(Math.random() * value);
	a[1] = value - a[0];
	return a;
    }

    //teilt value zufällig in 2 Teile, jedoch Varianz höchstens ein viertel
    public static int[] split2_middle(int value) {
	int[] a = new int[2];
	a[0] = (value/2) + ((int)(Math.random() * (value/4)));
	a[1] = value - a[0];
			    if ((int)(Math.random() * 2) <= 1) {
				int x = a[0];
				a[0] = a[1];
				a[1] = x;
			    }
	 return a;
    }

    //liefert eine Zahl, die etwa normalverteilt um value herumliegt, am besten Vielfache von 8
    public static float about_value(int val){
	float value = (float)val;
		float a = (int) (Math.random() *100);
	if (a <= 1) return (float)(value * 3);
	else if(a <= 3) return (float)(value * 2);
	else if(a <= 7) return (float)(value * 1.75);
	else if(a <= 13) return (float)(value * 1.5);
	else if(a <= 23) return (float)(value * 1.25);
	else if(a <= 34) return (float)(value * 1.125);
	else if(a <= 66) return value;
	else if(a <= 77) return (float)(value / 1.125);
	else if(a <= 87) return (float)(value / 1.25);
	else if(a <= 93) return (float)(value / 1.5);
	else if(a <= 97) return (float)(value / 1.75);
	else if(a <=99) return (float)(value / 2);
	else return (float)(value /3);
	
    }
}
