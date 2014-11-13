package org.outstudio.wxtest;

import org.junit.Test;
import org.outstudio.wxtest.TimeUtil;

import static org.junit.Assert.assertEquals;

public class TimeUtilTest {

	@Test
	public void getDatetimeFromCreateTimeLength10() {
		String time = TimeUtil.getDatetimeFromCreateTime(1413118184L);
		assertEquals("2014-10-12 20:49:44", time);
	}

	@Test
	public void getDatetimeFromCreateTimeLength13() {
		String time = TimeUtil.getDatetimeFromCreateTime(1413118184292L);
		assertEquals("2014-10-12 20:49:44", time);
	}

}
