package com.shanchain.data.common.utils.encryption;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DesUtils {
	
    private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

    public static String encrypt(String content, String strkey) {
        if (content.equals("")) {
            return "";
        } else
            return doFinal(Cipher.ENCRYPT_MODE, content, strkey);
    }

    public static String decrypt(String content, String strkey) {
        if (content.equals("")) {
            return "";
        } else
            return doFinal(Cipher.DECRYPT_MODE, content, strkey);
    }

    public static String doFinal(int opmode, String content, String strkey) {
        try {
        	IvParameterSpec zeroIv = new IvParameterSpec(iv);
            Key key = new SecretKeySpec(Base64.decode(strkey, Base64.DEFAULT), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(opmode, key, zeroIv);

            //
            // make input
            byte plaintext[] = null;
            if (opmode == Cipher.DECRYPT_MODE)
                plaintext = Base64.decode(content, Base64.DEFAULT);
            else
                plaintext = content.getBytes("UTF-8");

            byte[] output = cipher.doFinal(plaintext);
            // make output
            String Ciphertext = null;
            if (opmode == Cipher.DECRYPT_MODE)
                Ciphertext = new String(output);
            else
                Ciphertext = Base64.encodeToString(output, Base64.DEFAULT);

            return Ciphertext;

        } catch (Exception e) {

            return null;
        }
    }
    
    public static byte[] encrypt(byte[] content, String strkey) {
        if (null == content || content.length == 0) {
            return null;
        } else
            return doFinal(Cipher.ENCRYPT_MODE, content, strkey);
    }

    public static byte[] decrypt(byte[] content, String strkey) {
        if (null == content || content.length == 0) {
            return null;
        } else
            return doFinal(Cipher.DECRYPT_MODE, content, strkey);
    }

    public static byte[] doFinal(int opmode, byte[] content, String strkey) {
        try {
        	IvParameterSpec zeroIv = new IvParameterSpec(iv);
            Key key = new SecretKeySpec(Base64.decode(strkey, Base64.DEFAULT), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(opmode, key, zeroIv);

            //
            // make input
            byte plaintext[] = null;
            if (opmode == Cipher.DECRYPT_MODE)
                plaintext = Base64.decode(content, Base64.DEFAULT);
            else
                plaintext = content;

            byte[] output = cipher.doFinal(plaintext);

            //
            // make output
            byte[] Ciphertext = null;
            if (opmode == Cipher.DECRYPT_MODE)
                Ciphertext = output;
            else
                Ciphertext = Base64.encode(output, Base64.DEFAULT);

            return Ciphertext;

        } catch (Exception e) {

            return null;
        }
    }
    
	@SuppressLint("TrulyRandom") 
	public static String generateDesKey() {
		SecureRandom sr = new SecureRandom();
		try {
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			kg.init(sr);
			Key key = kg.generateKey();
			return new String(Base64.encode(key.getEncoded(), Base64.DEFAULT));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
