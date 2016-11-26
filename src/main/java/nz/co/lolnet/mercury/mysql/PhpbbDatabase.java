package nz.co.lolnet.mercury.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.util.ConsoleOutput;

public class PhpbbDatabase {
	
	private final String host = Mercury.getInstance().getConfig().phpbb.get("host").getAsString();
	private final int port = Mercury.getInstance().getConfig().phpbb.get("port").getAsInt();
	private final String database = Mercury.getInstance().getConfig().phpbb.get("database").getAsString();
	private final String username = Mercury.getInstance().getConfig().phpbb.get("username").getAsString();
	private final String password = Mercury.getInstance().getConfig().phpbb.get("password").getAsString();
	
	public Connection getPhpbbConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		} catch (ClassNotFoundException | LinkageError | SQLException ex) {
			ConsoleOutput.error("Encountered an error processing 'getPhpbbConnection' - Exception!");
			ex.printStackTrace();
		}
		return null;
	}
}