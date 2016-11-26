package nz.co.lolnet.mercury.auth.application;

import java.util.List;

public class ApplicationData {
	
	private String type;
	private String name;
	private String token;
	private String owneruuid;
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
	
	public String getOwnerUUID() {
		return this.owneruuid;
	}
	
	public List<String> getPermissions() {
		return this.permissions;
	}
}