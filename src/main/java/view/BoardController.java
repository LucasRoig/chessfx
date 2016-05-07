package view;

import data.ChessColors;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class BoardController {
	Square[] squareTab = new Square[64];
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
			squareTab[i] = new Square(color);
			int col = i % 8;
			int row = 7 - i / 8;
			boardPane.add(squareTab[i], col, row);
		}
	}

}
