/*
 * UrlTest.java
 *
 * Created on 26. Februar 2004, 02:59
 */

package io;
import java.net.*;
import java.io.*;

/**
 *
 * @author  tobias
 */
public class UrlTest {
    
    /** Creates a new instance of UrlTest */
    public UrlTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // exception-handling? *hehe* ;-)
        try{
            
            // building basic-url
            String url = "http://jd.jupi.info" + 
                         "/javadungeon/highscores.php" + //file
                         "?action=newEntry" +            //action
                         "&verbose=on" +                 //output?
                         "&string=";  // String to be added URL-encoded later
            
            // The string what all is about... (with linebreak)
            String string = "Blablubb-Java-Text " + new java.util.Date() + "\n";
            
            // add URL-encoded string to url
            url += java.net.URLEncoder.encode(string);
            
            URL jd = new URL(url);
            URLConnection uc = jd.openConnection();
            
            BufferedReader in = new BufferedReader(
                                  new InputStreamReader(uc.getInputStream()));
            
            // Read the output (response) to StdOut
//            String inputLine;
//            while ((inputLine = in.readLine()) != null){
//                System.out.println(inputLine);
//            }
            
            in.close();
            
        } catch (Exception e) {     // at least: it's not Throwable ;-)
            System.out.println(e.getMessage());
        }
    } 
}
