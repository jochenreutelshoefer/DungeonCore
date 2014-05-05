
 import gui.*;
import gui.init.StartView;

import javax.swing.*;

 
 public class JDApplet
 extends JApplet
 {

	
	 private StartView main;

  // boolean inAnApplet;
   
 	
   public void init()
   {
	   int startCode = Integer.parseInt(getParameter("startCode"));
	   int lang = Integer.parseInt(getParameter("language"));
	   String name = (getParameter("playerName"));
	   boolean english = false;
	   if(lang == 1) {
		   english = true;
	   }
	   main = new StartView(name,startCode, this,english);

   }
 
   public void start()
   {
     main.setVisible(true);
   }
 
   public void stop()
   {
     main.setVisible(false);
   }
 }
