package chessfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.ChessColors;
import data.Move;
import data.Position;

public class MainApp extends Application {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		log.info("Starting Hello JavaFX and Maven demonstration application");

		String fxmlFile = "/fxml/hello.fxml";
		log.debug("Loading FXML for main view from: {}", fxmlFile);
		FXMLLoader loader = new FXMLLoader();
		Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

		log.debug("Showing JFX scene");
		Scene scene = new Scene(rootNode, 400, 200);
		scene.getStylesheets().add("/styles/styles.css");

		stage.setTitle("Hello JavaFX and Maven");
		stage.setScene(scene);
		// stage.show();
		ConsoleInterface();
	}

	public static void ConsoleInterface() {
		Position p = Position.getStartingPosition();
		System.out.println(p.toString());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean quit = false;
		while (!quit) {
			try {
				String s = br.readLine();
				// Lecture d'un coup
				if (s.length() == 5 && s.charAt(2) == '-') {
					Move m = Move.parseFromString(s);
					if (p.isMoveLegal(m)) {
						// Le coup est légal
						Position newPosition = p.getPositionAfterMove(m);

						if (p.isLastPositon()) {
							p.setNextPosition(newPosition);
							p = p.getNextPosition();
						} else if (p.getNextPosition().equals(newPosition)) {
							p = p.getNextPosition();
						} else if (p.getSublines().isEmpty()) {
							p.addSubLine(newPosition);
							p = newPosition;
						} else {
							boolean trouve = false;
							for (Position pos : p.getSublines()) {
								if (pos.equals(newPosition)) {
									p = pos;
									trouve = true;
									break;
								}
							}
							if (!trouve) {
								p.addSubLine(newPosition);
								p = newPosition;
							}
						}
						System.out.println(p.toString());
					} else {
						System.out.println("Le coup entré n'est pas légal");
					}

					// Lecture d'une commande
				} else {
					switch (s) {
					case "quit":
						quit = true;
						break;
					case "pMove":
						if (p.getPreviousPosition() != null) {
							p = p.getPreviousPosition();
							System.out.println(p.toString());
						} else {
							System.out.println("Pas de position précédente");
						}
						break;
					case "nMove":
						if (p.getNextPosition() != null) {
							p = p.getNextPosition();
							System.out.println(p.toString());
						} else {
							System.out.println("Pas de position suivante");
						}
						break;
					case "showMoves":
						Position f = p;
						String str = "";
						Stack<Position> stack = new Stack<>();
						while (!f.isFirstPosition()) {
							f = f.getPreviousPosition();
						}
						for (Position sub : f.getSublines()) {
							stack.push(sub);
						}
						stack.push(f.getNextPosition());
						while (!stack.isEmpty()) {
							Position pos = stack.pop();
							str += pos.getSideToMove() == ChessColors.Black ? (pos.getMoveCount() + ". ")
									: (pos.getMoveCount() - 1) + "... ";
							str += pos.getLastMove() + "  ";
							for (Position sub : pos.getSublines()) {
								stack.push(sub);
							}
							while (!pos.isLastPositon()) {
								pos = pos.getNextPosition();
								str += pos.getSideToMove() == ChessColors.Black ? (pos.getMoveCount() + ". ") : "";
								str += pos.getLastMove() + "  ";
								for (Position sub : pos.getSublines()) {
									stack.push(sub);
								}
							}
							System.out.println(str);
							str = "";
						}

						break;
					case "showSublines":
						String sub = "";
						if (p.getNextPosition() != null) {
							sub += p.getNextPosition().getLastMove() + " / ";
						}
						for (Position subline : p.getSublines()) {
							sub += subline.getLastMove() + " / ";
						}
						System.out.println(sub);
						break;
					default:
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
