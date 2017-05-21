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

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.authentication.Authentication;
import nz.co.lolnet.mercury.entries.Data;
import nz.co.lolnet.mercury.entries.Databases;
import nz.co.lolnet.mercury.entries.IEndpoint;
import nz.co.lolnet.mercury.mysql.MySQL;
import nz.co.lolnet.mercury.util.JsonResponse;
import nz.co.lolnet.mercury.util.LogHelper;

@Path("/lolcon/createsign")
public class CreateSign implements IEndpoint {
	
	@Override
	public Response doGet() {
		return Response.status(Status.METHOD_NOT_ALLOWED).entity(JsonResponse.error("Method not allowed", "Method not allowed")).build();
	}
	
	@Override
	public Response doPost() {
		return Response.status(Status.BAD_REQUEST).entity(JsonResponse.error("Bad Request", "Bad request")).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String request) {
		Authentication authentication = new Authentication();
		Response response = authentication.checkAuthentication(request, getPermission());
		
		if (response.getStatus() != Status.ACCEPTED.getStatusCode()) {
			return response;
		}
		
		Data data = authentication.getData();
		if (!response.hasEntity() || !(response.getEntity() instanceof JsonObject)) {
			data.setMessage(authentication.doEncrypt(JsonResponse.error("Internal Server Error", "Invalid response")));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(data)).build();
		}
		
		JsonObject jsonObject = (JsonObject) response.getEntity();
		if (!jsonObject.has("playerName") || !jsonObject.has("location") || !jsonObject.has("details")) {
			data.setMessage(authentication.doEncrypt(JsonResponse.error("Bad Request", "Request is missing required arguments")));
			return Response.status(Status.BAD_REQUEST).entity(new Gson().toJson(data)).build();
		}
		
		String playerName = jsonObject.get("playerName").getAsString();
		String location = jsonObject.get("location").getAsString();
		String details = jsonObject.get("details").getAsString();
		
		try (MySQL mysql = new MySQL(Mercury.getInstance().getConfig().getDatabases().get(Databases.LOLCON))) {
			mysql.createConnection();
			mysql.setPreparedStatement(mysql.getConnection().prepareStatement("INSERT INTO `CommandSignDatabase`(`Location`, `Line3`, `Creator`) VALUES (?,?,?);"));
			mysql.getPreparedStatement().setString(1, location);
			mysql.getPreparedStatement().setString(2, details);
			mysql.getPreparedStatement().setString(3, playerName);
			mysql.getPreparedStatement().executeUpdate();
			
			jsonObject = new JsonObject();
			jsonObject.addProperty("status", true);
			data.setMessage(authentication.doEncrypt(new Gson().toJson(jsonObject)));
			jsonObject = null;
			
			return Response.status(Status.OK).entity(new Gson().toJson(data)).build();
		} catch (SQLException ex) {
			LogHelper.error("Encountered an error processing in '" + getClass().getSimpleName() + "' - " + ex.getMessage());
			ex.printStackTrace();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonResponse.error("Internal Server Error", "Unable to process request.")).build();
	}
	
	@Override
	public String getPermission() {
		return "LolCon.CreateSign";
	}
}