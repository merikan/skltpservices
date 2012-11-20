/**
 * Copyright (c) 2012, Sjukvardsradgivningen. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
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
