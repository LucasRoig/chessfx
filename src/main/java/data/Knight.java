package data;

public class Knight extends Piece {
	public Knight(ChessColors color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return this.color == ChessColors.White ? "\u2658" : "\u265E";
	}

	@Override
	public long possibleMoves(int square, Bitboard bitboard) {
		// TODO A compléter
		return 0;
	}

	@Override
	public void setAt(long square, Bitboard bitboard) {
		// On place les 1 ou il faut
		long un = (long) 1 << square;
		bitboard.knights |= un;
		if (this.color == ChessColors.Black) {
			bitboard.black |= un;
		} else {
			bitboard.white |= un;
		}

		// On enleve les 1 des autres long
		un = Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16) - ((long) 1 << square);
		bitboard.pawns &= un;
		bitboard.bishops &= un;
		bitboard.rooks &= un;
		bitboard.queens &= un;
		bitboard.kings &= un;
		if (this.color == ChessColors.Black) {
			bitboard.white &= un;
		} else {
			bitboard.black &= un;
		}
	}

	@Override
	public String getLetter() {
		return "N";
	}
}
