package chessfx.core.board;

import java.util.List;

import chessfx.core.Square;

public interface IPosition extends IReadableBoard{
	
	/**
	 * Retourne la liste des cases que la pièce située sur la case s peut
	 * atteindre par un coup légal.
	 * @param s case de départ
	 * @return
	 */
	public List<Square> getValidTargets(Square s);
	
	/**
	 * retourne true si le trait est aux blancs
	 * @return true si le trait est aux blancs
	 */
	public boolean isWhiteToMove();
	
	/**
	 * Retourne true si la position est légale (pas de roi laissé en échec)
	 * @return
	 */
	public boolean isLegal();
}
