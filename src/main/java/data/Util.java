package data;

public class Util {

	/**
	 * Retourne l'entier correspondant à la case passée en paramètre.
	 * 
	 * @param str
	 * @return
	 */
	static public long getSquareFromString(String str) {
		char[] c = str.toLowerCase().toCharArray();
		if (c.length != 2) {
			throw new RuntimeException("Erreur lors du passage string -> long, str.size = " + c.length);
		}
		return (c[0] - 97) + 8 * (Long.parseLong(str.substring(1)) - 1);
	}
}
