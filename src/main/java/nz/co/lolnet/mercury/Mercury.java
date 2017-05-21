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

package nz.co.lolnet.mercury;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import nz.co.lolnet.mercury.config.Configuration;
import nz.co.lolnet.mercury.entries.Config;
import nz.co.lolnet.mercury.util.LogHelper;

@WebListener
public class Mercury implements ServletContextListener {
	
	private static Mercury instance;
	private ServletContext servletContext;
	private Configuration configuration;
	
	public Mercury() {
		instance = this;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		Thread.currentThread().setName("Mercury");
		servletContext = event.getServletContext();
		configuration = new Configuration();
		configuration.loadConfig();
		LogHelper.info("Mercury initialized.");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		configuration = null;
		servletContext = null;
		instance = null;
		LogHelper.info("Mercury destroyed!");
	}
	
	public static Mercury getInstance() {
		return instance;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	private Configuration getConfiguration() {
		return configuration;
	}
	
	public Config getConfig() {
		return getConfiguration().getConfig();
	}
}