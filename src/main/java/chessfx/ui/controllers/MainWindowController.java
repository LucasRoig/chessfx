package chessfx.ui.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import chessfx.core.PieceType;
import chessfx.core.game.IGame;
import chessfx.pgn.PgnReader;
import chessfx.ui.model.GameSelectionModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainWindowController implements PromotionHandler {
	@FXML
	BorderPane borderPane; // Parent
	@FXML
	VBox menuBar;
	GameSelectionModel gameModel;
	GridPane board;
	BoardController boardController;
	VBox notationPane;
	NotationPaneController notationPaneController;

	ScrollPane openingTree;
	OpeningTreeController openingTreeController;
	Stage openingTreeStage;

	ApplicationContext ap;

	@FXML
	public void initialize() throws Exception {
		ap = new ClassPathXmlApplicationContext("beans.xml");

		String fxmlFile = "/fxml/board.fxml";
		FXMLLoader loader = new FXMLLoader();
		board = (GridPane) loader.load(getClass().getResourceAsStream(fxmlFile));
		boardController = loader.getController();

		loader = new FXMLLoader();
		openingTree = (ScrollPane) loader.load(getClass().getResourceAsStream("/fxml/openingTree.fxml"));
		openingTreeController = loader.getController();
		openingTreeStage = new Stage();
		openingTreeStage.setScene(new Scene(openingTree, 800, 600));

		// Pane central, on met le board dedans nécessaire pour un
		// redimensionnement propre
		AnchorPane centralPane = new AnchorPane();
		centralPane.getStyleClass().add("background");
		centralPane.getChildren().add(board);
		NumberBinding size = Bindings.min(borderPane.heightProperty().subtract(menuBar.heightProperty()),
				borderPane.widthProperty());
		board.prefHeightProperty().bind(size);
		board.prefWidthProperty().bind(size);

		fxmlFile = "/fxml/notationPane.fxml";
		loader = new FXMLLoader();
		notationPane = (VBox) loader.load(getClass().getResourceAsStream(fxmlFile));
		notationPaneController = loader.getController();
		notationPane.setPrefWidth(300);

		borderPane.setCenter(centralPane);
		borderPane.setRight(notationPane);

		this.setGame((IGame) ap.getBean("game"));
	}

	public void handleOpenPgn() {
		FileChooser fChooser = new FileChooser();
		fChooser.setSelectedExtensionFilter(new ExtensionFilter("Pgn Files", "*.pgn"));
		File choosenFile = fChooser.showOpenDialog(borderPane.getScene().getWindow());
		if (choosenFile != null) {
			FileReader in;
			try {
				in = new FileReader(choosenFile);
				BufferedReader br = new BufferedReader(in);
				PgnReader reader = new PgnReader((IGame) ap.getBean("game"));
				reader.setReader(br);
				this.setGame(reader.parseGame());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void openOpeningTree() throws IOException {
		openingTreeController.setGameMoves(this.gameModel.getMoves());
		openingTreeController.update();
		openingTreeStage.showAndWait();
	}

	public void setGame(IGame g) {
		this.gameModel = new GameSelectionModel(g);
		this.gameModel.setPromotionHandler(this);
		boardController.setGameModel(this.gameModel);
		notationPaneController.setGameModel(this.gameModel);
		notationPaneController.updateMoveList();
	}

	public PieceType askPromotion() {
		Dialog<PieceType> dialog = new Dialog<>();
		dialog.setTitle("Promotion");

		ButtonType buttonKnight = new ButtonType("Knight");
		ButtonType buttonBishop = new ButtonType("Bishop");
		ButtonType buttonRook = new ButtonType("Rook");
		ButtonType buttonQueen = new ButtonType("Queen");
		dialog.getDialogPane().getButtonTypes().addAll(buttonKnight, buttonBishop, buttonQueen, buttonRook);
		dialog.setResultConverter(new Callback<ButtonType, PieceType>() {

			@Override
			public PieceType call(ButtonType param) {
				if (param == buttonQueen) {
					return PieceType.Queen;
				} else if (param == buttonRook) {
					return PieceType.Rook;
				} else if (param == buttonBishop) {
					return PieceType.Bishop;
				} else {
					return PieceType.Knight;
				}
			}

		});
		Optional<PieceType> result = dialog.showAndWait();
		return result.get();
	}

}
