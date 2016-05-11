package chessfx.core;

public class Piece {
	private ChessColors color;
	private PieceType pieceType;
	
	public Piece(ChessColors color, PieceType pieceType) {
		this.color = color;
		this.pieceType = pieceType;
	}

	public ChessColors getColor() {
		return color;
	}

	public PieceType getPieceType() {
		return pieceType;
	}
	
	public boolean equals(Piece p){
		return (this.getColor() == p.getColor()) && (this.getPieceType() == p.getPieceType());
	}
}
