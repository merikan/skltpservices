package se.skl.skltpservices.takecare.takecareintegrationcomponent.getsubjectofcareschedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.soitoolkit.commons.mule.util.MiscUtil;

public class GetSubjectOfCareScheduleResponseTransformerTest {

	@Test
	public void testTransformer_ok() throws Exception {

		// Specify input and expected result

		String input = MiscUtil
				.readFileAsString("src/test/resources/testfiles/GetSubjectOfCareSchedule/response-input.xml");

		String expectedResult = MiscUtil
				.readFileAsString("src/test/resources/testfiles/GetSubjectOfCareSchedule/response-output.xml");

		// Create the transformer under test and let it perform the
		// transformation

		GetSubjectOfCareScheduleResponseTransformer transformer = new GetSubjectOfCareScheduleResponseTransformer();
		String result = (String) transformer.pojoTransform(input, "UTF-8");

		// Compare the result to the expected value
		assertEquals(expectedResult, result);
	}

	@Test
	public void testTransformBookingAllowed() throws Exception {
		GetSubjectOfCareScheduleResponseTransformer transformer = new GetSubjectOfCareScheduleResponseTransformer();
		boolean bookingAllowed = transformer.shortToBoolean(Short.valueOf("1"));
		assertTrue(bookingAllowed);
	}

	@Test
	public void testTransformBookingNotAllowed() throws Exception {
		GetSubjectOfCareScheduleResponseTransformer transformer = new GetSubjectOfCareScheduleResponseTransformer();
		boolean bookingAllowed = transformer.shortToBoolean(Short.valueOf("0"));
		assertFalse(bookingAllowed);
	}

	@Test
	public void testTransformBookingNotAllowedWhenIllegalValue() throws Exception {
		GetSubjectOfCareScheduleResponseTransformer transformer = new GetSubjectOfCareScheduleResponseTransformer();
		boolean bookingAllowed = transformer.shortToBoolean(Short.valueOf("23"));
		assertFalse(bookingAllowed);
	}

}