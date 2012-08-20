package se.skl.skltpservices.pull.main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.AbstractTestCase;

public class EngagementIndexPullIntegrationTest extends AbstractTestCase {

	@BeforeClass
	public void beforeClass() {
		setDisposeManagerPerSuite(true);
		setTestTimeoutSecs(240);
	}

	@Before
	public void doSetUp() throws Exception {
		super.doSetUp();
		setDisposeManagerPerSuite(true);
	}

	@Override
	protected String getConfigResources() {
		return "teststubs-and-services-config.xml";
	}

	@Test
	public void testSomething() {
		Assert.assertTrue(true);
	}

}
