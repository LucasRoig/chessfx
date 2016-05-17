package chessfx.core.board;

import java.util.List;

import chessfx.core.InvalidPieceException;
import chessfx.core.Move;

public interface IReadableGamePosition extends IPosition {

	/**
	 * Retourne une nouvelle position qui résulte du coup m
	 * 
	 * @param m
	 *            le coup dont on désire obtenir la position résultante
	 * @return
	 */
	public IWritableGamePosition getPositionAfter(Move m) throws InvalidPieceException;

	public int getId();

	public IReadableGamePosition getNextPosition();

	public IReadableGamePosition getPreviousPosition();

	public List<IReadableGamePosition> getSublines();

	/**
	 * Retourne le dernier coup à avoir été joué.
	 * 
	 * @return le dernier coup à avoir été joué.
	 */
	public Move getLastMove();

	/**
	 * Retourne le nombre de coups joués depuis le début de la partie
	 * 
	 * @return le nombre de coups joués depuis le début de la partie
	 */
	public int getMoveCount();

	/**
	 * Renvoie True si la position est la premier de la partie
	 */
	public boolean isFirstPositionOfTheGame();

	/**
	 * Renvoie True si la position est la premier de la ligne
	 */
	public boolean isFirstPositionOfTheLine();

	/**
	 * Renvoie True si la position est la derniere de la ligne
	 */
	public boolean isLastPositionOfTheLine();
}
