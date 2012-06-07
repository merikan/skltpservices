package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.soitoolkit.commons.mule.util.MiscUtil;

import se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes.GetAllTimeTypesRequestTransformer;

public class GetAllTimeTypesRequestTransformerTest {

	@Test
	public void testTransformer_ok() throws Exception {

		// Specify input and expected result 
		String input          = MiscUtil.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/request-input.xml");

		String expectedResult = MiscUtil.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/request-input.xml"); // No transformation is done by default so use input also as expected...


		// Create the transformer under test and let it perform the transformation

		GetAllTimeTypesRequestTransformer transformer = new GetAllTimeTypesRequestTransformer();
		String result = (String)transformer.pojoTransform(input, "UTF-8");


		// Compare the result to the expected value
		assertEquals(expectedResult, result);
	}
}