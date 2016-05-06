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

	@Override
	public long attacks(long fromVector, Bitboard board) {
		long attack = (fromVector & Util.notAFile & Util.notBFile & Util.not1row) >>> 10;
		attack |= (fromVector & Util.notAFile & Util.not1row & Util.not2row) >>> 17;
		attack |= (fromVector & Util.notHFile & Util.not1row & Util.not2row) >>> 15;
		attack |= (fromVector & Util.notGFile & Util.notHFile & Util.not1row) >>> 6;
		attack |= (fromVector & Util.notGFile & Util.notHFile & Util.not8row) << 10;
		attack |= (fromVector & Util.notHFile & Util.not8row & Util.not7row) << 17;
		attack |= (fromVector & Util.notAFile & Util.not8row & Util.not7row) << 15;
		attack |= (fromVector & Util.notAFile & Util.notBFile & Util.not8row) << 6;
		return attack;
	}
}
