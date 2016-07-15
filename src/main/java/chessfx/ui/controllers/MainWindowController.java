package chessfx.ui.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import chessfx.core.PieceType;
import chessfx.core.game.GameFactory;
import chessfx.core.game.IGame;
import chessfx.data.Database;
import chessfx.pgn.PgnReader;
import chessfx.ui.DatabaseView;
import chessfx.ui.model.GameSelectionModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

	Database data;
	File dataPath = new File("data.cot");
	DatabaseView dataView = new DatabaseView();

	@FXML
	public void initialize() throws Exception {
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

		if (dataPath.exists()) {
			this.loadData(dataPath);
		} else {
			this.data = new Database();
		}
		dataView.setData(this.data);
		dataView.update();
		notationPaneController.setData(data);

		borderPane.setCenter(centralPane);
		borderPane.setRight(notationPane);
		borderPane.setLeft(dataView);

		this.setGame(new GameFactory().getGame());
	}

	public void openOpeningTree() throws IOException {
		openingTreeController.setGameMoves(this.gameModel.getMoves());
		openingTreeController.update();
		openingTreeStage.showAndWait();
	}

	public void setGame(IGame g) {
		this.gameModel = new GameSelectionModel(g);
		this.dataView.setModel(this.gameModel);
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

	public void setStage(Stage s) {
		s.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (!dataPath.exists()) {
					try {
						dataPath.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				saveData(dataPath);
			}
		});
	}

	public void loadData(File f) {
		ObjectInputStream ois = null;
		try {
			FileInputStream in = new FileInputStream(f);
			ois = new ObjectInputStream(in);
			Database d = (Database) ois.readObject();
			this.data = d;
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void saveData(File f) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream out = new FileOutputStream(f);
			oos = new ObjectOutputStream(out);
			oos.writeObject(this.data);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
