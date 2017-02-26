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

package nz.co.lolnet.mercury.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonResponse {
	
	public static String info(String info, String infoMessage) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("info", info);
		jsonObject.addProperty("infoMessage", infoMessage);
		return new Gson().toJson(jsonObject);
	}
	
	public static String error(String error, String errorMessage) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("error", error);
		jsonObject.addProperty("errorMessage", errorMessage);
		return new Gson().toJson(jsonObject);
	}
}