package nz.co.lolnet.mercury.auth.user;

import java.util.List;

public class UserData {
	
	private String type;
	private String name;
	private String token;
	private List<String> applications;
	private List<String> permissions;
	
	public String getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public List<String> getApplications() {
		return this.applications;
	}
	
	public List<String> getPermissions() {
		return this.permissions;
	}
}