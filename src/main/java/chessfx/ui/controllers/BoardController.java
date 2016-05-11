package chessfx.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import chessfx.core.ChessColors;
import chessfx.core.Piece;
import chessfx.core.Square;
import chessfx.ui.SquareComponent;
import chessfx.ui.model.GameSelectionModel;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;

public class BoardController {
	List<Square> legalTargets = new ArrayList<>();
	SquareComponent[] squareTab = new SquareComponent[64];
	GameSelectionModel gameModel;
	boolean moveOnScroll = true;// True si l'on peut se déplacer dans la partie
								// en scrollant
	Square from = Square.NoSquare;
	EventHandler<Event> handleClickOnSquare = new EventHandler<Event>() {
		public void handle(Event click) {
			SquareComponent s = (SquareComponent) click.getSource();
			handleClick(Square.values()[s.getSquareIndex()]);
		}
	};
	@FXML
	GridPane boardPane;

	@FXML
	public void initialize() {
		SquareComponent.setSize(Bindings.min(boardPane.heightProperty(), boardPane.widthProperty()).divide(8));
		for (int i = 0; i < squareTab.length; i++) {
			ChessColors color;
			if ((i / 8) % 2 == 0) {
				color = (i % 8) % 2 == 0 ? ChessColors.Black : ChessColors.White;
			} else {
				color = (i % 8) % 2 == 0 ? ChessColors.White : ChessColors.Black;
			}
			SquareComponent s = new SquareComponent(color, i);
			squareTab[i] = s;
			s.setOnMouseClicked(handleClickOnSquare);
			int col = i % 8;
			int row = 7 - i / 8;
			boardPane.add(squareTab[i], col, row);
		}
	}

	public void setGameModel(GameSelectionModel gameModel) {
		this.gameModel = gameModel;
		this.gameModel.getCurrentPosition().addListener((observable, oldValue, newValue) -> {
			this.updatePosition();
		});
		this.boardPane.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent event) {
				if (moveOnScroll) {
					if (event.getDeltaY() > 0) {
						gameModel.goToPreviousMove();
					} else {
						gameModel.goToNextMove();
					}
				}
			}
		});
		updatePosition();
	}

	private void updatePosition() {
		if (this.from != Square.NoSquare){
			this.squareTab[this.from.ordinal()].showBorder(false);
			this.from = Square.NoSquare;
			this.legalTargets.clear();
		}
		for (int i = 0; i < squareTab.length; i++) {
			Piece p = gameModel.getPieceAt(Square.values()[i]);
			squareTab[i].setPiece(p);
		}
	}

	private void handleClick(Square square) {
		if (this.from == Square.NoSquare) {
			this.from = square;
			this.squareTab[square.ordinal()].showBorder(true);
			this.legalTargets = this.gameModel.getLegalTargets(this.from);
		} else if (this.from == square) {
			this.from = Square.NoSquare;
			this.squareTab[square.ordinal()].showBorder(false);
			this.legalTargets.clear();
		} else {
			if(this.legalTargets.contains(square)){
				//Le coup est legal
				this.gameModel.userMakesLegalMove(this.from, square);
			}else{
				this.squareTab[this.from.ordinal()].showBorder(false);
				this.from = Square.NoSquare;
				this.legalTargets.clear();
			}
		}
	}

}
