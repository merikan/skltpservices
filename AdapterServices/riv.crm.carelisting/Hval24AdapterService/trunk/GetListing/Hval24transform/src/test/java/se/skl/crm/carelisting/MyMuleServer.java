package se.skl.crm.carelisting;

import org.mule.MuleServer;

public class MyMuleServer {
	public static void main(String[] args) {
		MuleServer server = new MuleServer("carelisting-unittest-config.xml");
		server.start(false, true);
	}
}