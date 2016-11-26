package nz.co.lolnet.mercury.util;

import com.google.gson.JsonObject;

public class Response {
	
	public JsonObject info(String info, String infoMessage) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("info", info);
		jsonObject.addProperty("infoMessage", infoMessage);
		return jsonObject;
	}
	
	public JsonObject error(String error, String errorMessage) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("error", error);
		jsonObject.addProperty("errorMessage", errorMessage);
		return jsonObject;
	}
}