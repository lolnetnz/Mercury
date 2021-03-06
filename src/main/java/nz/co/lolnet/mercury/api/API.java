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

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.entries.IEndpoint;
import nz.co.lolnet.mercury.util.Reference;

@Path("/")
public class API implements IEndpoint {
	
	@Override
	public Response doGet() {
		return Response.status(Status.OK).entity(getMercuryInformation()).build();
	}
	
	@Override
	public Response doPost() {
		return Response.status(Status.OK).entity(getMercuryInformation()).build();
	}
	
	private String getMercuryInformation() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("App-Id", Reference.APP_ID);
		jsonObject.addProperty("App-Name", Reference.APP_NAME);
		jsonObject.addProperty("App-Version", Reference.APP_VERSION);
		jsonObject.addProperty("Authors", String.join(", ", Reference.AUTHORS));
		jsonObject.addProperty("Source", Reference.SOURCE);
		jsonObject.addProperty("Website", Reference.WEBSITE);
		return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
	}
	
	@Override
	public List<String> getPermissions() {
		return null;
	}
}