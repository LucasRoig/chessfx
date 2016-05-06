package data;

public class King extends Piece {
	public King(ChessColors color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return this.color == ChessColors.White ? "\u2654" : "\u265A";
	}

	@Override
	public void setAt(long square, Bitboard bitboard) {
		// On place les 1 ou il faut
		long un = (long) 1 << square;
		bitboard.kings |= un;
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
		bitboard.queens &= un;
		bitboard.bishops &= un;
		if (this.color == ChessColors.Black) {
			bitboard.white &= un;
		} else {
			bitboard.black &= un;
		}
	}

	@Override
	public String getLetter() {
		return "K";
	}

	@Override
	public long attacks(long fromVector, Bitboard b) {
		long attack = Util.eastOne(fromVector) | Util.westOne(fromVector);
		fromVector |= attack;
		attack |= Util.northOne(fromVector) | Util.southOne(fromVector);
		return attack;
	}
}
