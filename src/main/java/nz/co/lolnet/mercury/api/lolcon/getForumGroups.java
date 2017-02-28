/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet.mercury.api.lolcon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author James
 */
@Path("/lolcon/getforumgroup")
public class getForumGroups {
    private final String permission = "LolCon.getfourmgroup";

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
        JsonArray forumGroups = new JsonArray();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            conn = new MySQL(Mercury.getInstance().getConfig().getDatabases().get("lolcon")).getMySQLConnection();
            ps = conn.prepareStatement("SELECT `user_group_id`,`title` FROM xenforo.xf_user_group;");
            rs = ps.executeQuery();
            while (rs.next())
            {
                String groupName = rs.getString("user_group_id");
                if (groupName != null) forumGroups.add(groupName);
            }
            success = true;

        } catch (SQLException ex) {
            ConsoleOutput.error("Encountered an error processing 'changeplayername " + playerName + "," + playerUUID + "' - SQLException");
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
            data.setMessage(authentication.doEncrypt(new Gson().toJson(forumGroups)));
            return Response.status(Response.Status.OK).entity(new Gson().toJson(data)).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(JsonResponse.error("InternalServerError",
                    "Unable to process request.")).build();
        }

    }
}