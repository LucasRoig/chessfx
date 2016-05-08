package ui.model;

import chessClassicData.Game;
import chessClassicData.Move;
import chessClassicData.Piece;
import chessClassicData.Position;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe servant a manipuler une partie. Fourni la propriété currentPosition.
 * 
 * @author Lucas
 *
 */
public class GameModel {
	private ObjectProperty<Position> currentPosition = new SimpleObjectProperty<>();
	private Game game;

	public GameModel() {
		this.game = Game.getStartingGame();
		this.getCurrentPosition().set(this.game.getPosition(0));
	}

	public ObjectProperty<Position> getCurrentPosition() {
		return currentPosition;
	}

	public void goToNextMove() {
		if (currentPosition.get().getNextPosition() != null) {
			currentPosition.set(currentPosition.get().getNextPosition());
		}
	}

	public void goToPreviousMove() {
		if (currentPosition.get().getPreviousPosition() != null) {
			currentPosition.set(currentPosition.get().getPreviousPosition());
		}
	}

	public void goToPosition(int index) {
		this.currentPosition.set(this.game.getPosition(index));
	}

	public Piece getPieceAt(long square) {
		return currentPosition.get().getBitboard().getPieceAt(square);
	}

	public void userMakesMove(int from, int to) {
		Move m = new Move(from, to);
		Position cPos = currentPosition.get();
		if (cPos.isMoveLegal(m)) {
			Position newPosition = cPos.getPositionAfterMove(m);
			int newIndex = this.game.addPositionAfter(newPosition, this.getCurrentPosition().get().getIndex());
			this.getCurrentPosition().set(this.game.getPosition(newIndex));
		}
	}

}
