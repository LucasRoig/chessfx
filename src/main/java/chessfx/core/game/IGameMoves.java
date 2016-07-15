package chessfx.core.game;

import chessfx.core.board.IReadableGamePosition;
import chessfx.core.board.IWritableGamePosition;

public interface IGameMoves {
	public IReadableGamePosition getCurrentPosition();

	public void goToNextPosition();

	public void goToPreviousPosition();
	/**
	 * Va à la première position de la partie
	 */
	public void goToFirstPosition();

	public void goToEndOfLine();
	/**
	 * Va à la première position de la variante ou de la partie si l'on se trouve dans la ligne principale.
	 */
	public void goToBeginningOfLine();

	public void setCurrentPosition(int id);

	public void addPositionAndGoTo(IWritableGamePosition p);
}
