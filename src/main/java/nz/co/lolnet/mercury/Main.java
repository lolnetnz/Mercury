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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import nz.co.lolnet.mercury.util.LogHelper;
import nz.co.lolnet.mercury.util.Reference;

public class Main {
	
	public static void main(String[] args) {
		Thread.currentThread().setName(Reference.APP_NAME + "-Main");
		LogHelper.info(Reference.APP_NAME + " initializing...");
		
		try {
			Server server = new Server(80);
			WebAppContext webAppContext = new WebAppContext("src/main/webapp", "");
			webAppContext.addEventListener(new Mercury());
			server.setHandler(webAppContext);
			server.start();
		} catch (Exception ex) {
			LogHelper.error("Encountered an error processing 'main' - " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}