package de.jdungeon.log;

public class Log {

	public static boolean logging = true;

	enum MessageType {
		INFO, WARNING, ERROR, SEVERE
	};

	public static void info(String message) {
		log(message, MessageType.INFO);
	}

	public static void warning(String message) {
		log(message, MessageType.WARNING);
	}

	public static void error(String message) {
		log(message, MessageType.ERROR);
	}

	public static void error(String message, Exception e) {
		log(message +" :" + e.getStackTrace(), MessageType.ERROR);
	}

	public static void severe(String message) {
		log(message, MessageType.SEVERE);
	}

	private static void log(String message, MessageType type) {
		if (logging) {

			if (type == MessageType.INFO) {
				System.out.println("INFO: " + message);
			} else if (type == MessageType.WARNING) {
				System.out.println("WARNING: " + message);
			} else if (type == MessageType.ERROR) {
				System.out.println("ERROR: " + message);
			} else if (type == MessageType.SEVERE) {
				System.out.println("SEVERE: " + message);
			} else {
				System.out.println("Unknown Message type: " + message);
			}
		}
	}

}
