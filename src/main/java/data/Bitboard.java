package data;

/**
 * Contient la position de chaque piece.
 * 
 * @author Lucas
 *
 */
public class Bitboard {
	public long white; // Présence des pièces Blanches
	public long black; // Présence des pièces Noires
	public long pawns; // Présence des pions
	public long knights; // Présence des cavaliers
	public long bishops; // Présence des fous
	public long rooks; // Présence des tours
	public long queens;// présence des dames
	public long kings; // présence des rois

	// ---------Methodes Statiques-------------

	static private Bitboard startingBitboard;

	static public Bitboard getStartingBitboar() {
		if (Bitboard.startingBitboard == null) {
			Bitboard bitboard = new Bitboard();
			bitboard.black = Long.parseUnsignedLong("18446462598732840960");

			bitboard.white = Long.parseUnsignedLong("65535");
			bitboard.pawns = Long.parseUnsignedLong("71776119061282560");
			bitboard.knights = Long.parseUnsignedLong("4755801206503243842");
			bitboard.bishops = Long.parseUnsignedLong("2594073385365405732");
			bitboard.rooks = Long.parseUnsignedLong("9295429630892703873");
			bitboard.queens = Long.parseUnsignedLong("576460752303423496");
			bitboard.kings = Long.parseUnsignedLong("1152921504606846992");
			System.out.println(Long.toBinaryString((long) 1 << 63));
			Bitboard.startingBitboard = bitboard;
		}
		return Bitboard.startingBitboard;
	}

	// ----------------------------------------
	/**
	 * Crée un bitboard vide
	 */
	public Bitboard() {
		this.white = 0;
		this.black = 0;
		this.pawns = 0;
		this.knights = 0;
		this.bishops = 0;
		this.rooks = 0;
		this.queens = 0;
		this.kings = 0;
	}

	/**
	 * Renvoie un nouveau bitboard etant une copie du bitboard actuel
	 */
	public Bitboard copy() {
		Bitboard bitboard = new Bitboard();
		bitboard.black = this.black;
		bitboard.white = this.white;
		bitboard.pawns = this.pawns;
		bitboard.bishops = this.bishops;
		bitboard.knights = this.knights;
		bitboard.rooks = this.rooks;
		bitboard.queens = this.queens;
		bitboard.kings = this.kings;
		return bitboard;
	}

	public String toString() {
		String str = "";
		int i = 56;
		while (i >= 0) {
			Piece piece = this.getPieceAt(i);
			if (piece != null) {
				str += piece.toString();
			} else {
				str += "    ";
			}
			i++;
			if (i % 8 == 0) {
				i -= 16;
				str += '\n';
			}
		}
		return str;
	}

	// ---------Methodes pour les pieces--------
	/**
	 * Retourne la piece occupant la case passee en parametre retourne null si
	 * aucune piece n'est presente
	 * 
	 * @param square
	 * @return
	 */
	public Piece getPieceAt(long square) {
		ChessColors color;
		square = (long) 1 << square;
		if ((this.black & square) != 0) {
			color = ChessColors.Black;
		} else if ((this.white & square) != 0) {
			color = ChessColors.White;
		} else {
			return null;
		}

		if ((this.pawns & square) != 0) {
			return new Pawn(color);
		} else if ((this.knights & square) != 0) {
			return new Knight(color);
		} else if ((this.bishops & square) != 0) {
			return new Bishop(color);
		} else if ((this.rooks & square) != 0) {
			return new Rook(color);
		} else if ((this.queens & square) != 0) {
			return new Queen(color);
		} else if ((this.kings & square) != 0) {
			return new King(color);
		} else {
			return null;
		}
	}

	/**
	 * Place la piece passee en parametre sur la case passee en parametre
	 * 
	 * @param piece
	 * @param square
	 */
	public void setPieceAt(Piece piece, long square) {
		piece.setAt(square, this);
	}

	/**
	 * Enleve une piece de la case passee en parametre
	 * 
	 * @param square
	 */
	public void removeAt(long square) {
		long one = Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16) - ((long) 1 << square);
		this.black &= one;
		this.white &= one;
		this.pawns &= one;
		this.bishops &= one;
		this.knights &= one;
		this.rooks &= one;
		this.queens &= one;
		this.kings &= one;
	}

	// -----------Tests sur l'échiquier----------
	/**
	 * Retourne true si tous les entiers sont à 0
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.white == 0 && this.black == 0 && this.pawns == 0 && this.knights == 0 && this.bishops == 0
				&& this.rooks == 0 && this.queens == 0 && this.kings == 0;
	}

	/**
	 * Retourne True si b est dans la meme disposition que le bitboard actuel
	 * 
	 * @param b
	 *            BitBoard
	 * @return
	 */
	public boolean equals(Bitboard b) {
		return b.white == this.white && b.black == this.black && b.pawns == this.pawns && b.knights == this.knights
				&& b.bishops == this.bishops && b.rooks == this.rooks && b.queens == this.queens
				&& b.kings == this.kings;
	}
}
