package data;

import java.util.ArrayList;

/*
 * Contient une position, c'est à dire le bitboard associé ainsi qu'un pointeur
 * vers la ou les prochaine positions ainsi que vers la position précédente.
 */
public class Position {
	private short castleRights = 15;
	// Droits roque
	// 0001 - petit roque blanc
	// 0010 - grand roque blanc
	// 0100 - petit roque noir
	// 1000 - grand roque noir

	Bitboard bitboard;
	Position nextPosition;
	Position previousPosition;
	String lastMove = ""; // San du coup précédent
	int moveCount = 1;
	ChessColors sideToMove = ChessColors.White;
	ArrayList<Position> sublines = new ArrayList<Position>();
	boolean firstPositionOfLine = false;
	int index; // Index de la position afin de la retrouver.

	static public Position getStartingPosition() {
		Position p = new Position();
		p.setBitboard(Bitboard.getStartingBitboar());
		p.firstPositionOfLine = true;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

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

	// --------------Roque-----------------------------
	private boolean canWhiteShortCastle() {
		return (this.castleRights & 1) != 0;
	}

	private boolean canWhiteLongCastle() {
		return (this.castleRights & 2) != 0;
	}

	private boolean canBlackShortCastle() {
		return (this.castleRights & 4) != 0;
	}

	private boolean canBlackLongCastle() {
		return (this.castleRights & 8) != 0;
	}

	private void removeWhiteShortCastle() {
		this.castleRights &= ~1;
	}

	private void removeWhiteLongCastle() {
		this.castleRights &= ~2;
	}

	private void removeBlackShortCastle() {
		this.castleRights &= ~4;
	}

	private void removeBlackLongCastle() {
		this.castleRights &= ~8;
	}
	// --------------Tests sur la position-------------

	/**
	 * Retourne true si la position actuelle est légale
	 */
	public boolean isPositionLegal() {
		// Compte le nombre de rois
		if (Long.bitCount(this.bitboard.kings & this.bitboard.white) != 1
				|| Long.bitCount(this.bitboard.kings & this.bitboard.black) != 1) {
			return false;
		}

		// Compte les pions
		if (Long.bitCount(this.bitboard.pawns & this.bitboard.white) > 8
				|| Long.bitCount(this.bitboard.pawns & this.bitboard.black) > 8) {
			return false;
		}

		// Vérifie la position des pions
		if ((this.bitboard.pawns & (Util.row1 | Util.row8)) != 0) {
			return false;
		}

		// Vérifie que le camp n'étant pas au trait n'est pas en échec
		long kingSquare = this.sideToMove == ChessColors.White ? this.bitboard.kings & this.bitboard.black
				: this.bitboard.kings & this.bitboard.white;
		long attackedSquares = this.sideToMove == ChessColors.White ? this.bitboard.getSquaresAttackedByWhite()
				: this.bitboard.getSquaresAttackedByBlack();
		if ((kingSquare & attackedSquares) != 0) {
			return false;
		}

		return true;
	}

	/**
	 * Retourne true si le coup peut être joué dans la position actuelle
	 */
	public boolean isMoveLegal(Move m) {
		Piece p = this.getBitboard().getPieceAt(m.getFrom());
		// Vérifie que la piece est sur la case de départ
		if (p == null) {
			return false;
		}
		// Vérifie que la pièce est de la bonne couleur
		if (p.color != this.sideToMove) {
			return false;
		}

		// Verifie si le joueur veut roquer
		if (p.pieceType == PieceType.King && m.getFrom() == 4 && p.color == ChessColors.White) {
			// Le roi est sur sa case de départ et veut bouger
			if (m.getTo() == 2) {
				// Blanc grand roque
				if (!this.canWhiteLongCastle()) {
					return false;
				}
				// Compte les pieces entre d1 et b1
				if (!(this.bitboard.getPieceAt(1) == null && this.bitboard.getPieceAt(2) == null
						&& this.bitboard.getPieceAt(3) == null)) {
					return false;
				}
				// Aucune case menacée entre e1 et c1
				long mask = (long) 7 << 2;
				if ((mask & this.bitboard.getSquaresAttackedByBlack()) != 0) {
					return false;
				}
				m.setWhiteLongCastle();
				return true;
			} else if (m.getTo() == 6) {
				// Blanc petit roque
				if (!this.canWhiteShortCastle()) {
					return false;
				}
				// Compte les pieces entre f1 et g1
				if (!(this.bitboard.getPieceAt(5) == null && this.bitboard.getPieceAt(6) == null)) {
					return false;
				}
				// Aucune case menacée entre e1 et g1
				long mask = (long) 7 << 4;
				if ((mask & this.bitboard.getSquaresAttackedByBlack()) != 0) {
					return false;
				}
				m.setWhiteShortCastle();
				return true;
			}
		}
		if (p.pieceType == PieceType.King && m.getFrom() == 60 && p.color == ChessColors.Black) {
			if (m.getTo() == 58) {
				// Noir grand roque
				if (!this.canBlackLongCastle()) {
					return false;
				}
				// Compte les pieces entre d8 et b8
				if (!(this.bitboard.getPieceAt(57) == null && this.bitboard.getPieceAt(58) == null
						&& this.bitboard.getPieceAt(59) == null)) {
					return false;
				}
				// Aucune case menacée entre e8 et c8
				long mask = (long) 7 << 58;
				if ((mask & this.bitboard.getSquaresAttackedByWhite()) != 0) {
					return false;
				}
				m.setBlackLongCastle();
				return true;
			}
			if (m.getTo() == 62) {
				// Noir petit roque
				if (!this.canBlackShortCastle()) {
					return false;
				}
				// Compte les pieces entre f8 et g8
				if (!(this.bitboard.getPieceAt(61) == null && this.bitboard.getPieceAt(62) == null)) {
					return false;
				}
				// Aucune case menacée entre e8 et c8
				long mask = (long) 7 << 60;
				if ((mask & this.bitboard.getSquaresAttackedByWhite()) != 0) {
					return false;
				}
				m.setBlackShortCastle();
				return true;
			}
		}
		// Vérifie que la pièce peut se déplacer sur la case d'arrivée
		long possibleMoves = p.possibleMoves(m.getFrom(), this.bitboard);
		long squareToVector = (long) 1 << m.getTo();
		if ((possibleMoves & squareToVector) == 0) {
			return false;
		}
		// Vérifie que la position résultante est légale
		Position newPosition = this.getPositionAfterMove(m);
		if (!newPosition.isPositionLegal()) {
			return false;
		}
		return true;
	}

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
	public boolean isFirstPositionOfGame() {
		return this.getPreviousPosition() == null;
	}

	/**
	 * Retourne true si cette position est la première d'une ligne ou de la
	 * partie
	 */
	public boolean isFirstPositionOfLine() {
		return firstPositionOfLine;
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

	// --------------------------------------------------------------

	/**
	 * Ajoute la position p en tant que variante
	 * 
	 * @param p
	 */
	public void addSubLine(Position p) {
		if (this.getNextPosition() == null) {
			throw new RuntimeException("Ajout d'une variante alors qu'il n'y a pas de ligne principale.");
		}
		p.firstPositionOfLine = true;
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
		// Compte des coups et changement du camp au trait
		position.sideToMove = this.sideToMove == ChessColors.White ? ChessColors.Black : ChessColors.White;
		position.moveCount = position.sideToMove == ChessColors.White ? (this.moveCount + 1) : this.moveCount;

		// Manipulation des pièces
		position.setBitboard(this.getBitboard().copy());
		if (move.isWhiteShortCastle()) {
			position.getBitboard().removeAt(4);
			position.getBitboard().removeAt(7);
			position.getBitboard().setPieceAt(new King(ChessColors.White), 6);
			position.getBitboard().setPieceAt(new Rook(ChessColors.White), 5);
			position.lastMove = "0-0";
			position.removeWhiteLongCastle();
			position.removeWhiteShortCastle();
			return position;
		} else if (move.isWhiteLongCastle()) {
			position.getBitboard().removeAt(4);
			position.getBitboard().removeAt(0);
			position.getBitboard().setPieceAt(new King(ChessColors.White), 2);
			position.getBitboard().setPieceAt(new Rook(ChessColors.White), 3);
			position.lastMove = "0-0-0";
			position.removeWhiteLongCastle();
			position.removeWhiteShortCastle();
			return position;
		} else if (move.isBlackShortCastle()) {
			position.getBitboard().removeAt(60);
			position.getBitboard().removeAt(63);
			position.getBitboard().setPieceAt(new King(ChessColors.Black), 62);
			position.getBitboard().setPieceAt(new Rook(ChessColors.Black), 61);
			position.lastMove = "0-0";
			position.removeBlackLongCastle();
			position.removeBlackShortCastle();
			return position;
		} else if (move.isBlackLongCastle()) {
			position.getBitboard().removeAt(60);
			position.getBitboard().removeAt(56);
			position.getBitboard().setPieceAt(new King(ChessColors.Black), 58);
			position.getBitboard().setPieceAt(new Rook(ChessColors.Black), 59);
			position.lastMove = "0-0-0";
			position.removeBlackLongCastle();
			position.removeBlackShortCastle();
			return position;
		}
		Piece pieceMoving = position.getBitboard().getPieceAt(move.getFrom());
		position.getBitboard().removeAt(move.getFrom());
		position.getBitboard().setPieceAt(pieceMoving, move.getTo());

		// Pion avance de deux cases
		if (pieceMoving.pieceType == PieceType.Pawn && Math.abs(move.getFrom() - move.getTo()) == 16) {
			// Un pion avance de deux cases.
			if (pieceMoving.color == ChessColors.White) {
				position.bitboard.setEpSquare(move.getFrom() + 8);
			} else {
				position.bitboard.setEpSquare(move.getFrom() - 8);
			}
		}

		// Prise en passant
		if (pieceMoving.pieceType == PieceType.Pawn
				&& (Math.abs(move.getFrom() - move.getTo()) == 7 || Math.abs(move.getFrom() - move.getTo()) == 9)) {
			// Detection de la prise par un pion
			if (this.bitboard.getPieceAt(move.getTo()) == null) {
				// Si la case d'arrivée est vide on a une prise en passant.
				// On doit enlever la piece qui a été prise.
				if (pieceMoving.color == ChessColors.White) {
					position.bitboard.removeAt(move.getTo() - 8);
				} else {
					position.bitboard.removeAt(move.getTo() + 8);
				}
			}
		}

		// Roque
		position.castleRights = this.castleRights;
		switch ((int) move.getFrom()) {
		// Gère les déplacements des rois et des tours
		case 0:
			position.removeWhiteLongCastle();
			break;
		case 4:
			position.removeWhiteShortCastle();
			position.removeWhiteLongCastle();
			break;
		case 7:
			position.removeWhiteShortCastle();
			break;
		case 56:
			position.removeBlackLongCastle();
			break;
		case 60:
			position.removeBlackLongCastle();
			position.removeBlackShortCastle();
			break;
		case 63:
			position.removeBlackShortCastle();
			break;
		default:
			break;
		}
		switch ((int) move.getTo()) {
		// Gère la prise des tours
		case 0:
			position.removeWhiteLongCastle();
			break;
		case 7:
			position.removeWhiteShortCastle();
			break;
		case 56:
			position.removeBlackLongCastle();
			break;
		case 63:
			position.removeBlackShortCastle();
			break;
		default:
			break;
		}

		// Génere San
		String san = pieceMoving.getLetter();
		san += move.toString();
		position.lastMove = san;

		return position;
	}

}
