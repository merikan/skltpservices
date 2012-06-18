package se.skl.skltpservices.takecare;

import static org.junit.Assert.*;

import org.junit.Test;

public class TakeCareDateHelperTest {

	@Test
	public void testRemoveSecondsFromRivTaTime() {
		String actual = TakeCareDateHelper.toTakeCareLongTime("20120612121530");
		assertEquals("201206121215", actual);
	}

	@Test(expected = RuntimeException.class)
	public void testRemoveSecondsFromRivTaTimeThrowsErrorWhenNull() {
		TakeCareDateHelper.toTakeCareLongTime(null);
		fail("Excpected RuntimeException");
	}

	@Test(expected = RuntimeException.class)
	public void testRemoveSecondsFromRivTaTimeThrowsErrorWhenEmpty() {
		TakeCareDateHelper.toTakeCareLongTime("");
		fail("Excpected RuntimeException");
	}

	@Test(expected = RuntimeException.class)
	public void testRemoveSecondsFromRivTaTimeThrowsErrorWhenWrongLength() {
		TakeCareDateHelper.toTakeCareLongTime("123");
		fail("Excpected RuntimeException");
	}

}
