package flibs.util;
/**
 * Conjunto de utilidades para texto
 */
public class StringUtilities {
	
	public static String removeLastCharacter(String str) {
		return str.substring(0, str.length() - 1);
	}
	public static String removeStrings(String str, String[] toRemove) {
		String flag = str;
		for (String rm : toRemove) {
			flag = flag.replace(rm, "");
		}
		return flag;
	}
}
