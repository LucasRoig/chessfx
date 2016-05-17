package chessfx.core;

import java.util.List;

import chessfx.core.Piece;
import chessfx.core.board.IPosition;

public class Move {
	// 00000000 00000000 00000000 00111111 = from square = bits 1-6
	// 00000000 00000000 00001111 11000000 = to square = bits 7-12
	// 00000000 00000000 01110000 00000000 = piece type = bits 13-15
	// 00000000 00000001 10000000 00000000 = castle = bit 16-17
	// 00000000 00000010 00000000 00000000 = promotion = bit 18
	// 00000000 00011100 00000000 00000000 = promotion piece = bits 19-21
	// 00000000 00100000 00000000 00000000 = White=0,Black=1 = bit 22
	// 00000000 01000000 00000000 00000000 = gives mate? = bit 23 // NOT YET
	// IMPLEMENTED
	// 00000000 10000000 00000000 00000000 = gives check? = bit 24 // NOT YET
	// IMPLEMENTED
	// 00000001 00000000 00000000 00000000 = captures? = bit 25

	private static final int fromMask = Integer.parseUnsignedInt("111111", 2);
	private static final int toMask = Integer.parseUnsignedInt("111111000000", 2);
	private static final int pieceTypeMask = Integer.parseUnsignedInt("111000000000000", 2);
	private static final int castleMask = Integer.parseUnsignedInt("11000000000000000", 2);
	private static final int promotionMask = Integer.parseUnsignedInt("100000000000000000", 2);
	private static final int promotionTypeMask = Integer.parseUnsignedInt("111000000000000000000", 2);
	private static final int sideMask = Integer.parseUnsignedInt("1000000000000000000000", 2);
	private static final int matMask = Integer.parseUnsignedInt("10000000000000000000000", 2);
	private static final int checkMask = Integer.parseUnsignedInt("100000000000000000000000", 2);
	private static final int capturesMask = Integer.parseUnsignedInt("1000000000000000000000000", 2);

	private static final int fromShift = 0;
	private static final int toShift = 6;
	private static final int pieceTypeShift = 12;
	private static final int castleShift = 15;
	private static final int promotionShift = 17;
	private static final int promotionTypeShift = 18;
	private static final int sideShift = 21;
	private static final int mateShift = 22;
	private static final int checkShift = 23;
	private static final int capturesShifht = 24;

	private static final int longCastleValue = 2;
	private static final int shortCastleValue = 1;

	int data = 0;

	public Move(Square from, Square to, Piece piece) {
		this.setFrom(from);
		this.setTo(to);
		this.setPiece(piece);
		this.setSide(piece.getColor());
		// Roque
		if (this.getPiece().getPieceType() == PieceType.King && this.getFrom() == Square.E1
				&& this.getPiece().getColor() == ChessColors.White) {
			if (this.getTo() == Square.G1)
				this.setShortCastle(true);
			else if (this.getTo() == Square.C1)
				this.setLongCastle(true);
		} else if (this.getPiece().getPieceType() == PieceType.King && this.getFrom() == Square.E8
				&& this.getPiece().getColor() == ChessColors.Black) {
			if (this.getTo() == Square.G8)
				this.setShortCastle(true);
			else if (this.getTo() == Square.C8)
				this.setLongCastle(true);
		}
		// Promotion
		if (this.getPiece().getPieceType() == PieceType.Pawn && this.getPiece().getColor() == ChessColors.White
				&& this.getTo().getRow() == 8) {
			this.setPromotion(true);
		} else if (this.getPiece().getPieceType() == PieceType.Pawn && this.getPiece().getColor() == ChessColors.Black
				&& this.getTo().getRow() == 1) {
			this.setPromotion(true);
		}
	}

	static public Move getMoveFromSan(String san, IPosition p) throws Exception {
		// TODO : Réécrire la méthode, 1 : Trier pour la piece 2 : Trier pour
		// les cases. Conserver la san d'origine pour debug.
		san = san.replace("#", "");
		san = san.replace("+", "");
		List<Move> possibleMoves = p.getAllMoves();
		if (san.equals("O-O")) {
			for (Move m : possibleMoves) {
				if (m.isShortCastle()) {
					return m;
				}
			}
			throw new Exception("Invalid san : " + san);
		} else if (san.equals("0-0-0")) {
			for (Move m : possibleMoves) {
				if (m.isLongCastle()) {
					return m;
				}
			}
			throw new Exception("Invalid san : " + san);
		} else if (Character.isLowerCase(san.charAt(0))) {
			// C'est un pion
			possibleMoves.removeIf(m -> !(m.getPiece().getPieceType() == PieceType.Pawn));
			boolean promotion = false;
			PieceType promotionType = PieceType.Pawn;
			if (san.contains("=")) {
				promotion = true;
				switch (san.charAt(san.length() - 1)) {
				case 'Q':
					promotionType = PieceType.Queen;
					break;
				case 'R':
					promotionType = PieceType.Rook;
					break;
				case 'B':
					promotionType = PieceType.Bishop;
					break;
				case 'N':
					promotionType = PieceType.Knight;
					break;
				default:
					throw new Exception("Invalid san : " + san);
				}
				san = san.substring(0, san.length() - 2);
			}
			if (san.length() == 2) {
				// simple avancée
				Square to = Square.fromString(san);
				possibleMoves.removeIf(
						m -> !((m.getTo() == to) && (m.getFrom().columnLetter().equals(m.getTo().columnLetter()))));
				if (possibleMoves.size() != 1) {
					throw new Exception("Invalid san : " + san);
				}
				Move res = possibleMoves.get(0);
				if (promotion) {
					res.setPromotion(true);
					res.setPromotionPiece(promotionType);
				}
				return res;
			} else {
				// C'est une prise
				Square to = Square.fromString(san.substring(2));
				possibleMoves.removeIf(m -> m.getTo() != to);
				String columnFrom = san.substring(0, 1);
				possibleMoves.removeIf(m -> !m.getFrom().columnLetter().equals(columnFrom));
				if (possibleMoves.size() != 1) {
					throw new Exception("Invalid san : " + san);
				}
				Move res = possibleMoves.get(0);
				if (promotion) {
					res.setPromotion(true);
					res.setPromotionPiece(promotionType);
				}
				return res;
			}
		} else {
			PieceType piece;
			switch (san.charAt(0)) {
			case 'Q':
				piece = PieceType.Queen;
				break;
			case 'K':
				piece = PieceType.King;
				break;
			case 'R':
				piece = PieceType.Rook;
				break;
			case 'B':
				piece = PieceType.Bishop;
				break;
			case 'N':
				piece = PieceType.Knight;
				break;
			default:
				throw new Exception("Invalid san : " + san);
			}
			possibleMoves.removeIf(m -> m.getPiece().getPieceType() != piece);
			san = san.substring(1);
			san = san.replace("x", "");
			if (san.length() == 3) {
				if (Character.isDigit(san.charAt(0))) {
					int rowFrom = Integer.parseInt(san.substring(0, 1));
					possibleMoves.removeIf(m -> m.getFrom().getRow() != rowFrom);
					san = san.substring(1);
				} else {
					String columnFrom = san.substring(0, 1);
					possibleMoves.removeIf(m -> !m.getFrom().columnLetter().equals(columnFrom));
					san = san.substring(1);
				}
			}
			Square to = Square.fromString(san);
			possibleMoves.removeIf(m -> m.getTo() != to);
			if (possibleMoves.size() == 1) {
				return possibleMoves.get(0);
			} else {
				throw new Exception("Invalid san : " + san);
			}
		}
	}

	public String toString() {

		if (this.isLongCastle())
			return "O-O-O";
		if (this.isShortCastle())
			return "O-O";

		String s = "";
		switch (this.getPiece().getPieceType()) {
		case Pawn:
			if (this.isCapture())
				s += this.getFrom().columnLetter();
			break;
		case Knight:
			s += "N";
			break;
		case Bishop:
			s += "B";
			break;
		case Rook:
			s += "R";
			break;
		case Queen:
			s += "Q";
			break;
		case King:
			s += "K";
			break;
		default:
			break;
		}
		if (this.isCapture()) {
			s += "x";
		}
		return s + this.getTo().toString();
	}

	public void setFrom(Square from) {
		this.data &= ~fromMask;
		this.data |= from.ordinal() << fromShift;
	}

	public Square getFrom() {
		return Square.values()[(this.data & fromMask) >>> fromShift];
	}

	public void setTo(Square to) {
		this.data &= ~toMask;
		this.data |= to.ordinal() << toShift;
	}

	public Square getTo() {
		return Square.values()[(this.data & toMask) >> toShift];
	}

	public void setPiece(Piece p) {
		this.data &= ~pieceTypeMask & ~sideMask;
		this.setSide(p.getColor());
		this.data |= p.getPieceType().ordinal() << pieceTypeShift;
	}

	public Piece getPiece() {
		ChessColors color = this.getSide();
		PieceType type = PieceType.values()[(this.data & pieceTypeMask) >>> pieceTypeShift];
		return new Piece(color, type);
	}

	public void setLongCastle(boolean isLongCastle) {
		this.data &= ~castleMask;
		if (isLongCastle) {
			this.data |= longCastleValue << castleShift;
		}
	}

	public void setShortCastle(boolean isShortCastle) {
		this.data &= ~castleMask;
		if (isShortCastle) {
			this.data |= shortCastleValue << castleShift;
		}
	}

	public boolean isShortCastle() {
		return ((this.data & castleMask) >>> castleShift) == shortCastleValue;
	}

	public boolean isLongCastle() {
		return ((this.data & castleMask) >>> castleShift) == longCastleValue;
	}

	public boolean isCastle() {
		return this.isShortCastle() || this.isLongCastle();
	}

	public void setPromotion(boolean isPromotion) {
		this.data &= ~promotionMask;
		if (isPromotion) {
			this.data |= 1 << promotionShift;
		}
	}

	public boolean isPromotion() {
		return ((this.data & promotionMask) >>> promotionShift) == 1;
	}

	public void setPromotionPiece(PieceType p) {
		this.setPromotion(true);
		this.data &= ~promotionTypeMask;
		this.data |= p.ordinal() << promotionTypeShift;
	}

	public Piece getPromotionPiece() {
		ChessColors color = this.getSide();
		PieceType type = PieceType.values()[(this.data & promotionTypeMask) >>> promotionTypeShift];
		return new Piece(color, type);
	}

	public void setSide(ChessColors c) {
		this.data &= ~sideMask;
		this.data |= c.ordinal() << sideShift;
	}

	public ChessColors getSide() {
		return ChessColors.values()[(this.data & sideMask) >>> sideShift];
	}

	public void setCaptures(boolean isCapture) {
		this.data &= ~capturesMask;
		if (isCapture) {
			this.data |= 1 << capturesShifht;
		}
	}

	public boolean isCapture() {
		return ((this.data & capturesMask) >>> capturesShifht) == 1;
	}
}
