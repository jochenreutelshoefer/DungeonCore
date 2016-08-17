/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package test;
import java.io.*;

public class Logger {

	/**
	 * 
	 * @uml.property name="tabs" 
	 */
	int tabs;

	/**
	 * 
	 * @uml.property name="log" 
	 */
	BufferedWriter log = null;

	public Logger(String file) {
		FileWriter f = null;
		try {
			f = new FileWriter(file, false);
		} catch (Exception io) {
			//System.out.println("Fehler beim initialisieren der Log-Datei");
		}
		log = new BufferedWriter(f);
		tabs = 0;		
		
	}
	
	public void addText(String text, int tabs) {
		this.tabs = tabs;
		addTabs();	
		writeLine(text);
	}
	
	public void addTextRight(String text) {
		tabs++;		
		addTabs();
		writeLine(text);
	}
	
	public void addTextLeft(String text) {
		tabs--;	
		addTabs();
		writeLine(text);
	}
	
	private void addTabs() {
 		for( int i  = 0; i < tabs; i++) {
			try {
				log.write(new String("    "));
			}catch(Exception io ) {
				//System.out.println("Fehler beim schreiben auf die Log-Datei");		
			}
 		}
	
	}
	
	private void writeLine(String s ) {
		if(log != null) {
		try {
				log.write(s);
				log.newLine();
				log.flush();
				//System.out.println("Schreibe Zeile: "+s);
			}catch(Exception io ) {
				//System.out.println("Fehler beim schreiben auf die Log-Datei");		
				//System.out.println(io.toString());
			}
		} else {
			//System.out.println("BufferedWriter nicht initialisiert!");
		}
	}

	/**
	 * Returns the log.
	 * @return BufferedWriter
	 * 
	 * @uml.property name="log"
	 */
	public BufferedWriter getLog() {
		return log;
	}

	/**
	 * Returns the tabs.
	 * @return int
	 * 
	 * @uml.property name="tabs"
	 */
	public int getTabs() {
		return tabs;
	}

	/**
	 * Sets the tabs.
	 * @param tabs The tabs to set
	 * 
	 * @uml.property name="tabs"
	 */
	public void setTabs(int tabs) {
		this.tabs = tabs;
	}

}	
