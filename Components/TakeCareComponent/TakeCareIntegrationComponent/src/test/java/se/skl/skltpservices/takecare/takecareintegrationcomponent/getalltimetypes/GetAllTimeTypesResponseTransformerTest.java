package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.soitoolkit.commons.mule.util.MiscUtil;

public class GetAllTimeTypesResponseTransformerTest {

	@Test
	public void testTransformer_ok() throws Exception {

		String input = MiscUtil.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/response-input.xml");
		String expectedResult = MiscUtil
				.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/response-output.xml");
		GetAllTimeTypesResponseTransformer transformer = new GetAllTimeTypesResponseTransformer();
		String result = (String) transformer.pojoTransform(input, "UTF-8");

		assertEquals(expectedResult, result);
	}

	@Test(expected = RuntimeException.class)
	public void testTransformer_error() throws Exception {

		String input = MiscUtil
				.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/response-error-input.xml");
		GetAllTimeTypesResponseTransformer transformer = new GetAllTimeTypesResponseTransformer();
		transformer.pojoTransform(input, "UTF-8");

		fail("Expected RuntimeException!");
	}

	@Test
	public void testTransformer_error_correctMessage() throws Exception {

		String input = MiscUtil
				.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/response-error-input.xml");
		GetAllTimeTypesResponseTransformer transformer = new GetAllTimeTypesResponseTransformer();

		try {
			transformer.pojoTransform(input, "UTF-8");
		} catch (Exception e) {
			assertEquals("resultCode: 3001 resultText: Illegal argument!", e.getMessage());
			return;
		}

		fail("Expected RuntimeException!");
	}

}