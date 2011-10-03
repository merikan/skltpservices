package se.skl.tp.ticket.transformer;

import groovy.util.GroovyTestSuite;
import junit.framework.TestSuite;

public class TransformerGroovyTestSuite extends TestSuite {

	private static final String TEST_ROOT = "src/test/groovy/se/skl/tp/ticket/transformer/";

	@SuppressWarnings("unchecked")
	public static TransformerGroovyTestSuite suite() throws Exception {
		TransformerGroovyTestSuite suite = new TransformerGroovyTestSuite();
		GroovyTestSuite gsuite = new GroovyTestSuite();
		suite.addTestSuite(gsuite.compile(TEST_ROOT + "TicketMachineTest.groovy"));
		suite.addTestSuite(gsuite.compile(TEST_ROOT + "ArgosHeaderHelperTest.groovy"));
		suite.addTestSuite(gsuite.compile(TEST_ROOT + "SamlTicketTransformerTest.groovy"));
		return suite;
	}
}
