package util;
/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TurnArray {

	public static void main(String[] args) {
		int k = 10;
		int[][] a = new int[7][7];
		int[][] b = new int[7][7];
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j  < a[0].length; j++) {
				a[i][j] = k;
				k++;
			}
		}
		
		//System.out.println("Array am Anfang: ");
		printArray(a);
		
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j  < a[0].length; j++) {
				
				//Drehung:
				//b[i][j] = a[j][7-i-1];		
				b[j][7-i-1] = a[i][j];
			}
		}
		
		
		
		//System.out.println("Array nach Drehung; ");
		printArray(b);
		
		
	}
	
	private static void printArray(int [][] a) {
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
