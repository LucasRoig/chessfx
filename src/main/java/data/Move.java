package data;

/**
 * Implementation d'un coup contient la case de depart, la case d'arrivee.
 * 
 * @author Lucas
 *
 */
public class Move {
	private long from;
	private long to;

	/**
	 * Renvoie le coup correspondant a un string de la forme e2-e4 case depart -
	 * case arrivee
	 * 
	 * @param str
	 * @return
	 */
	static public Move parseFromString(String str) {
		if (str.length() != 5) {
			throw new RuntimeException("Erreur de formatage du coup" + str);
		}
		long from = Util.getSquareFromString(str.substring(0, 2));
		long to = Util.getSquareFromString(str.substring(3));
		return new Move(from, to);
	}

	public Move(long from, long to) {
		this.from = from;
		this.to = to;
	}

	public long getFrom() {
		return from;
	}

	public long getTo() {
		return to;
	}

	/**
	 * Retourne la case d'arrivée
	 */
	public String toString() {
		String s = String.valueOf(((char) ((this.to % 8) + 97)));
		s += (to / 8) + 1;
		return s;
	}
}
