package io;

import java.awt.image.LookupTable;

/*
 * Created on 27.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class MyLookUpTable extends LookupTable {

	int[][] filterColors;

	int t;

	public MyLookUpTable(int[][] col, int threshold) {
		super(0, 4);
		t = threshold;
		filterColors = col;

	}

	private boolean matches(int[] src) {
		//System.out.println(src [0]+" "+ src [1]+" "+ src [2]);
		boolean b = false;
		for (int i = 0; i < filterColors.length; i++) {
			boolean matchesThisColor = true;
			if (src[0] - filterColors[i][0] > t) {
				matchesThisColor = false;
			}
			if (src[0] - filterColors[i][0] < -t) {
				matchesThisColor = false;
			}
			if (src[1] - filterColors[i][1] > t) {
				matchesThisColor = false;
			}
			if (src[1] - filterColors[i][1] < -t) {
				matchesThisColor = false;
			}
			if (src[2] - filterColors[i][2] > t) {
				matchesThisColor = false;
			}
			if (src[2] - filterColors[i][2] < -t) {
				matchesThisColor = false;
			}
			if(matchesThisColor) {
				//System.out.println("filtered!");
				return true;
			}
		}
		return false;

	}

	// private boolean matches (int [] src) {
	// if( src[0] - filterColor[0] > t) {
	// return false;
	// }
	// if( src[0] - filterColor[0] < -t) {
	// return false;
	// }
	// if( src[1] - filterColor[1] > t) {
	// return false;
	// }
	// if( src[1] - filterColor[1] < -t) {
	// return false;
	// }
	// if( src[2] - filterColor[2] > t) {
	// return false;
	// }
	// if( src[2] - filterColor[2] < -t) {
	// return false;
	// }
	// return true;
	// }
	//	
	public int[] lookupPixel(int[] src, int[] dest) {
		if (matches(src)) {
			int[] blank = { 0, 50, 0, 0 };
			dest[3] = 0;
			dest[0] = 0;
			dest[1] = 0;
			dest[2] = 0;
			return blank;
		}
		int[] res = new int[4];
		res[3] = 255;
		res[0] = (src[0] + 20) % 255;
		res[1] = (src[1] + 20) % 255;
		res[2] = (src[2] + 20) % 255;

		// dest [3] = 255;
		// dest [0] = (src[0] +20) % 255;
		// dest [1] = (src[1] +20) % 255;
		// dest [2] = (src[2] +20) % 255;
		//		

		return res;
	}

}
