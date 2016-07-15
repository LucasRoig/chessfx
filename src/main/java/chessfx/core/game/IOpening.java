package chessfx.core.game;

import chessfx.core.ChessColors;

/**
 * Implémentation d'une ouverture.
 * 
 * @author lucas
 *
 */
public interface IOpening extends IGame {
	/**
	 * Retourne le camp auquel cette ouverture est dédiée.
	 * 
	 * @return
	 */
	public ChessColors getColor();

	public String getName();

	public void setColor(ChessColors c);

	public void setName(String n);
	
	public int getId();
	
	public void setId(int id);
}
