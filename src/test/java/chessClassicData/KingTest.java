package chessClassicData;

import static org.junit.Assert.*;

import org.junit.Test;

import chessClassicData.Bitboard;
import chessClassicData.ChessColors;
import chessClassicData.King;

public class KingTest {

	@Test
	public void test() {
		Bitboard b = new Bitboard();
		King k = new King(ChessColors.White);
		assertEquals(Long.parseUnsignedLong("40c0000000000000", 16),
				k.attacks(Long.parseUnsignedLong("8000000000000000", 16), b));
		assertEquals(Long.parseUnsignedLong("c040", 16), k.attacks(Long.parseUnsignedLong("80", 16), b));
		assertEquals(Long.parseUnsignedLong("302", 16), k.attacks(Long.parseUnsignedLong("1", 16), b));
		assertEquals(Long.parseUnsignedLong("203000000000000", 16),
				k.attacks(Long.parseUnsignedLong("100000000000000", 16), b));
	}

}
