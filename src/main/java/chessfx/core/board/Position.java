package chessfx.core.board;

import java.util.ArrayList;
import java.util.List;

import chessfx.core.ChessColors;
import chessfx.core.InvalidPieceException;
import chessfx.core.Move;
import chessfx.core.Piece;
import chessfx.core.PieceType;
import chessfx.core.Square;

/*
 * Contient une position, c'est à dire le bitboard associé ainsi qu'un pointeur
 * vers la ou les prochaine positions ainsi que vers la position précédente.
 */
public class Position implements IWritableGamePosition{
	private short castleRights = 15;
	// Droits roque
	// 0001 - petit roque blanc
	// 0010 - grand roque blanc
	// 0100 - petit roque noir
	// 1000 - grand roque noir

	IBoard board;
	IWritableGamePosition nextPosition;
	IWritableGamePosition previousPosition;
	Move lastMove; // San du coup précédent
	int moveCount;
	ChessColors sideToMove;
	List<IReadableGamePosition> sublines = new ArrayList<>();
	boolean firstPositionOfLine;
	int index; // Index de la position afin de la retrouver.

	//================Constructeurs et autres===========
	
	public Position(IBoard board) {
		this.board = board;
		this.setStarting();
	}

	/**
	 * Vide l'échiquier
	 */
	public void setEmpty(){
		this.board.setEmpty();
		this.castleRights = 15;
		this.nextPosition = null;
		this.previousPosition = null;
		this.lastMove = null;
		this.moveCount = 1;
		this.sideToMove = ChessColors.White;
		this.sublines = new ArrayList<>();
		this.firstPositionOfLine = false;
		this.setId(0);
		
	}
	/**
	 * Place l'échiquier dans la position de départ d'une partie
	 */
	public void setStarting(){
		this.board.setStarting();
		this.castleRights = 15;
		this.nextPosition = null;
		this.previousPosition = null;
		this.lastMove = null;
		this.moveCount = 1;
		this.sideToMove = ChessColors.White;
		this.sublines = new ArrayList<>();
		this.firstPositionOfLine = false;
		this.setId(0);
	}

	// ----------Getter Setter-------------------------
	
	
	@Override
	public Piece getPieceAt(Square s) {
		return this.board.getPieceAt(s);
	}

	@Override
	public void setIsFirstOfTheLine(boolean b) {
		this.firstPositionOfLine = b;
	}
	
	public void removeSubline(IWritableGamePosition p){
		this.sublines.remove(p);
	}
	
	public int getId() {
		return index;
	}

	public void setId(int index) {
		this.index = index;
	}

	public IWritableGamePosition getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(IWritableGamePosition nextPosition) {
		this.nextPosition = nextPosition;
	}

	public IWritableGamePosition getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(IWritableGamePosition previousPosition) {
		this.previousPosition = previousPosition;
	}

	public Move getLastMove() {
		return lastMove;
	}

	public int getMoveCount() {
		return moveCount;
	}
	
	@Override
	public boolean isWhiteToMove() {
		return this.sideToMove == ChessColors.White;
	}

	public List<IReadableGamePosition> getSublines() {
		return sublines;
	}
	
	public Square getEpSquare(){
		return this.board.getEpSquare();
	}

	// --------------Roque-----------------------------
	private boolean canWhiteShortCastle() {
		return (this.castleRights & 1) != 0 && this.board.canWhiteShortCastle();
	}

	private boolean canWhiteLongCastle() {
		return (this.castleRights & 2) != 0 && this.board.canWhiteLongCastle();
	}

	private boolean canBlackShortCastle() {
		return (this.castleRights & 4) != 0 && this.board.canBlackShortCastle();
	}

	private boolean canBlackLongCastle() {
		return (this.castleRights & 8) != 0 && this.board.canBlackLongCastle();
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
	
	@Override
	public List<Square> getValidTargets(Square from) {
		List<Square> l = this.board.getTargets(from);
		List<Square> res = new ArrayList<>();
		for (Square to : l) {
			Move m = new Move(from, to, this.board.getPieceAt(from));
			try {
				if(this.getPositionAfter(m).isLegal())
					res.add(to);
			} catch (InvalidPieceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Piece movingPiece = this.board.getPieceAt(from);
		if(movingPiece.getPieceType() == PieceType.King && (from == Square.E1 || from == Square.E8)){
			if(movingPiece.getColor() == ChessColors.White){
				if(canWhiteShortCastle()){
					res.add(Square.G1);
				}
				if(canWhiteLongCastle()){
					res.add(Square.C1);
				}
			}else{
				if(canBlackShortCastle()){
					res.add(Square.G8);
				}
				if(canBlackLongCastle()){
					res.add(Square.C8);
				}
			}
		}
		return res;
	}

	/**
	 * Retourne true si la position actuelle est légale
	 */
	public boolean isLegal() {
		// Vérifie que le camp n'étant pas au trait n'est pas en échec
		if(this.sideToMove == ChessColors.White){
			return !this.board.isBlackKingInCheck();
		}else{
			return !this.board.isWhiteKingInCheck();
		}
	}

	
	/**
	 * Retourne true si le coup peut être joué dans la position actuelle
	 */
	/*
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
	}*/

	/**
	 * retourne true si cette position est la premiere de la partie
	 * 
	 * @return
	 */
	public boolean isFirstPositionOfTheGame(){
		return this.getPreviousPosition() == null;
	}

	/**
	 * Retourne true si cette position est la première d'une ligne ou de la
	 * partie
	 */
	public boolean isFirstPositionOfTheLine() {
		return firstPositionOfLine;
	}

	/**
	 * Renvoie true si les deux positions sont identiques
	 * 
	 * @param p
	 * @return
	 */
	public boolean equals(IReadableBoard p) {
		for (Square s : Square.values()) {
			Piece piece = this.getPieceAt(s);
			if(piece != null && !piece.equals(p.getPieceAt(s))){
				return false;
			}
		}
		return true;
	}

	/**
	 * retourne true si cette position est la premiere de la ligne
	 */
	public boolean isLastPositionOfTheLine() {
		return this.getNextPosition() == null;
	}

	// --------------------------------------------------------------

	/**
	 * Ajoute la position p en tant que variante
	 * 
	 * @param p
	 */
	public void addSubline(IWritableGamePosition p) {
		if (this.getNextPosition() == null) {
			throw new RuntimeException("Ajout d'une variante alors qu'il n'y a pas de ligne principale.");
		}
		p.setIsFirstOfTheLine(true);
		this.sublines.add(p);
	}

	/**
	 * Retourne la position après que le coup passé en paramètre ait été joué.
	 * 
	 * @param move
	 * @return
	 * @throws InvalidPieceException 
	 */
	public Position getPositionAfter(Move move) throws InvalidPieceException {
		if (move.isCastle()){
			return this.getPositionAfterCastle(move);
		}
		Position position = new Position(this.board.copy());
		position.board = this.board.copy();
		position.setPreviousPosition(this);
		// Compte des coups et changement du camp au trait
		position.sideToMove = this.sideToMove == ChessColors.White ? ChessColors.Black : ChessColors.White;
		position.moveCount = position.sideToMove == ChessColors.White ? (this.moveCount + 1) : this.moveCount;

		// Manipulation des pièces
		Piece pieceMoving = position.board.getPieceAt(move.getFrom());
		position.board.removePieceAt(move.getFrom());
		position.board.setPieceAt(move.getTo(),pieceMoving);

		// Pion avance de deux cases
		if (pieceMoving.getPieceType() == PieceType.Pawn && Math.abs(move.getFrom().ordinal() - move.getTo().ordinal()) == 16) {
			// Un pion avance de deux cases.
			if (pieceMoving.getColor() == ChessColors.White) {
				position.board.setEpSquare(Square.values()[move.getFrom().ordinal() + 8]);
			} else {
				position.board.setEpSquare(Square.values()[move.getFrom().ordinal() - 8]);
			}
		}
		// Prise en passant
		if (pieceMoving.getPieceType() == PieceType.Pawn
				&& (Math.abs(move.getFrom().ordinal() - move.getTo().ordinal()) == 7 || Math.abs(move.getFrom().ordinal() - move.getTo().ordinal()) == 9)) {
			// Detection de la prise par un pion
			if (this.board.getPieceAt(move.getTo()) == null) {
				// Si la case d'arrivée est vide on a une prise en passant.
				// On doit enlever la piece qui a été prise.
				if (pieceMoving.getColor() == ChessColors.White) {
					position.board.removePieceAt(Square.values()[move.getTo().ordinal() - 8]);
				} else {
					position.board.removePieceAt(Square.values()[move.getTo().ordinal() + 8]);
				}
			}
		}
		// Roque
		position.castleRights = this.castleRights;
		if (move.getFrom() == Square.E1){
			position.removeWhiteLongCastle();
			position.removeWhiteShortCastle();
		}else if (move.getFrom() == Square.E8){
			position.removeBlackLongCastle();
			position.removeBlackShortCastle();
		}else if (move.getFrom() == Square.A1 || move.getTo() == Square.A1){
			position.removeWhiteShortCastle();
		}else if (move.getFrom() == Square.H1 || move.getTo() == Square.H1){
			position.removeWhiteShortCastle();
		}else if (move.getFrom() == Square.A8 || move.getTo() == Square.A8){
			position.removeBlackLongCastle();
		}else if (move.getFrom() == Square.H8 || move.getTo() == Square.H8){
			position.removeBlackShortCastle();
		}

		// Configure le coup
		
		position.lastMove = move;
		if(this.board.getPieceAt(move.getTo()) != null){
			position.lastMove.setCaptures(true);
		}
		return position;
	}
	
	private Position getPositionAfterCastle(Move m) throws InvalidPieceException{
		Position position = new Position(this.board.copy());
		position.board = this.board.copy();
		position.setPreviousPosition(this);
		// Compte des coups et changement du camp au trait
		position.sideToMove = this.sideToMove == ChessColors.White ? ChessColors.Black : ChessColors.White;
		position.moveCount = position.sideToMove == ChessColors.White ? (this.moveCount + 1) : this.moveCount;
		
		position.board.removePieceAt(m.getFrom());
		if(m.getFrom() == Square.E1){
			position.board.setPieceAt(m.getTo(),new Piece(ChessColors.White, PieceType.King));
			if (m.isShortCastle()){
				position.board.removePieceAt(Square.H1);
				position.board.setPieceAt(Square.F1, new Piece(ChessColors.White, PieceType.Rook));
			}else{
				position.board.removePieceAt(Square.A1);
				position.board.setPieceAt(Square.D1, new Piece(ChessColors.White, PieceType.Rook));
			}
			position.removeWhiteLongCastle();
			position.removeWhiteShortCastle();
		}else{
			position.board.setPieceAt(m.getTo(), new Piece(ChessColors.Black, PieceType.King));
			if (m.isShortCastle()){
				position.board.removePieceAt(Square.H8);
				position.board.setPieceAt(Square.F8, new Piece(ChessColors.Black, PieceType.Rook));
			}else{
				position.board.removePieceAt(Square.A8);
				position.board.setPieceAt(Square.D8, new Piece(ChessColors.Black, PieceType.Rook));
			}
			position.removeBlackLongCastle();
			position.removeBlackShortCastle();
		}
		position.lastMove = m;
		return position;
	}
}
