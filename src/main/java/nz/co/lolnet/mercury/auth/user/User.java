package nz.co.lolnet.mercury.auth.user;

import java.util.List;

import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.util.IOUtil;
import nz.co.lolnet.mercury.util.Response;

public class User {
	
	private final UserData userData;
	private JsonObject error;
	
	public User(String uuid) {
		this.userData = new IOUtil(uuid).getUserData();
	}
	
	public boolean checkCredentials(String token) {
		if (userData == null) {
			this.error = new Response().error("Not Found", "The server was unable to find a user matching the supplied uuid.");
			return false;
		}
		
		if (userData.getType() == null || !userData.getType().equals("user") || userData.getToken() == null) {
			this.error = new Response().error("InternalServerError", "The resource obtained was not of a user.");
			return false;
		}
		
		if (userData.getToken().equals(token)) {
			return true;
		}
		this.error = new Response().error("InternalServerError", "The server was unable to verify the users credentials.");
		return false;
	}
	
	public boolean checkPermissions(String... permissions) {
		if (userData == null) {
			this.error = new Response().error("Not Found", "The server was unable to find a user matching the supplied uuid.");
			return false;
		}
		
		if (userData.getType() == null || !userData.getType().equals("user") || userData.getPermissions() == null) {
			this.error = new Response().error("InternalServerError", "The resource obtained was not of a user.");
			return false;
		}
		
		if (userData.getPermissions().contains("*")) {
			return true;
		}
		
		if (permissions != null) {	
			for (String permission : permissions) {
				if (!userData.getPermissions().contains(permission)) {
					this.error = new Response().error("Forbidden", "The user doesn't not have the necessary permissions to complete this request.");
					return false;
				}
			}
			return true;
		}
		this.error = new Response().error("InternalServerError", "The server was unable to verify the users permissions.");
		return false;
	}
	
	public JsonObject getError() {
		if (this.error != null) {
			return this.error;
		}
		return new Response().error("InternalServerError", "No error message provided!");
	}
	
	public List<String> getApplications() {
		if (userData != null) {
			return this.userData.getApplications();
		}
		return null;
	}
}