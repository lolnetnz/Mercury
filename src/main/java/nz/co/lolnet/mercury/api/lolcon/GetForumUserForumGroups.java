package nz.co.lolnet.mercury.api.lolcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author James
 */
public class GetForumUserForumGroups {

    @Path("/lolcon/getforumuserforumgroups")
    public class GetPlayerBalance {

        private final String permission = "LolCon.getforumuserforumgroups";

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response doGet() {
            return Response.status(Response.Status.BAD_REQUEST).entity(JsonResponse.error("Bad Request", "Bad request")).build();
        }

        @GET
        @Path("{request}/")
        @Produces(MediaType.APPLICATION_JSON)
        public Response doGet(@PathParam("request") String request) {

            String forumGroups = "";
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
            if (!jsonObject.has("userforumid")) {
                data.setMessage(authentication.doEncrypt(JsonResponse.error("Bad Request", "Request is missing 'userforumid'")));
                return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(data)).build();
            }

            int userForumID = jsonObject.get("userForumID").getAsInt();

            MySQL mysql = new MySQL(Mercury.getInstance().getConfig().getDatabases().get("forum"));
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                connection = mysql.getMySQLConnection();
                if (connection == null) {
                    throw new SQLException("Connection is null!");
                }

                preparedStatement = connection.prepareStatement("SELECT user_group_id FROM xenforo.xf_user_group_relation WHERE `user_id`=?");
                preparedStatement.setInt(1, userForumID);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {

                    int groupID = resultSet.getInt("user_group_id");
                    if (groupID > 0) {
                        forumGroups += groupID + "~";
                    }
                }

                jsonObject = new JsonObject();
                jsonObject.addProperty("userForumID", userForumID);

                jsonObject.addProperty("forumGroups", forumGroups);

                data.setMessage(authentication.doEncrypt(new Gson().toJson(jsonObject)));
                jsonObject = null;

                return Response.status(Response.Status.OK).entity(new Gson().toJson(data)).build();
            } catch (SQLException ex) {
                ConsoleOutput.error("Encountered an error processing 'getforumuserforumgroups " + userForumID + "' - SQLException");
                ex.printStackTrace();
            } finally {
                mysql.closeMySQL(connection, preparedStatement, resultSet);
                mysql = null;
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(JsonResponse.error("InternalServerError", "Unable to process request.")).build();
        }
    }
}
