/*
 * Created on 27.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package applet;

public class JDTime {
	
	int sec;
	long ms;
	public JDTime(long l) {
		sec = (int)l/1000;
		ms = l;
	}
	
	public long getMs() {
		return ms;
	}
	
	public void addTime(long l) {
		ms = ms + l;
		sec = (int)ms / 1000;
	}
	
	public void addTime(JDTime t) {
		addTime(t.getMs());
	}
	
	public String toString() {
		return ""+addZero(getHours())+":"+addZero(getMinutes())+":"+addZero(getSeconds());
	}
	
	public int getHours() {
		return (int) sec/(60 * 60);
	}
	
	public int getMinutes() {
		return (int) (sec%(60 * 60)) / (60);
	}
	
	public int getSeconds() {
		return (int) (sec%(60));
	}
	
	public static String addZero(int i) {
		if(i > 99 || i < 0) {
			return "??";
		}
		if(i > 9) {
			return Integer.toString(i);
		}
		return "0"+Integer.toString(i);
	}
	
	public int compareTo(Object o) {
		if(o instanceof JDTime) {
			JDTime otherTime = (JDTime)o;
			if(otherTime.getHours()> this.getHours()) {
				return 1;
			} else if(otherTime.getHours()< this.getHours()) {
				return -1;
			}
			else {
				if(otherTime.getMinutes()> this.getMinutes()) {
					return 1;
				} else if(otherTime.getMinutes()< this.getMinutes()) {
					return -1;
				}
				else {
					if(otherTime.getSeconds()> this.getSeconds()) {
						return 1;
					} else if(otherTime.getSeconds()< this.getSeconds()) {
						return -1;
					}else {
						return 0;
					}
				}
			}
		}
		
		return 0;
	}

}
