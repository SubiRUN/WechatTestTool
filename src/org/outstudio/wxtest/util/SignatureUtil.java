package org.outstudio.wxtest.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignatureUtil {

	private static final char[] digit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 得到签名
	 */
	public static String getSignature(String token, String timestamp,
			String nonce) {

		String[] arr = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes("UTF-8"));
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 */
	private static String byteToStr(byte[] byteArray) {
		int len = byteArray.length;
		StringBuilder strDigest = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			strDigest.append(byteToHexStr(byteArray[i]));
		}
		return strDigest.toString();
	}

	/**
	 * 将字节转换为十六进制字符串
	 */
	private static String byteToHexStr(byte mByte) {
		char[] tempArr = new char[2];
		tempArr[0] = digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = digit[mByte & 0X0F];

		return new String(tempArr);
	}

}
