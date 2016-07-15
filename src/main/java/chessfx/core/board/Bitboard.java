package chessfx.core.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import chessfx.core.BitOp;
import chessfx.core.ChessColors;
import chessfx.core.Piece;
import chessfx.core.PieceType;
import chessfx.core.Square;
import chessfx.core.Util;

/**
 * Contient la position de chaque piece.
 * 
 * @author Lucas
 *
 */
public class Bitboard implements IBoard, Serializable {
	public long white; // Présence des pièces Blanches
	public long black; // Présence des pièces Noires
	public long pawns; // Présence des pions
	public long knights; // Présence des cavaliers
	public long bishops; // Présence des fous
	public long rooks; // Présence des tours
	public long queens;// présence des dames
	public long kings; // présence des rois
	private long epSquare; // Vecteur contenant un 1 si une prise en passant est
							// possible sur une case

	// ============Constructeur et autres===========================
	/**
	 * Crée un bitboard vide
	 */
	public Bitboard() {
		this.setEmpty();
	}

	/**
	 * Vide l'échiquier
	 */
	public void setEmpty() {
		this.white = 0;
		this.black = 0;
		this.pawns = 0;
		this.knights = 0;
		this.bishops = 0;
		this.rooks = 0;
		this.queens = 0;
		this.kings = 0;
		this.epSquare = 0;
	}

	/**
	 * Place l'échiquier dans la position de départ d'une partie
	 */
	public void setStarting() {
		this.black = Long.parseUnsignedLong("18446462598732840960");
		this.white = Long.parseUnsignedLong("65535");
		this.pawns = Long.parseUnsignedLong("71776119061282560");
		this.knights = Long.parseUnsignedLong("4755801206503243842");
		this.bishops = Long.parseUnsignedLong("2594073385365405732");
		this.rooks = Long.parseUnsignedLong("9295429630892703873");
		this.queens = Long.parseUnsignedLong("576460752303423496");
		this.kings = Long.parseUnsignedLong("1152921504606846992");
		this.epSquare = 0;
	}

	/**
	 * Renvoie un nouveau bitboard etant une copie du bitboard actuel
	 */
	public Bitboard copy() {
		Bitboard bitboard = new Bitboard();
		bitboard.black = this.black;
		bitboard.white = this.white;
		bitboard.pawns = this.pawns;
		bitboard.bishops = this.bishops;
		bitboard.knights = this.knights;
		bitboard.rooks = this.rooks;
		bitboard.queens = this.queens;
		bitboard.kings = this.kings;
		return bitboard;
	}

	/**
	 * Retourne True si b est dans la meme disposition que le bitboard actuel
	 * 
	 * @param b
	 *            Iboard
	 * @return
	 */
	public boolean equals(IReadableBoard b) {
		for (int i = 0; i < 64; i++) {
			Square s = Square.values()[i];
			if (!this.getPieceAt(s).equals(b.getPieceAt(s)))
				return false;
		}
		if (this.getEpSquare() != b.getEpSquare())
			return false;
		return true;
	}

	// ======================En Passant=====================================

	public void setEpSquare(Square s) {
		this.epSquare = (long) 1 << s.ordinal();
	}

	public Square getEpSquare() {
		if (this.epSquare == 0) {
			return Square.NoSquare;
		} else {
			return Square.values()[(int) Util.lessSignificantBit(epSquare)];
		}
	}

	// ======================Echec==========================================

	/**
	 * 
	 * @return true si le roi blanc est en échec
	 */
	public boolean isWhiteKingInCheck() {
		return (this.blackAttacks() & this.white & this.kings) != 0;
	}

	/**
	 * 
	 * @return true si le roi noir est en échec
	 */
	public boolean isBlackKingInCheck() {
		return (this.whiteAttacks() & this.black & this.kings) != 0;
	}
	// =====================Manipulation des pieces========================

	/**
	 * Retourne la piece occupant la case passee en parametre retourne null si
	 * aucune piece n'est presente
	 * 
	 * @param s
	 * @return
	 */
	public Piece getPieceAt(Square square) {
		ChessColors color;
		long s = (long) 1 << square.ordinal();
		if ((this.black & s) != 0) {
			color = ChessColors.Black;
		} else if ((this.white & s) != 0) {
			color = ChessColors.White;
		} else {
			return null;
		}

		if ((this.pawns & s) != 0) {
			return new Piece(color, PieceType.Pawn);
		} else if ((this.knights & s) != 0) {
			return new Piece(color, PieceType.Knight);
		} else if ((this.bishops & s) != 0) {
			return new Piece(color, PieceType.Bishop);
		} else if ((this.rooks & s) != 0) {
			return new Piece(color, PieceType.Rook);
		} else if ((this.queens & s) != 0) {
			return new Piece(color, PieceType.Queen);
		} else if ((this.kings & s) != 0) {
			return new Piece(color, PieceType.King);
		} else {
			return null;
		}
	}

	/**
	 * Place la piece passee en parametre sur la case passee en parametre
	 * 
	 * @param piece
	 * @param square
	 */
	public void setPieceAt(Square square, Piece piece) {
		long vector = (long) 1 << square.ordinal();
		this.removePieceAt(square);
		switch (piece.getPieceType()) {
		case Pawn:
			this.pawns |= vector;
			break;
		case Knight:
			this.knights |= vector;
			break;
		case Bishop:
			this.bishops |= vector;
			break;
		case Rook:
			this.rooks |= vector;
			break;
		case Queen:
			this.queens |= vector;
			break;
		case King:
			this.kings |= vector;
			break;
		}
		switch (piece.getColor()) {
		case White:
			this.white |= vector;
			break;
		case Black:
			this.black |= vector;
			break;
		}
	}

	/**
	 * Enleve une piece de la case passee en parametre
	 * 
	 * @param square
	 */
	public void removePieceAt(Square s) {
		long one = Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16) - ((long) 1 << s.ordinal());
		this.black &= one;
		this.white &= one;
		this.pawns &= one;
		this.bishops &= one;
		this.knights &= one;
		this.rooks &= one;
		this.queens &= one;
		this.kings &= one;
	}

	// ---------Cases Attaquees--------------------

	/**
	 * Retourne la liste des case atteignables par la piece située sur la case
	 * from Ne vérifié pas la légalité des coups sous-entendus
	 * 
	 * @param from
	 * @return
	 */
	public List<Square> getTargets(Square from) {
		Piece p = this.getPieceAt(from);
		long res = 0;
		if (p == null) {
			return new ArrayList<Square>();
		}
		switch (p.getPieceType()) {
		case Pawn:
			res = p.getColor() == ChessColors.White ? this.whitePawnTargets(from) : this.blackPawnTargets(from);
			break;
		case Knight:
			res = this.knightTargets(from, p.getColor());
			break;
		case Bishop:
			res = this.bishopTargets(from, p.getColor());
			break;
		case Rook:
			res = this.rookTargets(from, p.getColor());
			break;
		case Queen:
			res = this.queenTargets(from, p.getColor());
			break;
		case King:
			res = this.kingTargets(from, p.getColor());
			break;
		}
		return Util.vectorToSquareList(res);
	}

	@Override
	public boolean canWhiteShortCastle() {
		return (Long.parseUnsignedLong("70", 16) & this.blackAttacks()) == 0;
	}

	@Override
	public boolean canWhiteLongCastle() {
		return (Long.parseUnsignedLong("1c", 16) & this.blackAttacks()) == 0;
	}

	@Override
	public boolean canBlackShortCastle() {
		return (Long.parseUnsignedLong("7000000000000000", 16) & this.whiteAttacks()) == 0;
	}

	@Override
	public boolean canBlackLongCastle() {
		return (Long.parseUnsignedLong("1c00000000000000", 16) & this.whiteAttacks()) == 0;
	}
	// =========================Privées========================

	/**
	 * Renvoie les cases sur lesquelles un cavalier Ne vérifie pas la légalité
	 * des coups
	 */
	private long knightTargets(Square s, ChessColors c) {
		long vector = (long) 1 << s.ordinal();
		if (c == ChessColors.White) {
			return this.knightAttacks(vector) & ~this.white;
		} else {
			return this.knightAttacks(vector) & ~this.black;
		}
	}

	/**
	 * Renvoie les cases sur lesquelles un fou Ne vérifie pas la légalité des
	 * coups
	 */
	private long bishopTargets(Square s, ChessColors c) {
		long vector = (long) 1 << s.ordinal();
		if (c == ChessColors.White) {
			return this.bishopAttacks(vector) & ~this.white;
		} else {
			return this.bishopAttacks(vector) & ~this.black;
		}
	}

	/**
	 * Renvoie les cases sur lesquelles une tour Ne vérifie pas la légalité des
	 * coups
	 */
	private long rookTargets(Square s, ChessColors c) {
		long vector = (long) 1 << s.ordinal();
		if (c == ChessColors.White) {
			return this.rookAttacks(vector) & ~this.white;
		} else {
			return this.rookAttacks(vector) & ~this.black;
		}
	}

	/**
	 * Renvoie les cases sur lesquelles une dame Ne vérifie pas la légalité des
	 * coups
	 */
	private long queenTargets(Square s, ChessColors c) {
		long vector = (long) 1 << s.ordinal();
		if (c == ChessColors.White) {
			return this.queenAttacks(vector) & ~this.white;
		} else {
			return this.queenAttacks(vector) & ~this.black;
		}
	}

	/**
	 * Renvoie les cases sur lesquelles un roi Ne vérifie pas la légalité des
	 * coups
	 */
	private long kingTargets(Square s, ChessColors c) {
		long vector = (long) 1 << s.ordinal();
		if (c == ChessColors.White) {
			return this.kingAttacks(vector) & ~this.white;
		} else {
			return this.kingAttacks(vector) & ~this.black;
		}
	}

	/**
	 * Renvoie les cases sur lesquelles un pion blanc peut se déplacer Ne
	 * vérifie pas la légalité des coups
	 */
	private long whitePawnTargets(Square square) {
		long moves = 0;
		long squareVector = (long) 1 << square.ordinal();
		long attackedSquares = this.whitePawnAttacks(squareVector);
		long ep = (long) 1 << this.getEpSquare().ordinal();
		moves = attackedSquares & (this.black | ep);
		moves |= Util.northOne(squareVector);
		// est sur la deuxieme rangée
		if ((squareVector & (~Util.not2row)) != 0 && ((Util.northOne(squareVector) & (this.white | this.black)) == 0) ) {
			moves |= Util.northOne(Util.northOne(squareVector));// 2 vers le
																// haut
		}
		return moves &= ~this.white;
	}

	/**
	 * Renvoie les cases sur lesquelles un pion blanc peut se déplacer Ne
	 * vérifie pas la légalité des coups
	 */
	private long blackPawnTargets(Square square) {
		long moves = 0;
		long squareVector = (long) 1 << square.ordinal();
		long attackedSquares = this.blackPawnAttacks(squareVector);
		long ep = (long) 1 << this.getEpSquare().ordinal();
		moves = attackedSquares & (this.white | ep);
		moves |= Util.southOne(squareVector);
		// est sur la deuxieme rangée
		if ((squareVector & (~Util.not7row)) != 0 && ((Util.southOne(squareVector) & (this.white | this.black)) == 0) ) {
			moves |= Util.southOne(Util.southOne(squareVector));// 2 vers le
																// haut
		}
		return moves &= ~this.black;
	}

	/**
	 * Retourne un vecteur contenant toutes les cases attaquées par les blancs
	 * 
	 * @return
	 */
	private long whiteAttacks() {
		long resultat = this.whitePawnAttacks(this.white & this.pawns);
		resultat |= this.knightAttacks(this.white & this.knights);
		resultat |= this.bishopAttacks(this.white & this.bishops);
		resultat |= this.rookAttacks(this.white & this.rooks);
		resultat |= this.queenAttacks(this.white & this.queens);
		resultat |= this.kingAttacks(this.white & this.kings);
		return resultat;
	}

	/**
	 * Retourne un vecteur contenant toutes les cases attaquées par les blancs
	 * 
	 * @return
	 */
	private long blackAttacks() {
		long resultat = this.blackPawnAttacks(this.black & this.pawns);
		resultat |= this.knightAttacks(this.black & this.knights);
		resultat |= this.bishopAttacks(this.black & this.bishops);
		resultat |= this.rookAttacks(this.black & this.rooks);
		resultat |= this.queenAttacks(this.black & this.queens);
		resultat |= this.kingAttacks(this.black & this.kings);
		return resultat;
	}

	/**
	 * Retourne un vecteur contenant les cases attaquées par les pions blancs
	 * depuis les cases spécifiées dans le vecteur d'entrée
	 * 
	 * @param fromVector
	 * @return
	 */
	private long whitePawnAttacks(long fromVector) {
		long west = (fromVector << 9) & Util.notAFile;
		long east = (fromVector << 7) & Util.notHFile;
		return west | east;
	}

	/**
	 * Retourne un vecteur contenant les cases attaquées par les pions noirs
	 * depuis les cases spécifiées dans le vecteur d'entrée
	 * 
	 * @param fromVector
	 * @return
	 */
	private long blackPawnAttacks(long fromVector) {
		long west = (fromVector >>> 9) & Util.notHFile;
		long east = (fromVector >>> 7) & Util.notAFile;
		return west | east;
	}

	/**
	 * Retourne un vecteur contenant les cases attaquées par les cavaliers
	 * depuis les cases spécifiées dans le vecteur d'entrée
	 * 
	 * @param fromVector
	 * @return
	 */
	private long knightAttacks(long fromVector) {
		long attack = (fromVector & Util.notAFile & Util.notBFile & Util.not1row) >>> 10;
		attack |= (fromVector & Util.notAFile & Util.not1row & Util.not2row) >>> 17;
		attack |= (fromVector & Util.notHFile & Util.not1row & Util.not2row) >>> 15;
		attack |= (fromVector & Util.notGFile & Util.notHFile & Util.not1row) >>> 6;
		attack |= (fromVector & Util.notGFile & Util.notHFile & Util.not8row) << 10;
		attack |= (fromVector & Util.notHFile & Util.not8row & Util.not7row) << 17;
		attack |= (fromVector & Util.notAFile & Util.not8row & Util.not7row) << 15;
		attack |= (fromVector & Util.notAFile & Util.notBFile & Util.not8row) << 6;
		return attack;
	}

	/**
	 * Retourne un vecteur contenant les cases attaquées par les fous depuis les
	 * cases spécifiées dans le vecteur d'entrée
	 * 
	 * @param fromVector
	 * @return
	 */
	private long bishopAttacks(long fromVector) {
		ArrayList<Long> bishopsList = new ArrayList<>();
		long copyVector = fromVector;
		while (copyVector != 0) {
			long square = Util.lessSignificantBit(copyVector);
			bishopsList.add(square);
			copyVector &= ~((long) 1 << square);
		}
		long result = 0;
		for (Long bishop : bishopsList) {
			result |= this.rangeAttack(bishop, Util.NorthWest, Util.LessSignificantBit);
			result |= this.rangeAttack(bishop, Util.NorthEast, Util.LessSignificantBit);
			result |= this.rangeAttack(bishop, Util.SouthWest, Util.MostSignificantBit);
			result |= this.rangeAttack(bishop, Util.SouthEast, Util.MostSignificantBit);
		}
		return result;
	}

	/**
	 * Retourne un vecteur contenant les cases attaquées par les tours depuis
	 * les cases spécifiées dans le vecteur d'entrée
	 * 
	 * @param fromVector
	 * @return
	 */
	private long rookAttacks(long fromVector) {
		ArrayList<Long> rooksList = new ArrayList<>();
		long copyVector = fromVector;
		while (copyVector != 0) {
			long square = Util.lessSignificantBit(copyVector);
			rooksList.add(square);
			copyVector &= ~((long) 1 << square); // suppresion du bit a 1 dans
													// copyVector
		}
		long result = 0;
		for (Long rook : rooksList) {
			result |= this.rangeAttack(rook, Util.North, Util.LessSignificantBit);
			result |= this.rangeAttack(rook, Util.East, Util.LessSignificantBit);
			result |= this.rangeAttack(rook, Util.West, Util.MostSignificantBit);
			result |= this.rangeAttack(rook, Util.South, Util.MostSignificantBit);
		}
		return result;
	}
	
	private long rangeAttack(long position, BitOp direction, BitOp significantBit){
		long withoutCollisions = direction.apply(position);
		long collision = withoutCollisions & (this.white | this.black);
		if(collision != 0){
			long firstCollision = significantBit.apply(collision);
			return withoutCollisions ^ direction.apply(firstCollision);
		}else{
			return withoutCollisions;
		}
	}

	/**
	 * Retourne un vecteur contenant les cases attaquées par les dames depuis
	 * les cases spécifiées dans le vecteur d'entrée
	 * 
	 * @param fromVector
	 * @return
	 */
	private long queenAttacks(long fromVector) {
		return this.rookAttacks(fromVector) | this.bishopAttacks(fromVector);
	}

	/**
	 * Retourne un vecteur contenant les cases attaquées par les Rois depuis les
	 * cases spécifiées dans le vecteur d'entrée
	 * 
	 * @param fromVector
	 * @return
	 */
	private long kingAttacks(long fromVector) {
		long attack = Util.eastOne(fromVector) | Util.westOne(fromVector);
		fromVector |= attack;
		attack |= Util.northOne(fromVector) | Util.southOne(fromVector);
		return attack;
	}
}
