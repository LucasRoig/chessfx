package chessfx.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import chessfx.core.game.GameFactory;
import chessfx.core.game.IGame;
import chessfx.pgn.PgnReader;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class PgnImportHandler {
	private Window w;
	public PgnImportHandler(Window w) {
		this.w = w;
	}
	
	/**
	 * Ouvre un FileChooser et renvoie la partie sélectionner, renvoie null si une erreur apparait.
	 * @return
	 */
	public IGame handle(){
		FileChooser fChooser = new FileChooser();
		fChooser.setSelectedExtensionFilter(new ExtensionFilter("Pgn Files", "*.pgn"));
		File choosenFile = fChooser.showOpenDialog(w);
		IGame newGame = null;
		if (choosenFile != null) {
			FileReader in;
			try {
				in = new FileReader(choosenFile);
				BufferedReader br = new BufferedReader(in);
				PgnReader reader = new PgnReader(new GameFactory().getGame());
				reader.setReader(br);
				newGame = reader.parseGame();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return newGame;
	}
}
