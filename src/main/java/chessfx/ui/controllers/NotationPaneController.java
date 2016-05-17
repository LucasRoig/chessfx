package chessfx.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import chessfx.core.board.IReadableGamePosition;
import chessfx.core.game.IGameMoves;
import chessfx.ui.MoveLabel;
import chessfx.ui.model.GameSelectionModel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

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

	GameSelectionModel gameModel;
	IGameMoves gameMoves;
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

	public void setGameModel(GameSelectionModel gameModel) {
		this.gameModel = gameModel;
		this.gameMoves = this.gameModel.getMoves();
		gameModel.getCurrentPosition().addListener((observable, oldValue, newValue) -> {
			updateMoveList();
		});
	}

	public void updateMoveList() {
		this.flowPane.getChildren().clear();
		this.gameMoves = this.gameModel.getMoves();
		int saveId = this.gameMoves.getCurrentPosition().getId();
		this.gameMoves.goToFirstPosition();
		if (this.gameMoves.getCurrentPosition().getNextPosition() != null)
			generateMoveList(this.gameMoves.getCurrentPosition().getNextPosition().getId(),
					this.gameMoves.getCurrentPosition().getSublines());
		this.gameMoves.setCurrentPosition(saveId);
	}

	private void generateMoveList(int id, List<IReadableGamePosition> subline) {
		this.gameMoves.setCurrentPosition(id);
		IReadableGamePosition p = this.gameMoves.getCurrentPosition();
		MoveLabel l = MoveLabel.createMoveLabel(p.getLastMove(), p.getMoveCount(), p.getId(),
				p.isFirstPositionOfTheLine());
		l.setOnMouseClicked(ClickOnLabelHandler);
		if (this.gameModel.getCurrentPosition().get().getId() == l.getIndex())
			l.setFocus();
		this.flowPane.getChildren().add(l);
		for (IReadableGamePosition sub : subline) {
			this.flowPane.getChildren().add(new Label("("));
			generateMoveList(sub.getId(), new ArrayList<IReadableGamePosition>());
			this.flowPane.getChildren().add(new Label(")"));
		}
		this.gameMoves.setCurrentPosition(id);
		if (!this.gameMoves.getCurrentPosition().isLastPositionOfTheLine())
			generateMoveList(this.gameMoves.getCurrentPosition().getNextPosition().getId(),
					this.gameMoves.getCurrentPosition().getSublines());
	}

}
