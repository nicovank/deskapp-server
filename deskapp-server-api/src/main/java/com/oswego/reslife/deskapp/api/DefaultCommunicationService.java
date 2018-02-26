package com.oswego.reslife.deskapp.api.impl;

import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Service("communication")
@Path("communication")
public class DefaultCommunicationService {

	@Path("list")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String listMessages(String body) {
		return body;
	}
}
