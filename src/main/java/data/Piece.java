package data;

public abstract class Piece {
	ChessColors color;
	
	public abstract String toString();
	public abstract long possibleMoves(int square, Bitboard bitboard);
}
