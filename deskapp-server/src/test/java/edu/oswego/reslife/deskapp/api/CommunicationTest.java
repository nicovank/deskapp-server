package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Message;
import org.junit.jupiter.api.Test;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

public class CommunicationTest {
	@Test
	void connectionTest() {
		try {
			Message[] messages = Communication.listMessages(0, "MACKIN");
		} catch (CommunicationsException e) {
			fail("The database cannot be reached.");
		} catch (IOException e) {
			fail("The disk cannot be read from properly.");
		} catch (SQLException e) {
			fail("The SQL query is not valid.");
		} catch (ClassNotFoundException e) {
			fail("The JBDC driver could not be found.");
		}
	}
}
