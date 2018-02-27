package com.oswego.reslife.deskapp.services;

import com.oswego.reslife.deskapp.sql.SQLQueryManager;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Service("communication")
@Path("communication")
public class Communication {

	@Path("list")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String listMessages(String body) {
		try {
			SQLQueryManager manager = new SQLQueryManager("sql");
			System.out.println("'" + manager.getSQLQuery("init.Buildings") + "'");
			return manager.getSQLQuery("init.Buildings");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error: " + body;
	}
}
