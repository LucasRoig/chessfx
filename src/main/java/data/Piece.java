package data;

public abstract class Piece {
	ChessColors color;

	public abstract String toString();

	public abstract long possibleMoves(int square, Bitboard bitboard);

	/**
	 * Modifie le bitboard afin de placer une piece sur une case donnée.
	 * 
	 * @param square
	 */
	public abstract void setAt(long square, Bitboard bitboard);

	/**
	 * Renvoie la lettre associée à la pièce.
	 * 
	 * @return
	 */
	public abstract String getLetter();
	
	/**
	 * Retourne un vecteur representant toutes les cases attaquées par les pieces présentes sur le vecteur passé en paramètre.
	 * @param fromVector
	 * @return
	 */
	public abstract long attacks(long fromVector);
}
