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

package nz.co.lolnet.mercury.util.JamesPlayerGround;

import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


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

            return Base64.getEncoder().encodeToString(encrypted);
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

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

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
        System.out.println(jsonObject.toString());
        boolean output = jsonObject.get("hello").getAsBoolean();
        System.out.println(output);
    }
}
