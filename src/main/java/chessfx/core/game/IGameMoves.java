package chessfx.core.game;

import chessfx.core.board.IReadableGamePosition;
import chessfx.core.board.IWritableGamePosition;

public interface IGameMoves {
	public IReadableGamePosition getCurrentPosition();

	public void goToNextPosition();

	public void goToPreviousPosition();

	public void goToFirstPosition();

	public void goToEndOfLine();

	public void goToBeginningOfLine();

	public void setCurrentPosition(int id);

	public void addPositionAndGoTo(IWritableGamePosition p);
}
