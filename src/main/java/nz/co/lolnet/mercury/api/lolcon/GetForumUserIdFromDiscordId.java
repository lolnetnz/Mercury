/*
 * Copyright 2017 lolnet.co.nz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.authentication.Authentication;
import nz.co.lolnet.mercury.entries.Data;
import nz.co.lolnet.mercury.mysql.MySQL;
import nz.co.lolnet.mercury.util.ConsoleOutput;
import nz.co.lolnet.mercury.util.JsonResponse;

@Path("/lolcon/getforumuseridfromdiscordid")
public class GetForumUserIdFromDiscordId {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet() {
		return Response.status(Status.BAD_REQUEST).entity(JsonResponse.error("Bad Request", "Bad request")).build();
	}
	
	@GET
	@Path("{request}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@PathParam("request") String request) {
		Authentication authentication = new Authentication();
		Response response = authentication.checkAuthentication(request, "LolCon.getForumUserIdFromDiscordId");
		
		if (response.getStatus() != Status.ACCEPTED.getStatusCode()) {
			return response;
		}
		
		Data data = authentication.getData();
		if (!response.hasEntity() || !(response.getEntity() instanceof JsonObject)) {
			data.setMessage(authentication.doEncrypt(JsonResponse.error("Internal Server Error", "Invalid response")));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(data)).build();
		}
		
		JsonObject jsonObject = (JsonObject) response.getEntity();
		if (!jsonObject.has("discordId")) {
			data.setMessage(authentication.doEncrypt(JsonResponse.error("Bad Request", "Request is missing 'discordId'")));
			return Response.status(Status.BAD_REQUEST).entity(new Gson().toJson(data)).build();
		}
		
		String discordId = jsonObject.get("discordId").getAsString();
		
		try {
			Connection connection = new MySQL(Mercury.getInstance().getConfig().getDatabases().get("forum")).getMySQLConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT `user_id` FROM `xenforo.xf_user_field_value` WHERE `field_id`='discordid' AND `field_value`=? LIMIT 0 , 1");
			preparedStatement.setString(1, discordId);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			jsonObject = new JsonObject();
			jsonObject.addProperty("discordId", discordId);
			
			if (resultSet.getRow() != 0) {
				jsonObject.addProperty("forumId", resultSet.getString("user_id"));
			} else {
				jsonObject.addProperty("forumId", 0);
			}
			
			data.setMessage(authentication.doEncrypt(new Gson().toJson(jsonObject)));
			return Response.status(Status.OK).entity(new Gson().toJson(data)).build();
		} catch (SQLException ex) {
			ConsoleOutput.error("Encountered an error processing 'getForumUserIdFromDiscordId " + discordId + "' - SQLException");
			ex.printStackTrace();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonResponse.error("InternalServerError", "Unable to process request.")).build();
	}
}