package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class PawnTest {

	@Test
	public void test() {
		Pawn p = new Pawn(ChessColors.White);
		assertEquals(Long.parseUnsignedLong("14af520000",16), p.attacks(Long.parseUnsignedLong("856a100", 16)));
		assertEquals(Long.parseUnsignedLong("2152a680000",16), p.attacks(Long.parseUnsignedLong("10a149000", 16)));
		assertEquals(Long.parseUnsignedLong("ff00000000000000",16), p.attacks(Long.parseUnsignedLong("ff000000000000", 16)));
		p = new Pawn(ChessColors.Black);
		assertEquals(Long.parseUnsignedLong("2a55a0400000",16), p.attacks(Long.parseUnsignedLong("152a4080000000", 16)));
		assertEquals(Long.parseUnsignedLong("542af5",16), p.attacks(Long.parseUnsignedLong("88156200", 16)));
	}

}