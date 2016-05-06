package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class KingTest {

	@Test
	public void test() {
		King k = new King(ChessColors.White);
		assertEquals(Long.parseUnsignedLong("40c0000000000000",16), k.attacks(Long.parseUnsignedLong("8000000000000000", 16)));
		assertEquals(Long.parseUnsignedLong("c040",16), k.attacks(Long.parseUnsignedLong("80", 16)));
		assertEquals(Long.parseUnsignedLong("302",16), k.attacks(Long.parseUnsignedLong("1", 16)));
		assertEquals(Long.parseUnsignedLong("203000000000000",16), k.attacks(Long.parseUnsignedLong("100000000000000", 16)));
	}

}
