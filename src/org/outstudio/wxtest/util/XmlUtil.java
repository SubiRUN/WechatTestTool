package org.outstudio.wxtest.util;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

public class XmlUtil {

	/**
	 * 判断给定字符串是否符合XML格式
	 */
	public static boolean isValidXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			db.parse(new InputSource(reader));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
