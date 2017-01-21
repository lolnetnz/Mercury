package nz.co.lolnet.mercury.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.util.ConsoleOutput;

public class Configuration {
	
	public final File mercuryFile = new File("/var/lib/jetty9/mercury");
	public final File configFile = new File(mercuryFile.getAbsolutePath() + "/config.json");
	
	public JsonObject lolcon;
	public JsonObject forum;
	
	public void loadConfig() {
		try {
			if (!mercuryFile.exists()) {
				mercuryFile.mkdir();
			}
			
			if (!configFile.exists()) {
				configFile.createNewFile();
				Files.copy(new FileInputStream(Mercury.getInstance().getServletContext().getRealPath("/WEB-INF/config.json")), Paths.get(configFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
				ConsoleOutput.info("Successfully created configuration file.");
			}
			
			JsonObject jsonObject = new JsonParser().parse(new String(Files.readAllBytes(Paths.get(configFile.getAbsolutePath())), StandardCharsets.UTF_8)).getAsJsonObject();
			
			lolcon = jsonObject.get("lolcon").getAsJsonObject();
			forum = jsonObject.get("forum").getAsJsonObject();
			
			ConsoleOutput.info("Successfully loaded configuration file.");
		} catch (IllegalStateException | InvalidPathException | IOException | JsonParseException | NullPointerException | OutOfMemoryError | SecurityException | UnsupportedOperationException ex) {
			ConsoleOutput.error("Exception loading configuration file!");
			ex.printStackTrace();
		}
		return;
	}
}