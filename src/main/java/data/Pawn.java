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
	public long possibleMoves(int square, Bitboard bitboard) {
		// TODO A compléter
		return 0;
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
	public long attacks(long fromVector,Bitboard b) {
		if(this.color == ChessColors.White){
			return whiteAttacks(fromVector);
		}else{
			return blackAttacks(fromVector);
		}
		
	}
	
	private long whiteAttacks(long fromVector){
		long west = (fromVector << 9) & Util.notAFile;
		long east = (fromVector << 7) & Util.notHFile;
		return west | east;
	}
	private long blackAttacks(long fromVector){
		long west = (fromVector >>> 9) & Util.notHFile;
		long east = (fromVector >>> 7) & Util.notAFile;
		return west | east;
	}
}
