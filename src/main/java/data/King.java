package data;

public class King extends Piece {
	public King(ChessColors color) {
		this.color = color;
	}
	@Override
	public String toString() {
		return this.color == ChessColors.White ? "K" : "k";
	}

	@Override
	public long possibleMoves(int square, Bitboard bitboard) {
		// TODO A compléter
		return 0;
	}
}
