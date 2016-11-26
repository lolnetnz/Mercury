package nz.co.lolnet.mercury.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.util.ConsoleOutput;

public class LolConDatabase {
	
	private final String host = Mercury.getInstance().getConfig().lolcon.get("host").getAsString();
	private final int port = Mercury.getInstance().getConfig().lolcon.get("port").getAsInt();
	private final String database = Mercury.getInstance().getConfig().lolcon.get("database").getAsString();
	private final String username = Mercury.getInstance().getConfig().lolcon.get("username").getAsString();
	private final String password = Mercury.getInstance().getConfig().lolcon.get("password").getAsString();
	
	public Connection getLolConConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		} catch (ClassNotFoundException | LinkageError | SQLException ex) {
			ConsoleOutput.error("Encountered an error processing 'getLolConConnection' - Exception!");
			ex.printStackTrace();
		}
		return null;
	}
}