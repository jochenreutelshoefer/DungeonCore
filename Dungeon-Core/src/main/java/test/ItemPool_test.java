package test;
import item.Item;
import item.ItemPool;
import util.Arith;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ItemPool_test {

	public static void main(String[] args) {
		int k = 0;
		for(int i = 0; i  < 50; i++) {
			k += 2;
			//System.out.println("---------------wert: "+k);
			for(int j = 0; j < 5; j++) {
				
			double scattered = Arith.gauss(k,3);
			double quotient = scattered / k;
			Item it = ItemPool.getRandomItem((int)scattered, quotient);
			if(it != null) {
				//System.out.println(it.getText()+ " Wert: "+it.getWorth());
			}
			}
		}
	}
}
