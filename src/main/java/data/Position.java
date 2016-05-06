package data;

import java.util.ArrayList;

/*
 * Contient une position, c'est à dire le bitboard associé ainsi qu'un pointeur
 * vers la ou les prochaine positions ainsi que vers la position précédente.
 */
public class Position {
	Bitboard bitboard;
	Position nextPosition;
	Position previousPosition;
	String lastMove = ""; // San du coup précédent
	int moveCount = 1;
	ChessColors sideToMove = ChessColors.White;
	ArrayList<Position> sublines = new ArrayList<Position>();

	static public Position getStartingPosition() {
		Position p = new Position();
		p.setBitboard(Bitboard.getStartingBitboar());
		return p;
	}

	/**
	 * Crée une position vide;
	 */
	public Position() {
		this.bitboard = new Bitboard();
	}

	public String toString() {
		return this.getBitboard().toString();
	}

	// ----------Getter Setter-------------------------

	public Bitboard getBitboard() {
		return bitboard;
	}

	public void setBitboard(Bitboard bitboard) {
		this.bitboard = bitboard;
	}

	public Position getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(Position nextPosition) {
		this.nextPosition = nextPosition;
	}

	public Position getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(Position previousPosition) {
		this.previousPosition = previousPosition;
	}

	public String getLastMove() {
		return lastMove;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public ChessColors getSideToMove() {
		return sideToMove;
	}

	public ArrayList<Position> getSublines() {
		return sublines;
	}
	// --------------------------------------------------

	/**
	 * Retourne True s'il n'y a pas de piece dans la position
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.bitboard.isEmpty();
	}

	/**
	 * retourne true si cette position est la premiere de la partie
	 * 
	 * @return
	 */
	public boolean isFirstPosition() {
		return this.getPreviousPosition() == null;
	}

	/**
	 * Renvoie true si les deux positions sont identiques
	 * 
	 * @param p
	 * @return
	 */
	public boolean equals(Position p) {
		return this.getBitboard().equals(p.getBitboard()) && this.moveCount == p.moveCount
				&& this.sideToMove == p.sideToMove;
	}

	/**
	 * retourne true si cette position est la premiere de la ligne
	 */
	public boolean isLastPositon() {
		return this.getNextPosition() == null;
	}

	/**
	 * Ajoute la position p en tant que variante
	 * 
	 * @param p
	 */
	public void addSubLine(Position p) {
		if (this.getNextPosition() == null) {
			throw new RuntimeException("Ajout d'une variante alors qu'il n'y a pas de ligne principale.");
		}
		this.sublines.add(p);
	}

	/**
	 * Retourne la position après que le coup passé en paramètre ait été joué.
	 * 
	 * @param move
	 * @return
	 */
	public Position getPositionAfterMove(Move move) {
		Position position = new Position();

		position.setPreviousPosition(this);

		// Manipulation des pièces
		position.setBitboard(this.getBitboard().copy());
		Piece pieceMoving = position.getBitboard().getPieceAt(move.getFrom());
		position.getBitboard().removeAt(move.getFrom());
		position.getBitboard().setPieceAt(pieceMoving, move.getTo());

		// Génere San
		String san = pieceMoving.getLetter();
		san += move.toString();
		position.lastMove = san;

		// Compte des coups et changement du camp au trait
		position.sideToMove = this.sideToMove == ChessColors.White ? ChessColors.Black : ChessColors.White;
		position.moveCount = position.sideToMove == ChessColors.White ? (this.moveCount + 1) : this.moveCount;

		return position;
	}

}
