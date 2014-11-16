package org.outstudio.wxtest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.outstudio.wxtest.util.XmlUtil;

public class XmlUtilTest {

	static String TEXT_REQ_MSG;
	static String IMAGE_REQ_MSG;
	static String VOICE_REQ_MSG;
	static String VIDEO_REQ_MSG;
	static String LOCATION_REQ_MSG;
	static String LINK_REQ_MSG;

	static String INVALID_XML_1;

	static {
		try {
			TEXT_REQ_MSG = FileUtils.readFileToString(new File(
					"./test_res/TextReqMsg.txt"));
			IMAGE_REQ_MSG = FileUtils.readFileToString(new File(
					"./test_res/ImageReqMsg.txt"));
			VOICE_REQ_MSG = FileUtils.readFileToString(new File(
					"./test_res/VoiceReqMsg.txt"));
			VIDEO_REQ_MSG = FileUtils.readFileToString(new File(
					"./test_res/VideoReqMsg.txt"));
			LOCATION_REQ_MSG = FileUtils.readFileToString(new File(
					"./test_res/LocationReqMsg.txt"));
			LINK_REQ_MSG = FileUtils.readFileToString(new File(
					"./test_res/LinkReqMsg.txt"));
			INVALID_XML_1 = FileUtils.readFileToString(new File(
					"./test_res/invalid_xml_1.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void validTextReqMsgIsValidXml() {
		assertEquals(true, XmlUtil.isValidXml(TEXT_REQ_MSG));
	}

	@Test
	public void validImageReqMsgIsValidXml() {
		assertEquals(true, XmlUtil.isValidXml(IMAGE_REQ_MSG));
	}

	@Test
	public void validVoiceReqMsgIsValidXml() {
		assertEquals(true, XmlUtil.isValidXml(VOICE_REQ_MSG));
	}

	@Test
	public void validVideoReqMsgIsValidXml() {
		assertEquals(true, XmlUtil.isValidXml(VIDEO_REQ_MSG));
	}

	@Test
	public void validLocationReqMsgIsValidXml() {
		assertEquals(true, XmlUtil.isValidXml(LOCATION_REQ_MSG));
	}

	@Test
	public void validLinkReqMsgIsValidXml() {
		assertEquals(true, XmlUtil.isValidXml(LINK_REQ_MSG));
	}

	@Test
	public void invalidXmlCase1() {
		assertEquals(false, XmlUtil.isValidXml(INVALID_XML_1));
	}
}
