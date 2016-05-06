package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class KnightTest {

	@Test
	public void test() {
		Knight k = new Knight(ChessColors.White);
		assertEquals(Long.parseUnsignedLong("20400000000000",16), k.attacks(Long.parseUnsignedLong("8000000000000000", 16)));
		assertEquals(Long.parseUnsignedLong("4020000000000",16), k.attacks(Long.parseUnsignedLong("100000000000000", 16)));
		assertEquals(Long.parseUnsignedLong("20400",16), k.attacks(Long.parseUnsignedLong("1", 16)));
		assertEquals(Long.parseUnsignedLong("402000",16), k.attacks(Long.parseUnsignedLong("80", 16)));
		assertEquals(Long.parseUnsignedLong("142200221400",16), k.attacks(Long.parseUnsignedLong("8000000", 16)));
	}

}
