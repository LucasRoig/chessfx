package chessfx.core.board;

import chessfx.core.Piece;
import chessfx.core.Square;

public interface IReadableBoard {
	/**
	 * Retourne la piece occupant la case s
	 * @param s la case dont on veut connaitre l'occupant
	 */
	public Piece getPieceAt(Square s);
	
	/**
	 * Vide l'échiquier
	 */
	public void setEmpty();

	/**
	 * Place l'échiquier dans la position de départ d'une partie
	 */
	public void setStarting();
	
	/**
	 * Retourne true si les deux échiquiers sont égaux
	 * @return true si les deux échiquiers sont égaux
	 */
	public boolean equals(IReadableBoard b);
	
	/**
	 * Renvoie la case sur laquelle la prise en passant est possible
	 * Renvoie NoSquare si aucune prise en passant n'est possible
	 * @return
	 */
	public Square getEpSquare();
}
