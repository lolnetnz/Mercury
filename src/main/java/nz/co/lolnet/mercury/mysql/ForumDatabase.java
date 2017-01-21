package nz.co.lolnet.mercury.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.util.ConsoleOutput;

public class ForumDatabase {
	
	private final String host = Mercury.getInstance().getConfig().forum.get("host").getAsString();
	private final int port = Mercury.getInstance().getConfig().forum.get("port").getAsInt();
	private final String database = Mercury.getInstance().getConfig().forum.get("database").getAsString();
	private final String username = Mercury.getInstance().getConfig().forum.get("username").getAsString();
	private final String password = Mercury.getInstance().getConfig().forum.get("password").getAsString();
	
	public Connection getForumConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		} catch (ClassNotFoundException | LinkageError | SQLException ex) {
			ConsoleOutput.error("Encountered an error processing 'getForumConnection' - Exception!");
			ex.printStackTrace();
		}
		return null;
	}
}