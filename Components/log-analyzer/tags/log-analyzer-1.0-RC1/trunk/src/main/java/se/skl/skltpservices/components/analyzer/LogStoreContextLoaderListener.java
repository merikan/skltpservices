package se.skl.skltpservices.components.analyzer;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import se.skl.skltpservices.components.log.services.LogStoreService;

public class LogStoreContextLoaderListener extends ContextLoaderListener {
	private static final Logger log = LoggerFactory.getLogger(LogStoreContextLoaderListener.class);

	private LogStoreService logStoreService;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce);
		this.logStoreService = (LogStoreService) WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean("logStoreService");		
		log.info("Starts LogStore service");
		try {
			logStoreService.start();
		} catch (Exception e) {
			log.error("Unable to start LogStore service", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		super.contextDestroyed(sce);
		log.info("Stops LogStore service");
		logStoreService.stop();
	}
}
