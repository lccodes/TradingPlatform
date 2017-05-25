package brown.setup;

public class Logging {
	
	public final static boolean LOGGING = true;
	
	public static void log(String message) {
		if (LOGGING) {
			System.out.println(message);
		}
	}

}
