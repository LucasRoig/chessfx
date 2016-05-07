package chessClassicData;

import java.util.ArrayList;

public class Rook extends Piece {
	public Rook(ChessColors color) {
		this.color = color;
		this.pieceType = PieceType.Rook;
	}

	@Override
	public String toString() {
		return this.color == ChessColors.White ? "\u2656" : "\u265C";
	}

	@Override
	public void setAt(long square, Bitboard bitboard) {
		// On place les 1 ou il faut
		long un = (long) 1 << square;
		bitboard.rooks |= un;
		if (this.color == ChessColors.Black) {
			bitboard.black |= un;
		} else {
			bitboard.white |= un;
		}

		// On enleve les 1 des autres long
		un = Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16) - ((long) 1 << square);
		bitboard.pawns &= un;
		bitboard.knights &= un;
		bitboard.bishops &= un;
		bitboard.queens &= un;
		bitboard.kings &= un;
		if (this.color == ChessColors.Black) {
			bitboard.white &= un;
		} else {
			bitboard.black &= un;
		}
	}

	@Override
	public String getLetter() {
		return "R";
	}

	@Override
	public long attacks(long fromVector, Bitboard board) {
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
			// Deplacement vers le haut
			long withoutCollisions = Util.north(rook);
			long collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0) {
				long firstCollision = Util.lessSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.north(firstCollision));
			} else {
				result |= withoutCollisions;
			}

			// Deplacement vers le bas
			withoutCollisions = Util.south(rook);
			collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0) {
				long firstCollision = Util.mostSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.south(firstCollision));
			} else {
				result |= withoutCollisions;
			}

			// Deplacement vers l'est
			withoutCollisions = Util.east(rook);
			collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0) {
				long firstCollision = Util.lessSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.east(firstCollision));
			} else {
				result |= withoutCollisions;
			}

			// Deplacement vers l'ouest
			withoutCollisions = Util.west(rook);
			collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0) {
				long firstCollision = Util.mostSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.west(firstCollision));
			} else {
				result |= withoutCollisions;
			}
		}
		return result;
	}
}
