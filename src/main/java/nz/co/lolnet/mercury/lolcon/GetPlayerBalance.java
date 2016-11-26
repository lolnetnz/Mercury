package nz.co.lolnet.mercury.lolcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.auth.Authentication;
import nz.co.lolnet.mercury.mysql.LolConDatabase;
import nz.co.lolnet.mercury.util.ConsoleOutput;
import nz.co.lolnet.mercury.util.Response;

@Path("/lolcon/getplayerbalance")
public class GetPlayerBalance {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPlayerBalance() {
		return new Gson().toJson(new Response().error("Bad Request", "Bad request"));
	}
	
	@GET
	@Path("{playerName}/{applicationUUID}/{applicationToken}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPlayerBalance(@PathParam("playerName") String playerName, @PathParam("applicationUUID") String applicationUUID, @PathParam("applicationToken") String applicationToken) {
		JsonObject jsonObject;
		
		Authentication authentication = new Authentication();
		jsonObject = authentication.authenticateApplication(applicationUUID, applicationToken, "LolCon.GetPlayerBalance");
		if (!jsonObject.has("info") || !jsonObject.get("info").getAsString().equalsIgnoreCase("Accepted")) {
			return new Gson().toJson(jsonObject);
		}
		
		try {
			Connection connection = new LolConDatabase().getLolConConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT `lolcoins` FROM `player` WHERE `playerName`=? LIMIT 0 , 1");
			preparedStatement.setString(1, playerName);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			jsonObject = new JsonObject();
			jsonObject.addProperty("playerName", playerName);
			
			if (resultSet.getRow() != 0) {
				jsonObject.addProperty("playerBalance", resultSet.getInt("lolcoins"));
			} else {
				jsonObject.addProperty("playerBalance", -1);
			}
			
			return new Gson().toJson(jsonObject);
		} catch (SQLException ex) {
			ConsoleOutput.error("Encountered an error processing 'getPlayerBalance " + playerName + "' - SQLException");
			ex.printStackTrace();
		}
		return new Gson().toJson(new Response().error("InternalServerError", "Unable to process request."));
	}
}