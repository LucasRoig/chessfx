package data;

public abstract class Piece {
	ChessColors color;
	
	public abstract String toString();
	public abstract long possibleMoves(int square, Bitboard bitboard);
	
	/**
	 * Modifie le bitboard afin de placer une piece sur une case donnée.
	 * @param square
	 */
	public abstract void setAt(long square, Bitboard bitboard);
}
