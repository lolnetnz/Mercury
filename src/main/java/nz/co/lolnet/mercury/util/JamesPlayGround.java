/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet.mercury.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class JamesPlayGround {

    static HashMap<String, String> keys = new HashMap<>();
    static String initVector = "jeXtEsnJreZ4EA6F"; // 16 bytes IV

    public static String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String key = "GdfxtHw9ce2SjHEN"; // 128 bit key
        String key2 = "mEbvGDDmYKMka4LAb6eZTZeJ";
        keys.put("James", key);
        keys.put("lx", key2);
        String encrypt = "hh1iTa/bZbUNTX4c1MA7ZA==";
        for (String owner : keys.keySet()) {
            boolean correctKey = false;
            String trykey = keys.get(owner);
            try {
                
            } catch (Exception e) {
            }
        }
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hello",true);
        String input = jsonObject.toString();
    
        String decrypt = decrypt(key2, encrypt);


        System.out.println(decrypt(key, encrypt(key, "Hello World")));
        
        JsonParser parser = new JsonParser();
        JsonObject jsonObject2 = parser.parse(input).getAsJsonObject();
        System.out.println(jsonObject.toString());
        boolean output = jsonObject.get("hello").getAsBoolean();
        System.out.println(output);
    }
}
