package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import static org.junit.Assert.assertEquals;

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

}