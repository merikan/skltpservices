package se.skltp.adapterservices.druglogistics.dosdispensing;

import static org.junit.Assert.*;

import org.junit.Test;
import org.soitoolkit.commons.mule.util.MiscUtil;

public class ApseRetryComponentTest {

	@Test
	public void testOk() throws Exception {
	        String inputFile = "src/test/resources/testfiles/hamtaMeddelande/request-input.xml";
			String input     = MiscUtil.readFileAsString(inputFile);
			String response  = new ApseRetryComponent().performPost(input);
	}
}
