package chessClassicData;

import static org.junit.Assert.*;

import org.junit.Test;

import chessClassicData.Bishop;
import chessClassicData.Bitboard;
import chessClassicData.ChessColors;

public class BishopTest {

	@Test
	public void test() {
		Bitboard board = Bitboard.getStartingBitboar();
		Bishop b = new Bishop(ChessColors.White);
		assertEquals(Long.parseUnsignedLong("5a000000005a00", 16), b.attacks(board.bishops, board));
	}

}
