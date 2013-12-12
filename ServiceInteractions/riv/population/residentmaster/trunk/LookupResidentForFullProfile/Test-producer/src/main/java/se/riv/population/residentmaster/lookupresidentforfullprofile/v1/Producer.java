package se.riv.population.residentmaster.lookupresidentforfullprofile.v1;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;

import javax.xml.ws.Endpoint;
import java.net.URL;

public class Producer implements Runnable {

    protected Producer(String address) throws Exception {
        System.out.println("Starting LookupResidentForFullProfile testproducer");

        // Loads a cxf configuration file to use
        final SpringBusFactory bf = new SpringBusFactory();
        final URL busFile = this.getClass().getClassLoader().getResource("cxf.xml");
        final Bus bus = bf.createBus(busFile.toString());

        SpringBusFactory.setDefaultBus(bus);
        final Object implementor = new LookupResidentForFullProfileImpl();
        Endpoint.publish(address, implementor);
    }

    @Override
    public void run() {
        System.out.println("LookupResidentForFullProfile testproducer ready...");
    }

    public static void main(String[] args) throws Exception {
        (new Thread(new Producer("http://localhost:22000/testproducer/LookupResidentForFullProfile/1/rivtabp21"))).start();
    }

}
