package kenner.ko.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static boolean isDebug = false;
	
	public static void info(String input){
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		String timestamp = format.format(date);
		System.out.println("INFO @ " + timestamp + ": " +input);
	}
	
	public static void error(String input){
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		String timestamp = format.format(date);
		System.out.println("ERROR @ " + timestamp + ": " +input);
	}
	
	public static void error(String input, String Class, String Func){
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		String timestamp = format.format(date);
		System.out.println("ERROR @ " + timestamp + "in: " +
					Class+":"+Func+": " +input);
	}
	
	public static void debug(String input){
		if(isDebug){
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			String timestamp = format.format(date);
			System.out.println("DEBUG @ " + timestamp + ": " +input);
		}
	}
}
