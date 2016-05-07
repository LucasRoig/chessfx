package view;

import data.ChessColors;
import javafx.beans.binding.NumberBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Square extends StackPane {
	public ImageView square = new ImageView();
	private ImageView piece = new ImageView();
	static private NumberBinding size;
	static private Image whiteSquare = new Image("/images/whiteSquare.png");
	static private Image blackSquare = new Image("/images/blackSquare.png");
	static private Image pawnWhite = new Image("/images/pawnWhite.png");
	static private Image knightWhite = new Image("/images/knightWhite.png");
	static private Image bishopWhite = new Image("/images/bishopWhite.png");
	static private Image rookWhite = new Image("/images/rookWhite.png");
	static private Image queenWhite = new Image("/images/queenWhite.png");
	static private Image kingWhite = new Image("/images/kingWhite.png");
	static private Image pawnBlack = new Image("/images/pawnBlack.png");
	static private Image knightBlack = new Image("/images/knightBlack.png");
	static private Image bishopBlack = new Image("/images/bishopBlack.png");
	static private Image rookBlack = new Image("/images/rookBlack.png");
	static private Image queenBlack = new Image("/images/queenBlack.png");
	static private Image kingBlack = new Image("/images/kingBlack.png");

	static public void setSize(NumberBinding size) {
		Square.size = size;
	}

	public Square(ChessColors color) {
		if (size == null) {
			throw new RuntimeException("Utiliser la methode setSize() avant d'instancier une case");
		}
		square.setImage(color == ChessColors.White ? Square.whiteSquare : Square.blackSquare);
		square.fitHeightProperty().bind(size);
		square.fitWidthProperty().bind(size);
		this.getChildren().add(square);
	}
}
