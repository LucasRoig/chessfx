package chessfx.ui.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import chessfx.core.board.IReadableGamePosition;
import chessfx.core.game.IGameMoves;
import chessfx.ui.ExpandTreeButton;
import chessfx.ui.MoveLabel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class OpeningTreeController {
	@FXML
	private AnchorPane anchor;
	@FXML
	private GridPane grid;

	private IGameMoves gameMoves;
	private List<List<Node>> linesList = new ArrayList<>();
	private List<Integer> shiftList = new ArrayList<>();
	private List<Boolean> visibility = new ArrayList<>();

	private EventHandler<MouseEvent> expandHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			ExpandTreeButton b = (ExpandTreeButton) event.getSource();
			if (b.isExpanded()) {
				b.setExpanded(false);
				for (Integer i : b.getLineId()) {
					visibility.set(i, false);
				}
			} else {
				b.setExpanded(true);
				for (Integer i : b.getLineId()) {
					visibility.set(i, true);
				}
			}
			update();
		}
	};

	public void setGameMoves(IGameMoves gameMoves) {
		this.gameMoves = gameMoves;
		this.linesList = new ArrayList<>();
		this.shiftList = new ArrayList<>();
		this.visibility = new ArrayList<>();
		int saveId = this.gameMoves.getCurrentPosition().getId();
		this.gameMoves.goToFirstPosition();
		parseLine(this.gameMoves.getCurrentPosition().getNextPosition(), 0);
		this.gameMoves.setCurrentPosition(saveId);

		for (@SuppressWarnings("unused")
		List<Node> list : linesList) {
			this.visibility.add(false);
		}
		this.visibility.set(0, true);
	}

	public void update() {
		this.grid.getChildren().clear();
		this.grid.getColumnConstraints().clear();
		this.grid.getRowConstraints().clear();
		int y = -1;
		for (int i = 0; i < linesList.size(); i++) {
			if (this.visibility.get(i)) {
				y++;
				int shift = shiftList.get(i);
				for (int j = 0; j < linesList.get(i).size(); j++) {
					this.grid.add(linesList.get(i).get(j), j + shift, y);
				}
			}
		}
	}

	private void parseLine(IReadableGamePosition p, int Xshift) {
		Stack<IReadableGamePosition> stack = new Stack<>();
		int currentY = linesList.size();
		List<Node> l = new LinkedList<>();
		linesList.add(l);
		shiftList.add(Xshift);
		while (p != null) {
			stack.push(p);
			p = p.getNextPosition();
		}
		while (!stack.isEmpty()) {
			IReadableGamePosition currentPosition = stack.pop();
			l.add(0, parseMove(currentPosition));
			int saveListSize = linesList.size();
			for (IReadableGamePosition subline : currentPosition.getSublines()) {
				parseLine(subline, Xshift + stack.size() + 1);
			}
			if (!currentPosition.getSublines().isEmpty()) {
				ExpandTreeButton b = new ExpandTreeButton();
				b.setOnMouseClicked(expandHandler);
				l.add(1, b);
				for (int i = saveListSize; i < shiftList.size(); i++) {
					b.getLineId().add(i);
				}
				for (int i = currentY + 1; i < shiftList.size(); i++) {
					this.shiftList.set(i, this.shiftList.get(i) + 1);
				}
			}

		}
	}

	private MoveLabel parseMove(IReadableGamePosition p) {
		MoveLabel l = MoveLabel.createMoveLabel(p.getLastMove(), p.getMoveCount(), p.getId(),
				p.isFirstPositionOfTheLine());
		l.setPadding(new Insets(5));
		return l;
	}
}
