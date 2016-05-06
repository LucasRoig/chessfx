package data;
public class Queen extends Piece{
	public Queen(ChessColors color) {
		this.color = color;
	}
	@Override
	public String toString() {
		return this.color == ChessColors.White ? "Q" : "q";
	}

	@Override
	public long possibleMoves(int square, Bitboard bitboard) {
		// TODO A compléter
		return 0;
	}

	@Override
	public void setAt(long square, Bitboard bitboard) {
		//On place les 1 ou il faut
		long un = (long)1 << square;
		bitboard.queens |= un; 
		if(this.color == ChessColors.Black){
			bitboard.black |= un;
		}else{
			bitboard.white |= un;
		}
		
		//On enleve les 1 des autres long
		bitboard.pawns ^= un;
		bitboard.knights ^= un;
		bitboard.rooks ^= un;
		bitboard.bishops ^= un;
		bitboard.kings ^= un;
		if(this.color == ChessColors.Black){
			bitboard.white ^= un;
		}else{
			bitboard.black ^= un;
		}
	}
}
