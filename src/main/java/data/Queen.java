package data;

public class Queen extends Piece {
	public Queen(ChessColors color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return this.color == ChessColors.White ? "\u2655" : "\u265B";
	}

	@Override
	public void setAt(long square, Bitboard bitboard) {
		// On place les 1 ou il faut
		long un = (long) 1 << square;
		bitboard.queens |= un;
		if (this.color == ChessColors.Black) {
			bitboard.black |= un;
		} else {
			bitboard.white |= un;
		}

		// On enleve les 1 des autres long
		un = Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16) - ((long) 1 << square);
		bitboard.pawns &= un;
		bitboard.knights &= un;
		bitboard.rooks &= un;
		bitboard.bishops &= un;
		bitboard.kings &= un;
		if (this.color == ChessColors.Black) {
			bitboard.white &= un;
		} else {
			bitboard.black &= un;
		}
	}

	@Override
	public String getLetter() {
		return "Q";
	}

	@Override
	public long attacks(long fromVector, Bitboard board) {
		long resultat = 0;
		Bishop b = new Bishop(ChessColors.White);
		Rook r = new Rook(ChessColors.White);
		resultat |= b.attacks(fromVector, board);
		resultat |= r.attacks(fromVector, board);
		return resultat;
	}
}
