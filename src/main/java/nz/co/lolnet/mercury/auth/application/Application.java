package nz.co.lolnet.mercury.auth.application;

import com.google.gson.JsonObject;

import nz.co.lolnet.mercury.util.IOUtil;
import nz.co.lolnet.mercury.util.JsonResponse;

public class Application {
	
	private final ApplicationData applicationData;
	private JsonObject error;
	
	public Application(String uuid) {
		this.applicationData = new IOUtil(uuid).getApplicationData();
	}
	
	public boolean checkCredentials(String token) {
		if (applicationData == null) {
			this.error = new JsonResponse().error("Not Found", "The server was unable to find a application matching the supplied uuid.");
			return false;
		}
		
		if (applicationData.getType() == null || !applicationData.getType().equals("application") || applicationData.getToken() == null) {
			this.error = new JsonResponse().error("InternalServerError", "The resource obtained was not of a application.");
			return false;
		}
		
		if (applicationData.getToken().equals(token)) {
			return true;
		}
		this.error = new JsonResponse().error("InternalServerError", "The server was unable to verify the applications credentials.");
		return false;
	}
	
	public boolean checkPermissions(String... permissions) {
		if (applicationData == null) {
			this.error = new JsonResponse().error("User Not Found", "The server was unable to find a user matching the supplied uuid.");
			return false;
		}
		
		if (applicationData.getType() == null || !applicationData.getType().equals("application") || applicationData.getPermissions() == null) {
			this.error = new JsonResponse().error("InternalServerError", "The resource obtained was not of a user.");
			return false;
		}
		
		if (permissions != null) {
			for (String permission : permissions) {
				if (!applicationData.getPermissions().contains(permission)) {
					this.error = new JsonResponse().error("Forbidden", "The application doesn't not have the necessary permissions to complete this request.");
					return false;
				}
			}
			return true;
		}
		this.error = new JsonResponse().error("InternalServerError", "The server was unable to verify the applications permissions.");
		return false;
	}
	
	public JsonObject getError() {
		if (this.error != null) {
			return this.error;
		}
		return new JsonResponse().error("InternalServerError", "No error message provided!");
	}
	
	public String getOwnerUUID() {
		if (applicationData != null) {
			return this.applicationData.getOwnerUUID();
		}
		return null;
	}
}