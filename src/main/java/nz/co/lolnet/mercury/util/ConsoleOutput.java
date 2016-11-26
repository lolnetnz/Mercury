package nz.co.lolnet.mercury.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConsoleOutput {
	
	public static void info(String string) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " INFO]: " + string);
	}
	
	public static void warn(String string) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " WARN]: " + string);
	}
	
	public static void error(String string) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " ERROR]: " + string);
	}
}