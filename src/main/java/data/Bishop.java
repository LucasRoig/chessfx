package data;

import java.util.ArrayList;

public class Bishop extends Piece {
	public Bishop(ChessColors color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return this.color == ChessColors.White ? "\u2657" : "\u265D";
	}

	@Override
	public long possibleMoves(int square, Bitboard bitboard) {
		// TODO A compléter
		return 0;
	}

	@Override
	public void setAt(long square, Bitboard bitboard) {
		// On place les 1 ou il faut
		long un = (long) 1 << square;
		bitboard.bishops |= un;
		if (this.color == ChessColors.Black) {
			bitboard.black |= un;
		} else {
			bitboard.white |= un;
		}

		// On enleve les 1 des autres long
		un = Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16) - ((long) 1 << square);
		bitboard.pawns &= un;
		bitboard.knights &= un;
		bitboard.rooks &= un;
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
		return "B";
	}

	@Override
	public long attacks(long fromVector, Bitboard board) {
		ArrayList<Long> bishopsList = new ArrayList<>();
		long copyVector = fromVector;
		while(copyVector != 0){
			long square = Util.lessSignificantBit(copyVector);
			bishopsList.add(square);
			copyVector &= ~((long)1 << square);
		}
		
		long result = 0;
		for (Long bishop : bishopsList) {
			//Nord Ouest
			long withoutCollisions = Util.northWest(bishop);
			long collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0){
				long firstCollision = Util.lessSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.northWest(firstCollision));
			}else{
				result |= withoutCollisions;
			}
			
			//Nord Est
			withoutCollisions = Util.northEast(bishop);
			collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0){
				long firstCollision = Util.lessSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.northEast(firstCollision));
			}else{
				result |= withoutCollisions;
			}
			
			//Sud Est
			withoutCollisions = Util.southEast(bishop);
			collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0){
				long firstCollision = Util.mostSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.southEast(firstCollision));
			}else{
				result |= withoutCollisions;
			}
			
			//Sud Ouest
			withoutCollisions = Util.southWest(bishop);
			collisions = withoutCollisions & (board.white | board.black);
			if (collisions != 0){
				long firstCollision = Util.mostSignificantBit(collisions);
				result |= (withoutCollisions ^ Util.southWest(firstCollision));
			}else{
				result |= withoutCollisions;
			}
		}
		
		return result;
	}
}
