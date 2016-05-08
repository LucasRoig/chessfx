package ui.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ui.model.GameModel;

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

		// Pane central, on met le board dedans nécessaire pour un
		// redimensionnement propre
		AnchorPane centralPane = new AnchorPane();
		centralPane.getStyleClass().add("background");
		centralPane.getChildren().add(board);
		NumberBinding size = Bindings.min(borderPane.heightProperty(), borderPane.widthProperty());
		board.prefHeightProperty().bind(size);
		board.prefWidthProperty().bind(size);

		fxmlFile = "/fxml/notationPane.fxml";
		loader = new FXMLLoader();
		notationPane = (VBox) loader.load(getClass().getResourceAsStream(fxmlFile));
		notationPaneController = loader.getController();
		notationPane.setPrefWidth(300);

		notationPaneController.setGameModel(this.gameModel);
		boardController.setGameModel(this.gameModel);

		borderPane.setCenter(centralPane);
		borderPane.setRight(notationPane);
	}
}
