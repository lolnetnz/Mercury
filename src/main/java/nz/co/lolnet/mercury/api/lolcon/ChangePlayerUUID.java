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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.authentication.Authentication;
import nz.co.lolnet.mercury.entries.Data;
import nz.co.lolnet.mercury.mysql.MySQL;
import nz.co.lolnet.mercury.util.ConsoleOutput;
import nz.co.lolnet.mercury.util.JsonResponse;

@Path("/lolcon/changeplayeruuid")
public class ChangePlayerUUID {
    private final String permission = "LolCon.changeplayeruuid";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet() {
        return Response.status(Response.Status.BAD_REQUEST).entity(JsonResponse.error("Bad Request", "Bad request")).build();
    }

    @GET
    @Path("{request}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet(@PathParam("request") String request) {

        Authentication authentication = new Authentication();
        Response response = authentication.checkAuthentication(request, permission);

        if (response.getStatus() != Response.Status.ACCEPTED.getStatusCode()) {
            return response;
        }

        Data data = authentication.getData();
        if (!response.hasEntity() || !(response.getEntity() instanceof JsonObject)) {
            data.setMessage(authentication.doEncrypt(JsonResponse.error("Internal Server Error", "Invalid response")));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(data)).build();
        }
        JsonObject jsonObject = (JsonObject) response.getEntity();
        if (!jsonObject.has("playerName")) {
            data.setMessage(authentication.doEncrypt(JsonResponse.error("Bad Request", "Request is missing 'playerName'")));
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(data)).build();
        }
        if (!jsonObject.has("playerUUID")) {
            data.setMessage(authentication.doEncrypt(JsonResponse.error("Bad Request", "Request is missing 'playerUUID'")));
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(data)).build();
        }

        String playerName = jsonObject.get("playerName").getAsString();
        String playerUUID = jsonObject.get("playerUUID").getAsString();

        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            conn = new MySQL(Mercury.getInstance().getConfig().getDatabases().get("lolcon")).getMySQLConnection();
            ps = conn.prepareStatement("UPDATE `player` SET `playeruuid` = ? WHERE `playerName`=?;");
            ps.setString(1, playerUUID);
            ps.setString(2, playerName);
            ps.executeUpdate();
            success = true;

        } catch (SQLException ex) {
            ConsoleOutput.error("Encountered an error processing 'ChangePlayerUUID " + playerName + "," + playerUUID + "' - SQLException");
            ex.printStackTrace();
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }

        }
        if (success) {
            jsonObject = new JsonObject();
            jsonObject.addProperty("success", success);
            data.setMessage(authentication.doEncrypt(new Gson().toJson(jsonObject)));
            return Response.status(Response.Status.OK).entity(new Gson().toJson(data)).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(JsonResponse.error("InternalServerError",
                    "Unable to process request.")).build();
        }

    }
}
