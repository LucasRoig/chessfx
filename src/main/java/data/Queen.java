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
}
