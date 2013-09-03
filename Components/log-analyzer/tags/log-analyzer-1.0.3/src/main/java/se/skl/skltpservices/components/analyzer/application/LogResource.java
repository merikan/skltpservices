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
package se.skl.skltpservices.components.analyzer.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import se.skl.skltpservices.components.analyzer.LogServiceConfig;
import se.skl.skltpservices.components.analyzer.domain.EventSummary;


@Controller
@Path("/log")
@Produces({"application/json; charset=UTF-8"})
public class LogResource {
    @Autowired
    private LogServiceConfig logServiceConfig;

	@GET
	@Path("/timeline/{contract}/{error}/{sender}/{receiver}/{time}")
	public List<EventSummary> timeline(@PathParam("contract") String contract,
			@PathParam("error") String error,
			@PathParam("sender") String sender,
			@PathParam("receiver") String receiver,
			@PathParam("time") long time) {
		return logServiceConfig.getLogStoreRepository().getTimeLine(contract, error, sender, receiver, time);
	}
	
	@GET
	@Path("/event/{id}")
	public Map<String, String> event(@PathParam("id") String id) {
		return logServiceConfig.getLogStoreRepository().getEventProperties(id);
	}
	
	@GET
	@Path("/metadata")
	public Map<String, Collection<String>> metadata() {
		Map<String, Collection<String>> map = new HashMap<String, Collection<String>>();
		map.put("senders", senders());
		map.put("receivers", receivers());
		map.put("contracts", contracts());
		return map;
	}
	
	@GET
	@Path("/contracts")
	public Collection<String> contracts() {
		return logServiceConfig.getLogStoreRepository().getContracts();		
	}

	@GET
	@Path("/senders")
	public Collection<String> senders() {
		return logServiceConfig.getLogStoreRepository().getSenders();		
	}

	@GET
	@Path("/receivers")
	public Collection<String> receivers() {
		return logServiceConfig.getLogStoreRepository().getReceivers();		
	}

}
