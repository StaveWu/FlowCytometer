package utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

	@Test
	void testSpecialCharFilter() {
		fail("Not yet implemented");
	}

	@Test
	void testGetNumFrom() {
		fail("Not yet implemented");
	}

	@Test
	void testGetTail() {
		assertEquals("B", StringUtils.getTail("C://A/C/B"));
		assertEquals("B", StringUtils.getTail("C://A/C\\B"));
	}

	@Test
	void testGetParentPath() {
		assertEquals("C://A", StringUtils.getParentPath("C://A/B"));
	}
	
	@Test
	void testGetExtension() {
		assertEquals("txt", StringUtils.getExtension("C://A/B.txt"));
	}

}
