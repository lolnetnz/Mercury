package nz.co.lolnet.mercury;

import org.glassfish.jersey.servlet.ServletContainer;

import nz.co.lolnet.mercury.config.Configuration;

public class Mercury extends ServletContainer {
	
	private static final long serialVersionUID = 1L;
	private static Mercury instance;
	private Configuration configuration;
	
	public Mercury() {
		instance = this;
	}
	
	public static Mercury getInstance() {
		return instance;
	}
	
	public Configuration getConfig() {
		if (this.configuration == null) {
			this.configuration = new Configuration();
			this.configuration.loadConfig();
		}
		return this.configuration;
	}
}