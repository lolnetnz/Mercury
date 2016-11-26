package nz.co.lolnet.mercury.auth;

import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.auth.application.Application;
import nz.co.lolnet.mercury.auth.user.User;
import nz.co.lolnet.mercury.util.Response;

public class Authentication {
	
	private Application application;
	private User user;
	
	public JsonObject authenticateApplication(String uuid, String token, String permission) {
		if (application == null) {
			application = new Application(uuid);
		}
		
		if (!application.checkCredentials(token)) {
			return application.getError();
		}
		
		if (!application.checkPermissions(permission)) {
			return application.getError();
		}
		
		if (user == null) {
			user = new User(application.getOwnerUUID());
		}
		
		if (!user.getApplications().contains(uuid)) {
			return user.getError();
		}
		
		if (user.checkPermissions(permission)) {
			return new Response().info("Accepted", "Successfully verified application");
		}
		return user.getError();
	}
	
	public JsonObject authenticateUser(String uuid, String token) {
		if (user == null) {
			user = new User(uuid);
		}
		
		if (user.checkCredentials(token)) {
			return new Response().info("Accepted", "Successfully verified user");
		}
		return user.getError();
	}
}