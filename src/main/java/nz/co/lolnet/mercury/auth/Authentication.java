package nz.co.lolnet.mercury.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.auth.application.Application;
import nz.co.lolnet.mercury.auth.user.User;
import nz.co.lolnet.mercury.util.Response;

public class Authentication {
	
	private Application application;
	private User user;
	private JsonObject result;
	
	public void authenticateApplication(String uuid, String token, String permission) {
		if (application == null) {
			application = new Application(uuid);
		}
		
		if (!application.checkCredentials(token)) {
			this.result = application.getError();
			return;
		}
		
		if (!application.checkPermissions(permission)) {
			this.application.getError();
			return;
		}
		
		if (user == null) {
			user = new User(application.getOwnerUUID());
		}
		
		if (!user.getApplications().contains(uuid)) {
			this.result = user.getError();
			return;
		}
		
		if (user.checkPermissions(permission)) {
			this.result = new Response().info("Accepted", "Successfully verified application");
			return;
		}
		this.result = user.getError();
	}
	
	public void authenticateUser(String uuid, String token) {
		if (user == null) {
			user = new User(uuid);
		}
		
		if (user.checkCredentials(token)) {
			this.result = new Response().info("Accepted", "Successfully verified user");
			return;
		}
		this.result = user.getError();
	}
	
	public boolean isAuthenticated() {
		if (result != null && result.has("info") && result.get("info").getAsString().equalsIgnoreCase("Accepted")) {
			return true;
		}
		return false;
	}
	
	public String getResult() {
		return new Gson().toJson(result);
	}
}