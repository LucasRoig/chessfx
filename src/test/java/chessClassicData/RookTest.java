package chessClassicData;

import static org.junit.Assert.*;

import org.junit.Test;

import chessClassicData.Bitboard;
import chessClassicData.ChessColors;
import chessClassicData.Rook;

public class RookTest {

	@Test
	public void test() {
		Bitboard board = Bitboard.getStartingBitboar();
		Rook r = new Rook(ChessColors.White);
		assertEquals(Long.parseUnsignedLong("4281000000008142", 16), r.attacks(board.rooks, board));
	}

}