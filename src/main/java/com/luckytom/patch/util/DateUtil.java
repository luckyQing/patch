package com.luckytom.patch.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author luckytom
 * @version 1.0 2017年11月29日 上午10:19:07
 */
public final class DateUtil {
	
	public static final ThreadLocal<DateFormat> SDF_YYYYMMDDHHMMSS = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	public static String convertDateToStr(Date date){
		return SDF_YYYYMMDDHHMMSS.get().format(date);
	}
}
