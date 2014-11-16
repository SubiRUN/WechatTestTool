package org.outstudio.wxtest.util;

public class StringUtil {

	/**
	 * 将字符串补充为指定长度的字符串，从右侧添加字符
	 * 
	 * @param str
	 *            填充前的字符串
	 * @param ch
	 *            填充使用的字符
	 * @param len
	 *            字符串的目标长度
	 * @return 填充后的字符串
	 */
	public static String paddingRight(String str, char ch, int len) {
		StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < len - str.length(); i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

}
