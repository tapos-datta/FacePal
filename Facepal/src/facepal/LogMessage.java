/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facepal;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Tapos
 */

public class LogMessage {
    
        
    protected static String defaultLogFile = WelcomeController.workingDir+"\\msglog.txt";

    public static void write(String s) throws IOException {
        
        write(defaultLogFile, s);
    }

    public static void write(String f, String s) throws IOException {
        TimeZone tz = TimeZone.getTimeZone("GMT+6"); // or PST, MID, etc ...
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss ");
        df.setTimeZone(tz);
        String currentTime = df.format(now);
        String newLine = System.getProperty("line.separator");

        FileWriter aWriter = new FileWriter(f, true);
        aWriter.write(currentTime + "  " + s );
        aWriter.write(newLine);
        aWriter.flush();
        aWriter.close();
    }
}
