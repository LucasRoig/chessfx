package chessClassicData;

public class Util {

	static long notAFile = Long.parseUnsignedLong("fefefefefefefefe", 16);
	static long notBFile = Long.parseUnsignedLong("fdfdfdfdfdfdfdfd", 16);
	static long notGFile = Long.parseUnsignedLong("bfbfbfbfbfbfbfbf", 16);
	static long notHFile = Long.parseUnsignedLong("7f7f7f7f7f7f7f7f", 16);
	static long not1row = Long.parseUnsignedLong("ffffffffffffff00", 16);
	static long not2row = Long.parseUnsignedLong("ffffffffffff00ff", 16);
	static long not7row = Long.parseUnsignedLong("ff00ffffffffffff", 16);
	static long not8row = Long.parseUnsignedLong("00ffffffffffffff", 16);
	static long Afile = ~notAFile;
	static long Hfile = ~notHFile;
	static long row1 = ~not1row;
	static long row8 = ~not8row;

	// Utilisé pour less significant bit
	static int[] index64Less = { 0, 1, 48, 2, 57, 49, 28, 3, 61, 58, 50, 42, 38, 29, 17, 4, 62, 55, 59, 36, 53, 51, 43,
			22, 45, 39, 33, 30, 24, 18, 12, 5, 63, 47, 56, 27, 60, 41, 37, 16, 54, 35, 52, 21, 44, 32, 23, 11, 46, 26,
			40, 15, 34, 20, 31, 10, 25, 14, 19, 9, 13, 8, 7, 6 };

	/**
	 * bitScanForward
	 * 
	 * @author Martin Läuter (1997) Charles E. Leiserson Harald Prokop Keith H.
	 *         Randall
	 *         "Using de Bruijn Sequences to Index a 1 in a Computer Word"
	 * @param bb
	 *            bitboard to scan
	 * @precondition bb != 0
	 * @return index (0..63) of least significant one bit
	 */
	static public long lessSignificantBit(long bb) {
		long debruijn64 = Long.parseUnsignedLong("03f79d71b4cb0a89", 16);
		if (bb == 0) {
			throw new RuntimeException("Pas de bit à 1");
		}
		return index64Less[(int) (((bb & -bb) * debruijn64) >>> 58)];
	}

	static int[] index64Most = { 0, 47, 1, 56, 48, 27, 2, 60, 57, 49, 41, 37, 28, 16, 3, 61, 54, 58, 35, 52, 50, 42, 21,
			44, 38, 32, 29, 23, 17, 11, 4, 62, 46, 55, 26, 59, 40, 36, 15, 53, 34, 51, 20, 43, 31, 22, 10, 45, 25, 39,
			14, 33, 19, 30, 9, 24, 13, 18, 8, 12, 7, 6, 5, 63 };

	/**
	 * bitScanReverse
	 * 
	 * @authors Kim Walisch, Mark Dickinson
	 * @param bb
	 *            bitboard to scan
	 * @precondition bb != 0
	 * @return index (0..63) of most significant one bit
	 */
	static long mostSignificantBit(long bb) {
		if (bb == 0) {
			throw new RuntimeException("Pas de bit à 1");
		}
		long debruijn64 = Long.parseUnsignedLong("03f79d71b4cb0a89", 16);
		bb |= bb >>> 1;
		bb |= bb >>> 2;
		bb |= bb >>> 4;
		bb |= bb >>> 8;
		bb |= bb >>> 16;
		bb |= bb >>> 32;
		return index64Most[(int) ((bb * debruijn64) >>> 58)];
	}

	/**
	 * Retourne le vecteur composé des cases sur la diagonale NordWest de la
	 * case passée en paramètre
	 */
	static public long northWest(long square) {
		long result = (long) 1 << square;
		while (((result & Afile) | (result & row8)) == 0) {
			result |= result << 7;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Retourne le vecteur composé des cases sur la diagonale NordEst de la case
	 * passée en paramètre
	 */
	static public long northEast(long square) {
		long result = (long) 1 << square;
		while (((result & Hfile) | (result & row8)) == 0) {
			result |= result << 9;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Retourne le vecteur composé des cases sur la diagonale SudOuest de la
	 * case passée en paramètre
	 */
	static public long southWest(long square) {
		long result = (long) 1 << square;
		while (((result & Afile) | (result & row1)) == 0) {
			result |= result >>> 9;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Retourne le vecteur composé des cases sur la diagonale SudEst de la case
	 * passée en paramètre
	 */
	static public long southEast(long square) {
		long result = (long) 1 << square;
		while (((result & Hfile) | (result & row1)) == 0) {
			result |= result >>> 7;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Retourne le vecteur composé des cases au Nord de la case passée en
	 * paramètre
	 */
	static public long north(long square) {
		long result = (long) 1 << square;
		while ((result & row8) == 0) {
			result |= result << 8;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Retourne le vecteur composé des cases au Sud de la case passée en
	 * paramètre
	 */
	static public long south(long square) {
		long result = (long) 1 << square;
		while ((result & row1) == 0) {
			result |= result >>> 8;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Retourne le vecteur composé des cases à l'Est de la case passée en
	 * paramètre
	 */
	static public long east(long square) {
		long result = (long) 1 << square;
		while ((result & Hfile) == 0) {
			result |= result << 1;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Retourne le vecteur composé des cases à l'Ouest de la case passée en
	 * paramètre
	 */
	static public long west(long square) {
		long result = (long) 1 << square;
		while ((result & Afile) == 0) {
			result |= result >>> 1;
		}
		// On avait ajouté la case de départ dans le résultat
		result &= ~((long) 1 << square);
		return result;
	}

	/**
	 * Decale les bits de 1 vers le nord, ne prend pas en compte les bits de la
	 * huitième rangée
	 */
	static public long northOne(long vector) {
		vector &= Util.not8row;
		return vector << 8;
	}

	/**
	 * Decale les bits de 1 vers le sud, ne prend pas en compte les bits de la
	 * premiere rangée
	 */
	static public long southOne(long vector) {
		vector &= Util.not1row;
		return vector >>> 8;
	}

	/**
	 * Decale les bits de 1 vers l'est, ne prend pas en compte les bits de la
	 * colonne H
	 */
	static public long eastOne(long vector) {
		vector &= Util.notHFile;
		return vector << 1;
	}

	/**
	 * Decale les bits de 1 vers l'ouest, ne prend pas en compte les bits de la
	 * colonne A
	 */
	static public long westOne(long vector) {
		vector &= Util.notAFile;
		return vector >>> 1;
	}

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
