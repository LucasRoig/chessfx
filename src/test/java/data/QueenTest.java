package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueenTest {

	@Test
	public void test() {
		Bitboard board = Bitboard.getStartingBitboar();
		Queen q = new Queen(ChessColors.White);
		assertEquals(Long.parseUnsignedLong("141c000000001c14", 16), q.attacks(board.queens, board));
	}

}
