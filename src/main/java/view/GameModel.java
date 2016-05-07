package view;

import data.Move;
import data.Piece;
import data.Position;
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

	public GameModel() {
		currentPosition.set(Position.getStartingPosition());
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

	public Piece getPieceAt(long square) {
		return currentPosition.get().getBitboard().getPieceAt(square);
	}

	public void userMakesMove(int from, int to) {
		Move m = new Move(from, to);
		Position cPos = currentPosition.get();
		if (cPos.isMoveLegal(m)) {
			Position newPosition = cPos.getPositionAfterMove(m);
			if (cPos.isLastPositon()) {
				cPos.setNextPosition(newPosition);
				currentPosition.set(cPos.getNextPosition());
			} else if (cPos.getNextPosition().equals(newPosition)) {
				this.goToNextMove();
			} else if (cPos.getSublines().isEmpty()) {
				cPos.addSubLine(newPosition);
				currentPosition.set(newPosition);
			} else {
				boolean trouve = false;
				for (Position pos : cPos.getSublines()) {
					if (pos.equals(newPosition)) {
						currentPosition.set(newPosition);
						trouve = true;
						break;
					}
				}
				if (!trouve) {
					cPos.addSubLine(newPosition);
					currentPosition.set(newPosition);
				}
			}
		}
	}
}
