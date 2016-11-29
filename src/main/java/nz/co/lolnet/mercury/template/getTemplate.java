package nz.co.lolnet.mercury.template;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import nz.co.lolnet.mercury.auth.Authentication;
import nz.co.lolnet.mercury.util.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by James on 29/11/2016.
 */
@Path("/template/gettemplate")
public class getTemplate {

    private final String permissionRequired = "template.getTemplate";


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTemplate() {
        return new Gson().toJson(new Response().error("Bad Request", "Bad request"));
    }

    @GET
    @Path("{ID}/{ID2}/{ID3}/{applicationUUID}/{applicationToken}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getForumUserIdFromDiscordId(@PathParam("ID") String ID1, @PathParam("ID2") String ID2, @PathParam("ID3") String myIDthree
            ,@PathParam("applicationUUID") String applicationUUID, @PathParam("applicationToken") String applicationToken) {

        //The Authentication bits
        Authentication authentication = new Authentication();
        authentication.authenticateApplication(applicationUUID, applicationToken, permissionRequired);
        if (!authentication.isauthenticated()) {
            return authentication.getResult();
        }


        //The important bits
        String output = ID1 + " " + ID2 + " " + myIDthree;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("myOutput", output);



        return new Gson().toJson(jsonObject);
    }
}
