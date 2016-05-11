package chessfx.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MoveLabel extends Label {
	int index;

	public MoveLabel(String s, int index) {
		super(s);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Met une couleur de fond sous le label
	 */
	public void setFocus() {
		this.backgroundProperty().set(new Background(new BackgroundFill(Color.AQUA, new CornerRadii(5), Insets.EMPTY)));
	}

}
