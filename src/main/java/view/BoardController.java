package view;

import data.ChessColors;
import data.Piece;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class BoardController {
	Square[] squareTab = new Square[64];
	GameModel gameModel = new GameModel();
	int from = -1;
	EventHandler<Event> handleClickOnSquare = new EventHandler<Event>() {
		public void handle(Event click) {
			Square s = (Square) click.getSource();
			handleClick(s.getSquareIndex());
		}
	};
	@FXML
	GridPane boardPane;

	@FXML
	public void initialize() {
		Square.setSize(Bindings.min(boardPane.heightProperty(), boardPane.widthProperty()).divide(8));
		for (int i = 0; i < squareTab.length; i++) {
			ChessColors color;
			if ((i / 8) % 2 == 0) {
				color = (i % 8) % 2 == 0 ? ChessColors.Black : ChessColors.White;
			} else {
				color = (i % 8) % 2 == 0 ? ChessColors.White : ChessColors.Black;
			}
			Square s = new Square(color, i);
			squareTab[i] = s;
			s.setOnMouseClicked(handleClickOnSquare);
			int col = i % 8;
			int row = 7 - i / 8;
			boardPane.add(squareTab[i], col, row);
		}
		this.gameModel.getCurrentPosition().addListener((observable, oldValue, newValue) -> {
			this.updatePosition();
		});
		updatePosition();
	}

	private void updatePosition() {
		for (int i = 0; i < squareTab.length; i++) {
			Piece p = gameModel.getPieceAt(i);
			squareTab[i].setPiece(p);
		}
	}

	private void handleClick(int square) {
		if (this.from == -1) {
			this.from = square;
		} else if (this.from == square) {
			this.from = -1;
		} else {
			this.gameModel.userMakesMove(this.from, square);
			this.from = -1;
		}
	}

}
