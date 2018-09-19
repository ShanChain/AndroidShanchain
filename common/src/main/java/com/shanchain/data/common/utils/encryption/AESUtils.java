package com.shanchain.data.common.utils.encryption;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {


	private static String ivParameter = "shanchain1848000";//

	private static String WAYS = "AES";
	private static String MODE = "";
	private static boolean isPwd = false;
	private static String ModeCode = "PKCS5Padding";

	private static int pwdLenght = 16;
	private static String val = "0";

	public static String selectMod(int type) {
		// ECB("ECB", "0"), CBC("CBC", "1"), CFB("CFB", "2"), OFB("OFB", "3");
		switch (type) {
		case 0:
			isPwd = false;
			MODE = WAYS + "/" + AESType.ECB.key() + "/" + ModeCode;

			break;
		case 1:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CBC.key() + "/" + ModeCode;
			break;
		case 2:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CFB.key() + "/" + ModeCode;
			break;
		case 3:
			isPwd = true;
			MODE = WAYS + "/" + AESType.OFB.key() + "/" + ModeCode;
			break;

		}
		return MODE;
	}


	public static String encrypt(String sSrc, String sKey) {
		try {
			sKey = toMakekey(sKey, pwdLenght, val);
			Cipher cipher = Cipher.getInstance(selectMod(1));
			byte[] raw = sKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, WAYS);

			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			if (isPwd == false) {// ECB
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			}

			byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
			return URLEncoder.encode(new String(Base64.encode(encrypted)), "UTF-8");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(sSrc);
			System.out.println("encrypt failed and key is "+sKey);
			return null;
		}

	}

	public static String decrypt(String sSrc, String sKey) {
		try {
			sSrc = URLDecoder.decode(sSrc, "UTF-8");
			sKey = toMakekey(sKey, pwdLenght, val);
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, WAYS);
			Cipher cipher = Cipher.getInstance(selectMod(1));
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			if (isPwd == false) {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			}
			byte[] encrypted1 = Base64.decode(sSrc.getBytes());
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	// key取前16位
	public static String toMakekey(String str, int strLength, String val) {
		byte[] arrA = new byte[16];
		byte[] arrB = str.getBytes();
		for (int i = 0; i < arrA.length; i++) {
			arrA[i] = arrB[i];
		}
		return new String(arrA);
	}


	public static byte[] newencrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static byte[] newdecrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}


	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
