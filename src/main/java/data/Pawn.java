package data;

public class Pawn extends Piece {

	public Pawn(ChessColors color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return this.color == ChessColors.White ? "\u2659" : "\u265F";
	}

	@Override
	public long possibleMoves(long square, Bitboard bitboard) {
		long moves = 0;
		long squareVector = (long) 1 << square;
		long attackedSquares = this.attacks(squareVector, bitboard);
		if (this.color == ChessColors.White) {
			moves = attackedSquares & bitboard.black;
			moves |= Util.northOne(squareVector);
			// est sur la deuxieme rangée
			if ((squareVector & (~Util.not2row)) != 0) {
				moves |= Util.northOne(Util.northOne(squareVector));// 2 vers le
																	// haut
			}
			moves ^= bitboard.white;
		} else {
			moves = attackedSquares & bitboard.white;
			moves |= Util.southOne(squareVector);
			// est sur la 7eme rangée
			if ((squareVector & (~Util.not7row)) != 0) {
				moves |= Util.southOne(Util.southOne(squareVector));// 2 vers le
																	// bas
			}
			moves ^= bitboard.black;
		}
		return moves;
	}

	@Override
	public void setAt(long square, Bitboard bitboard) {
		// On place les 1 ou il faut
		long un = (long) 1 << square;
		bitboard.pawns |= un;
		if (this.color == ChessColors.Black) {
			bitboard.black |= un;
		} else {
			bitboard.white |= un;
		}

		// On enleve les 1 des autres long
		un = Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16) - ((long) 1 << square);
		bitboard.bishops &= un;
		bitboard.knights &= un;
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
		return "";
	}

	@Override
	public long attacks(long fromVector, Bitboard b) {
		if (this.color == ChessColors.White) {
			return whiteAttacks(fromVector);
		} else {
			return blackAttacks(fromVector);
		}

	}

	private long whiteAttacks(long fromVector) {
		long west = (fromVector << 9) & Util.notAFile;
		long east = (fromVector << 7) & Util.notHFile;
		return west | east;
	}

	private long blackAttacks(long fromVector) {
		long west = (fromVector >>> 9) & Util.notHFile;
		long east = (fromVector >>> 7) & Util.notAFile;
		return west | east;
	}
}
