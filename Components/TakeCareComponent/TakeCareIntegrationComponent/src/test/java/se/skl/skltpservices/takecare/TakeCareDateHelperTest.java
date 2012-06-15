package se.skl.skltpservices.takecare;

import static org.junit.Assert.*;

import org.junit.Test;

public class TakeCareDateHelperTest {

	@Test
	public void testRemoveSecondsFromRivTaTime() {
		String actual = TakeCareDateHelper.removeSecondsFromRivTaTime("20120612121530");
		assertEquals("201206121215", actual);
	}

	@Test(expected = RuntimeException.class)
	public void testRemoveSecondsFromRivTaTimeThrowsErrorWhenNull() {
		TakeCareDateHelper.removeSecondsFromRivTaTime(null);
		fail("Excpected RuntimeException");
	}

	@Test(expected = RuntimeException.class)
	public void testRemoveSecondsFromRivTaTimeThrowsErrorWhenEmpty() {
		TakeCareDateHelper.removeSecondsFromRivTaTime("");
		fail("Excpected RuntimeException");
	}

	@Test(expected = RuntimeException.class)
	public void testRemoveSecondsFromRivTaTimeThrowsErrorWhenWrongLength() {
		TakeCareDateHelper.removeSecondsFromRivTaTime("123");
		fail("Excpected RuntimeException");
	}

}
