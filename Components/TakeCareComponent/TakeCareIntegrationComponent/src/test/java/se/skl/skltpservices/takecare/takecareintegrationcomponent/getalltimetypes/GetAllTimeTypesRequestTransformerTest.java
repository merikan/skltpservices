package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.soitoolkit.commons.mule.util.MiscUtil;

import se.skl.skltpservices.takecare.booking.GetTimeTypes;
import se.skl.skltpservices.takecare.booking.gettimetypesrequest.ProfdocHISMessage;

public class GetAllTimeTypesRequestTransformerTest {
	
	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(GetTimeTypes.class);

	@Test
	public void testTransformer_ok() throws Exception {
		String input = MiscUtil.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/request-input.xml");

		String expectedResult = MiscUtil
				.readFileAsString("src/test/resources/testfiles/GetAllTimeTypes/request-output.xml");

		GetAllTimeTypesRequestTransformer transformer = new GetAllTimeTypesRequestTransformer();
		String result = (String) transformer.pojoTransform(input, "UTF-8");
		
		assertEquals(expectedResult, result);
	}
}