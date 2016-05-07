package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainWindowController {
	@FXML
	BorderPane borderPane; // Parent
	GameModel gameModel = new GameModel();
	GridPane board;
	BoardController boardController;
	VBox notationPane;
	NotationPaneController notationPaneController;

	@FXML
	public void initialize() throws Exception {
		String fxmlFile = "/fxml/board.fxml";
		FXMLLoader loader = new FXMLLoader();
		board = (GridPane) loader.load(getClass().getResourceAsStream(fxmlFile));
		boardController = loader.getController();

		fxmlFile = "/fxml/notationPane.fxml";
		loader = new FXMLLoader();
		notationPane = (VBox) loader.load(getClass().getResourceAsStream(fxmlFile));
		notationPaneController = loader.getController();

		notationPaneController.setGameModel(this.gameModel);
		boardController.setGameModel(this.gameModel);

		borderPane.setCenter(board);
		borderPane.setRight(notationPane);
	}
}
