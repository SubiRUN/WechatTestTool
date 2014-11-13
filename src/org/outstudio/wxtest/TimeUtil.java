package org.outstudio.wxtest;

import java.sql.Timestamp;

public class TimeUtil {

	public static String getDatetimeFromCreateTime(long createTime) {
		String raw = String.valueOf(createTime);
		String strTime = StringUtil.paddingRight(raw, '0', 13);
		long time = Long.parseLong(strTime);
		return new Timestamp(time).toString().substring(0, 19);
	}
	
	public static void main(String[] args) {
		long t1=System.currentTimeMillis();
		System.out.println(t1);
		
		String s=getDatetimeFromCreateTime(t1);
		System.out.println(s);
	}

}
