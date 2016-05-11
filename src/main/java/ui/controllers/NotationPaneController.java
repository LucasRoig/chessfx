package ui.controllers;

import chessClassicData.ChessColors;
import chessfx.core.board.Position;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import ui.MoveLabel;
import ui.model.GameModel;

/**
 * Le notation pane affiche les informations sur la partie ainsi que les coups
 * de la partie.
 * 
 * @author Lucas
 *
 */
public class NotationPaneController {
	@FXML
	FlowPane flowPane; // Panneau dans lequel la liste des coups est affichée

	GameModel gameModel;
	EventHandler<Event> ClickOnLabelHandler = new EventHandler<Event>() {
		public void handle(Event event) {
			MoveLabel l = (MoveLabel) event.getSource();
			gameModel.goToPosition(l.getIndex());
		}
	};

	@FXML
	public void initialize() {
		flowPane.setHgap(10);
		flowPane.setVgap(4);
	}

	public void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
		gameModel.getCurrentPosition().addListener((observable, oldValue, newValue) -> {
			updateMoveList();
		});
	}

	public void updateMoveList() {
		this.flowPane.getChildren().clear();
		Position pos = gameModel.getCurrentPosition().get();
		while (!pos.isFirstPositionOfGame()) {
			pos = pos.getPreviousPosition();
		}
		generateMoveList(pos.getNextPosition());
	}

	private void generateMoveList(Position pos) {
		while (pos != null) {

			String s = "";
			if (pos.getSideToMove() == ChessColors.Black) {
				s += pos.getMoveCount() + ". ";
			} else if (pos.isFirstPositionOfLine()) {
				s += (pos.getMoveCount() - 1) + "... ";
			}
			s += pos.getLastMove();
			MoveLabel l = new MoveLabel(s, pos.getIndex());
			l.setOnMouseClicked(ClickOnLabelHandler);
			if (this.gameModel.getCurrentPosition().get().getIndex() == l.getIndex()) {
				l.setFocus();
			}
			this.flowPane.getChildren().add(l);
			if (!pos.isFirstPositionOfLine()) {
				for (Position subline : pos.getPreviousPosition().getSublines()) {
					this.flowPane.getChildren().add(new Label("("));
					generateMoveList(subline);
					this.flowPane.getChildren().add(new Label(")"));
				}
			}
			pos = pos.getNextPosition();
		}
	}

}
