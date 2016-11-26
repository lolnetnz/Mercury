package nz.co.lolnet.mercury.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.auth.application.ApplicationData;
import nz.co.lolnet.mercury.auth.user.UserData;

public class IOUtil {
	
	private final String applicationData;
	private final String userData;
	
	public IOUtil(String fileName) {
		this.applicationData = Mercury.getInstance().getConfig().mercuryFile.getAbsolutePath() + "/application/" + fileName + ".json";
		this.userData = Mercury.getInstance().getConfig().mercuryFile.getAbsolutePath() + "/user/" + fileName + ".json";
	}
	
	public ApplicationData getApplicationData() {
		try {
			File applicationFile = new File(this.applicationData);
			if (applicationFile.exists()) {
				return new Gson().fromJson(new JsonParser().parse(new String(Files.readAllBytes(Paths.get(applicationFile.getAbsolutePath())), StandardCharsets.UTF_8)), ApplicationData.class);
			}
		} catch (InvalidPathException | IOException | JsonParseException | NullPointerException | OutOfMemoryError | SecurityException ex) {
			ConsoleOutput.error("Encountered an error processing 'getApplicationData' - Exception");
			ex.printStackTrace();
		}
		return null;
	}
	
	public UserData getUserData() {
		try {
			File userFile = new File(this.userData);
			if (userFile.exists()) {
				return new Gson().fromJson(new JsonParser().parse(new String(Files.readAllBytes(Paths.get(userFile.getAbsolutePath())), StandardCharsets.UTF_8)), UserData.class);
			}
		} catch (InvalidPathException | IOException | JsonParseException | NullPointerException | OutOfMemoryError | SecurityException ex) {
			ConsoleOutput.error("Encountered an error processing 'getUserData' - Exception");
			ex.printStackTrace();
		}
		return null;
	}
}