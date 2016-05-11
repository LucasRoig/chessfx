package chessClassicData;

import static org.junit.Assert.*;

import org.junit.Test;

import chessfx.core.Util;

public class UtilTest {

	@Test
	public void test() {
		assertEquals(Long.parseUnsignedLong("102040810204000", 16), Util.northWest(7));
		assertEquals(Long.parseUnsignedLong("102040800000000", 16), Util.northWest(28));
		assertEquals(Long.parseUnsignedLong("0", 16), Util.northWest(63));

		assertEquals(Long.parseUnsignedLong("8040201008040200", 16), Util.northEast(0));
		assertEquals(Long.parseUnsignedLong("8040000000000000", 16), Util.northEast(45));
		assertEquals(Long.parseUnsignedLong("0", 16), Util.northEast(63));

		assertEquals(Long.parseUnsignedLong("0", 16), Util.southEast(63));
		assertEquals(Long.parseUnsignedLong("2040810204080", 16), Util.southEast(56));
		assertEquals(Long.parseUnsignedLong("204080000000", 16), Util.southEast(52));

		assertEquals(Long.parseUnsignedLong("40201008040201", 16), Util.southWest(63));
		assertEquals(Long.parseUnsignedLong("4020", 16), Util.southWest(23));
		assertEquals(Long.parseUnsignedLong("0", 16), Util.southWest(1));

		assertEquals(Long.parseUnsignedLong("101010101010100", 16), Util.north(0));
		assertEquals(Long.parseUnsignedLong("101010000000000", 16), Util.north(32));
		assertEquals(Long.parseUnsignedLong("0", 16), Util.north(63));

		assertEquals(Long.parseUnsignedLong("80808080808080", 16), Util.south(63));
		assertEquals(Long.parseUnsignedLong("80808", 16), Util.south(27));
		assertEquals(Long.parseUnsignedLong("0", 16), Util.south(6));

		assertEquals(Long.parseUnsignedLong("fe00000000000000", 16), Util.east(56));
		assertEquals(Long.parseUnsignedLong("8000000000000000", 16), Util.east(62));
		assertEquals(Long.parseUnsignedLong("0", 16), Util.east(63));

		assertEquals(Long.parseUnsignedLong("7f00000000000000", 16), Util.west(63));
		assertEquals(Long.parseUnsignedLong("f", 16), Util.west(4));
		assertEquals(Long.parseUnsignedLong("0", 16), Util.west(0));

		assertEquals(0, Util.lessSignificantBit(1));
		assertEquals(1, Util.lessSignificantBit(2));
		assertEquals(0, Util.lessSignificantBit(63));

		assertEquals(0, Util.mostSignificantBit(1));
		assertEquals(1, Util.mostSignificantBit(3));
		assertEquals(5, Util.mostSignificantBit(63));
	}

}
