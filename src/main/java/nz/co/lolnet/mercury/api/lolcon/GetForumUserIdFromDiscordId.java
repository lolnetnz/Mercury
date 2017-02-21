package nz.co.lolnet.mercury.api.lolcon;

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
import nz.co.lolnet.mercury.mysql.ForumDatabase;
import nz.co.lolnet.mercury.util.ConsoleOutput;
import nz.co.lolnet.mercury.util.JsonResponse;

@Path("/lolcon/getforumuseridfromdiscordid")
public class GetForumUserIdFromDiscordId {

	private final String permissionRequired = "LolCon.getForumUserIdFromDiscordId";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getForumUserIdFromDiscordId() {
		return new Gson().toJson(new JsonResponse().error("Bad Request", "Bad request"));
	}
	
	@GET
	@Path("{discordId}/{applicationUUID}/{applicationToken}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getForumUserIdFromDiscordId(@PathParam("discordId") String discordId, @PathParam("applicationUUID") String applicationUUID, @PathParam("applicationToken") String applicationToken) {
		Authentication authentication = new Authentication();
		authentication.authenticateApplication(applicationUUID, applicationToken, permissionRequired);
		if (!authentication.isAuthenticated()) {
			return authentication.getResult();
		}
		
		try {
			Connection connection = new ForumDatabase().getForumConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT `user_id` FROM `xenforo.xf_user_field_value` WHERE `field_id`='discordid' AND `field_value`=? LIMIT 0 , 1");
			preparedStatement.setString(1, discordId);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			JsonObject jsonObject = new JsonObject();
			
			if (resultSet.getRow() != 0) {
				jsonObject.addProperty("forumId", resultSet.getString("user_id"));
			} else {
				jsonObject.addProperty("forumId", 0);
			}
			
			return new Gson().toJson(jsonObject);
		} catch (SQLException ex) {
			ConsoleOutput.error("Encountered an error processing 'getForumUserIdFromDiscordId " + discordId + "' - SQLException");
			ex.printStackTrace();
		}
		return new Gson().toJson(new JsonResponse().error("InternalServerError", "Unable to process request."));
	}
}