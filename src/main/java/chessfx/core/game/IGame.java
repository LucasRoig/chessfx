package chessfx.core.game;

import javax.swing.event.ChangeListener;

public interface IGame {
	public IGameMoves getMoves();
	
	/**
	 * Ajoute les coups de g à la partie actuelle
	 * @param g
	 * @throws Exception 
	 */
	public void merge(IGame g) throws Exception;
}
