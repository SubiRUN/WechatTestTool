package org.outstudio.wxtest;

import org.junit.Test;
import org.outstudio.wxtest.util.StringUtil;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {

	@Test
	public void paddingRightNormalCase() {
		String result = StringUtil.paddingRight("abc", '0', 5);
		assertEquals("abc00", result);
	}

	@Test
	public void paddingRightWhenInputStringIsLongerThanInputLength() {
		String result = StringUtil.paddingRight("123456789", '0', 5);
		assertEquals("123456789", result);
	}
	
}
