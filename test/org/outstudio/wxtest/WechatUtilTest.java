package org.outstudio.wxtest;

import org.junit.Test;
import org.outstudio.wxtest.util.WechatUtil;

import static org.junit.Assert.assertEquals;

public class WechatUtilTest {

	@Test
	public void getDatetimeFromCreateTimeLength10() {
		String time = WechatUtil.getDatetimeFromCreateTime(1413118184L);
		assertEquals("2014-10-12 20:49:44", time);
	}

	@Test
	public void getDatetimeFromCreateTimeLength13() {
		String time = WechatUtil.getDatetimeFromCreateTime(1413118184292L);
		assertEquals("2014-10-12 20:49:44", time);
	}

}
