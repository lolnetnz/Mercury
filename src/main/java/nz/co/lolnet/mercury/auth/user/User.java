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
			return setError(new Response().error("Not Found", "The server was unable to find a user matching the supplied uuid."));
		}
		
		if (userData.getType() == null || !userData.getType().equals("user") || userData.getToken() == null) {
			return setError(new Response().error("InternalServerError", "The resource obtained was not of a user."));
		}
		
		if (userData.getToken().equals(token)) {
			return true;
		}
		return setError(new Response().error("InternalServerError", "The server was unable to verify the users credentials."));
	}
	
	public boolean checkPermissions(String... permissions) {
		if (userData == null) {
			return setError(new Response().error("Not Found", "The server was unable to find a user matching the supplied uuid."));
		}
		
		if (userData.getType() == null || !userData.getType().equals("user") || userData.getPermissions() == null) {
			return setError(new Response().error("InternalServerError", "The resource obtained was not of a user."));
		}
		
		if (userData.getPermissions().contains("*")) {
			return true;
		}
		
		if (permissions != null) {	
			for (String permission : permissions) {
				if (!userData.getPermissions().contains(permission)) {
					return setError(new Response().error("Forbidden", "The user doesn't not have the necessary permissions to complete this request."));
				}
			}
			return true;
		}
		return setError(new Response().error("InternalServerError", "The server was unable to verify the users permissions."));
	}
	
	private boolean setError(JsonObject error) {
		this.error = error;
		return false;
	}
	
	public JsonObject getError() {
		return this.error;
	}
	
	public List<String> getApplications() {
		if (userData != null) {
			return this.userData.getApplications();
		}
		return null;
	}
}