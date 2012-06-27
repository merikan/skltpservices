package se.skl.skltpservices.components.analyzer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.evactor.EvactorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class EvactorContextLoaderListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(EvactorContextLoaderListener.class);

    private ActorSystem system;
    private ActorRef apiServer;
    private ActorRef context;
    private String name = "evactor";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Config akkaConf = ConfigFactory.load();
        system = ActorSystem.create(name, akkaConf);

        log.info("Starting up actor system: {}", system.name());
        context = system.actorOf(new Props(EvactorContext.class), "evactor");

//        Props apiProps = new Props(new UntypedActorFactory() {
//            public Actor create() {
//                return new ApiServer(system, 8081);
//            }
//        });

//        apiServer = system.actorOf(apiProps, "apiServer");
//        apiServer.tell("startup");
        log.info("Succefully started actor system: {}", system.name());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        apiServer.tell("shutdown");
        context.tell("shutdown");
        system.shutdown();
        log.info("Shut down actor system: {}", system.name());
    }

}
