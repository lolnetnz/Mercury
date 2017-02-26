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

package nz.co.lolnet.mercury.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.util.ConsoleOutput;

public class Configuration {
	
	private final File mercuryDirectory;
	private final File configFile;
	private Config config;
	
	public Configuration() {
		mercuryDirectory = new File("/var/lib/jetty9/mercury");
		configFile = new File(getMercuryDirectory().getAbsolutePath() + "/config.json");
	}
	
	public void loadConfig() {
		try {
			if (getMercuryDirectory() != null && !getMercuryDirectory().exists()) {
				getMercuryDirectory().mkdir();
			}
			
			if (getConfigFile() != null && !getConfigFile().exists()) {
				getConfigFile().createNewFile();
				Files.copy(new FileInputStream(Mercury.getInstance().getServletContext().getRealPath("/WEB-INF/config.json")), Paths.get(getConfigFile().getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
				ConsoleOutput.info("Successfully created configuration file.");
			}
			
			JsonObject jsonObject = new JsonParser().parse(new String(Files.readAllBytes(Paths.get(getConfigFile().getAbsolutePath())), StandardCharsets.UTF_8)).getAsJsonObject();
			config = new Gson().fromJson(jsonObject, Config.class);
			
			ConsoleOutput.info("Successfully loaded configuration file.");
		} catch (IOException | OutOfMemoryError | RuntimeException ex) {
			ConsoleOutput.error("Exception loading configuration file!");
			ex.printStackTrace();
		}
	}
	
	private File getMercuryDirectory() {
		return mercuryDirectory;
	}
	
	private File getConfigFile() {
		return configFile;
	}
	
	public Config getConfig() {
		return config;
	}
}