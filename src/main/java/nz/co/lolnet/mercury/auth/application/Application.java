package nz.co.lolnet.mercury.auth.application;

import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.util.IOUtil;
import nz.co.lolnet.mercury.util.Response;

public class Application {
	
	private final ApplicationData applicationData;
	private JsonObject error;
	
	public Application(String uuid) {
		this.applicationData = new IOUtil(uuid).getApplicationData();
	}
	
	public boolean checkCredentials(String token) {
		if (applicationData == null) {
			return setError(new Response().error("Not Found", "The server was unable to find a application matching the supplied uuid."));
		}
		
		if (applicationData.getType() == null || !applicationData.getType().equals("application") || applicationData.getToken() == null) {
			return setError(new Response().error("InternalServerError", "The resource obtained was not of a application."));
		}
		
		if (applicationData.getToken().equals(token)) {
			return true;
		}
		return setError(new Response().error("InternalServerError", "The server was unable to verify the applications credentials."));
	}
	
	public boolean checkPermissions(String... permissions) {
		if (applicationData == null) {
			return setError(new Response().error("User Not Found", "The server was unable to find a user matching the supplied uuid."));
		}
		
		if (applicationData.getType() == null || !applicationData.getType().equals("application") || applicationData.getPermissions() == null) {
			return setError(new Response().error("InternalServerError", "The resource obtained was not of a user."));
		}
		
		if (permissions != null) {
			for (String permission : permissions) {
				if (!applicationData.getPermissions().contains(permission)) {
					return setError(new Response().error("Forbidden", "The application doesn't not have the necessary permissions to complete this request."));
				}
			}
			return true;
		}
		return setError(new Response().error("InternalServerError", "The server was unable to verify the applications permissions."));
	}
	
	private boolean setError(JsonObject error) {
		this.error = error;
		return false;
	}
	
	public JsonObject getError() {
		return this.error;
	}
	
	public String getOwnerUUID() {
		if (applicationData != null) {
			return this.applicationData.getOwnerUUID();
		}
		return null;
	}
}