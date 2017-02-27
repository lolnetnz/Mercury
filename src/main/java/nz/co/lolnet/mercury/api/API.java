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
package nz.co.lolnet.mercury.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.PathParam;
import org.json.simple.JSONObject;

@Path("/")
public class API {

    List<APIRequest> APIRequest = new ArrayList<>();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet() {
        return Response.status(Status.OK).entity(getMercuryInformation()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doPost() {
        return Response.status(Status.OK).entity(getMercuryInformation()).build();
    }

    private String getMercuryInformation() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Application-Title", "Mercury");
        jsonObject.addProperty("Application-Version", "0.0.1-ALPHA");
        jsonObject.addProperty("Application-Author", "lolnet.co.nz");
        return new Gson().toJson(jsonObject);
    }

    @GET
    @Path("{request}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet(@PathParam("request") String request) {
        setupAPIRequest();
        Object output = null;
        for (APIRequest aPIRequest : APIRequest) {
            if (aPIRequest.getRequestName().equalsIgnoreCase(request))
            {
                output = aPIRequest.run();
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("request", request);
        jsonObject.put("output", output);
        return Response.status(Status.OK).entity(new Gson().toJson(jsonObject)).build();
    }

    private void setupAPIRequest() {
        APIRequest.add(new nz.co.lolnet.mercury.api.example.Example());
    }
}
