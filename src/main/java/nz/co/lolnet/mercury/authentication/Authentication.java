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

package nz.co.lolnet.mercury.authentication;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.entries.Account;
import nz.co.lolnet.mercury.entries.Data;
import nz.co.lolnet.mercury.util.LogHelper;
import nz.co.lolnet.mercury.util.MercuryUtil;

public class Authentication {
	
	private String uniqueId;
	private Account account;
	
	public Response checkAuthentication(String request, List<String> permissions) {
		try {
			if (StringUtils.isBlank(request)) {
				return Response.status(Status.BAD_REQUEST).entity(MercuryUtil.createErrorResponse("Bad Request", "Blank request")).build();
			}
			
			JsonObject jsonObject = new JsonParser().parse(new String(Base64.getUrlDecoder().decode(request), StandardCharsets.UTF_8)).getAsJsonObject();
			Data data = new Gson().fromJson(jsonObject, Data.class);
			
			if (data == null) {
				return Response.status(Status.BAD_REQUEST).entity(MercuryUtil.createErrorResponse("Bad Request", "Invalid data")).build();
			}
			
			uniqueId = data.getUniqueId();
			account = Mercury.getInstance().getConfig().getAccounts().get(getUniqueId());
			
			if (getAccount() == null) {
				return Response.status(Status.BAD_REQUEST).entity(MercuryUtil.createErrorResponse("Bad Request", "Supplied UniqueId does not exist")).build();
			}
			
			if (StringUtils.isBlank(data.getMessage())) {
				return Response.status(Status.BAD_REQUEST).entity(MercuryUtil.createErrorResponse("Bad Request", "Request contained no data")).build();
			}
			
			jsonObject = new JsonParser().parse(doDecrypt(data.getMessage())).getAsJsonObject();
			if (jsonObject == null || !jsonObject.has("creationTime") || !isRequestValid(jsonObject.remove("creationTime").getAsLong())) {
				return Response.status(Status.FORBIDDEN).entity(MercuryUtil.createErrorResponse("Forbidden", "Request could not be validated")).build();
			}
			
			if (jsonObject.size() != 0) {
				return Response.status(Status.ACCEPTED).entity(jsonObject).build();
			}
			return Response.status(Status.ACCEPTED).build();
		} catch (RuntimeException ex) {
			LogHelper.error("Encountered an error processing 'checkAuthentication' in '" + getClass().getSimpleName() + "' - " + ex.getMessage());
			ex.printStackTrace();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(MercuryUtil.createErrorResponse("Internal Server Error", "An error occurred during authentication!")).build();
	}
	
	private boolean isRequestValid(long creationTime) {
		if (Mercury.getInstance() == null || Mercury.getInstance().getConfig() == null) {
			return false;
		}
		
		long requestValidityTime = Mercury.getInstance().getConfig().getRequestValidityTime();
		if (creationTime == 0 || requestValidityTime == 0) {
			return false;
		}
		
		if (creationTime >= (System.currentTimeMillis() - requestValidityTime) && creationTime <= (System.currentTimeMillis() + requestValidityTime)) {
			return true;
		}
		return false;
	}
	
	public boolean hasPermission(List<String> permissions) {
		if (getAccount() == null || permissions == null || permissions.isEmpty()) {
			return false;
		}
		
		if (getAccount().getPermissions().contains("*")) {
			return true;
		}
		
		for (String permission : permissions) {
			if (StringUtil.isNotBlank(permission) && getAccount().getPermissions().contains(permission)) {
				return true;
			}
		}
		return false;
	}
	
	public String doEncrypt(String decrypted) {
		if (getAccount() != null) {
			return doEncrypt(decrypted, getAccount().getToken());
		}
		return null;
	}
	
	public String doDecrypt(String encrypted) {
		if (getAccount() != null) {
			return doDecrypt(encrypted, getAccount().getToken());
		}
		return null;
	}
	
	private String doEncrypt(String decrypted, String password) {
		try {
			if (StringUtils.isAnyBlank(decrypted, password)) {
				return null;
			}
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			byte[] input = decrypted.getBytes(StandardCharsets.UTF_8);
			byte[] secret = getSecret(password.toCharArray());
			byte[] ivBytes = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
			
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, "AES"), new IvParameterSpec(ivBytes));
			byte[] encrypted = new byte[cipher.getOutputSize(input.length)];
			int enc_len = cipher.update(input, 0, input.length, encrypted, 0);
			enc_len += cipher.doFinal(encrypted, enc_len);
			
			byte[] byteArray = new byte[ivBytes.length + encrypted.length];
			System.arraycopy(ivBytes, 0, byteArray, 0, ivBytes.length);
			System.arraycopy(encrypted, 0, byteArray, ivBytes.length, encrypted.length);
			
			return Base64.getEncoder().encodeToString(byteArray);
		} catch (GeneralSecurityException | RuntimeException ex) {
			LogHelper.error("Encountered an error processing 'doEncrypt' in '" + getClass().getSimpleName() + "' - " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
	
	private String doDecrypt(String encrypted, String password) {
		try {
			if (StringUtils.isAnyBlank(encrypted, password)) {
				return null;
			}
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			byte[] input = Base64.getDecoder().decode(encrypted);
			byte[] secret = getSecret(password.toCharArray());
			byte[] ivBytes = new byte[16];
			
			byte[] byteArray = new byte[input.length - ivBytes.length];
			System.arraycopy(input, 0, ivBytes, 0, 16);
			System.arraycopy(input, 16, byteArray, 0, byteArray.length);
			
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, "AES"), new IvParameterSpec(ivBytes));
			byte[] decrypted = new byte[cipher.getOutputSize(byteArray.length)];
			int dec_len = cipher.update(byteArray, 0, byteArray.length, decrypted, 0);
			dec_len += cipher.doFinal(decrypted, dec_len);
			
			return new String(decrypted, StandardCharsets.UTF_8).trim();
		} catch (GeneralSecurityException | RuntimeException ex) {
			LogHelper.error("Encountered an error processing 'doDecrypt' in '" + getClass().getSimpleName() + "' - " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
	
	private byte[] getSecret(char[] password) throws GeneralSecurityException, RuntimeException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(new String(password).getBytes(StandardCharsets.UTF_8));
		byte[] secret = new byte[16];
		System.arraycopy(messageDigest.digest(), 0, secret, 0, secret.length);
		return secret;
	}
	
	private String getUniqueId() {
		return uniqueId;
	}
	
	private Account getAccount() {
		return account;
	}
	
	public Data getData() {
		return new Data(getUniqueId());
	}
}