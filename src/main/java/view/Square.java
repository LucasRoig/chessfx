package view;

import data.ChessColors;
import data.Piece;
import javafx.beans.binding.NumberBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Square extends StackPane {
	private ImageView square = new ImageView();
	private ImageView piece = new ImageView();
	private ImageView border = new ImageView();
	private int squareIndex;
	static private NumberBinding size;
	static private Image whiteSquare = new Image("/images/woodWhite.png");
	static private Image blackSquare = new Image("/images/woodBlack.png");
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
	static private Image borderImg = new Image("/images/border.png");

	static public void setSize(NumberBinding size) {
		Square.size = size;
	}

	public Square(ChessColors color, int squareIndex) {
		// On doit avoir utilisé setSize avant d'instancier une case
		this.squareIndex = squareIndex;
		square.setImage(color == ChessColors.White ? Square.whiteSquare : Square.blackSquare);
		square.fitHeightProperty().bind(size);
		square.fitWidthProperty().bind(size);
		this.getChildren().add(square);

		piece.fitHeightProperty().bind(size);
		piece.fitWidthProperty().bind(size);
		this.getChildren().add(piece);

		border.fitHeightProperty().bind(size);
		border.fitWidthProperty().bind(size);
		this.getChildren().add(border);
	}

	public int getSquareIndex() {
		return squareIndex;
	}

	public void setPiece(Piece p) {
		if (p == null) {
			this.piece.setImage(null);
		} else if (p.getColor() == ChessColors.White) {
			switch (p.getPieceType()) {
			case Pawn:
				this.piece.setImage(pawnWhite);
				break;
			case Knight:
				this.piece.setImage(knightWhite);
				break;
			case Bishop:
				this.piece.setImage(bishopWhite);
				break;
			case Rook:
				this.piece.setImage(rookWhite);
				break;
			case Queen:
				this.piece.setImage(queenWhite);
				break;
			case King:
				this.piece.setImage(kingWhite);
				break;
			default:
				break;
			}
		} else {
			switch (p.getPieceType()) {
			case Pawn:
				this.piece.setImage(pawnBlack);
				break;
			case Knight:
				this.piece.setImage(knightBlack);
				break;
			case Bishop:
				this.piece.setImage(bishopBlack);
				break;
			case Rook:
				this.piece.setImage(rookBlack);
				break;
			case Queen:
				this.piece.setImage(queenBlack);
				break;
			case King:
				this.piece.setImage(kingBlack);
				break;
			default:
				break;
			}
		}
	}

	public void removePiece() {
		this.piece.setImage(null);
	}

	public void showBorder(boolean value) {
		if (value) {
			this.border.setImage(borderImg);
		} else {
			this.border.setImage(null);
		}
	}
}
