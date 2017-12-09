/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session.utils
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session.utils;

import java.lang.reflect.Array;

/** 
 * ClassName: StringUtil.java <br>
 * Description: <br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class StringUtil {
	
	private static final char[] DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final char[] DIGITS_NOCASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	public static String longToString(long longValue) {
		return longToString(longValue, false);
	}

	public static String longToString(long longValue, boolean noCase) {
		char[] digits = noCase ? DIGITS_NOCASE : DIGITS;
		int digitsLength = digits.length;

		if (longValue == 0) {
			return String.valueOf(digits[0]);
		}

		if (longValue < 0) {
			longValue = -longValue;
		}

		StringBuilder strValue = new StringBuilder();

		while (longValue != 0) {
			int digit = (int) (longValue % digitsLength);
			longValue = longValue / digitsLength;

			strValue.append(digits[digit]);
		}

		return strValue.toString();
	}

	public static String bytesToString(byte[] bytes) {
		return bytesToString(bytes, false);
	}

	public static String bytesToString(byte[] bytes, boolean noCase) {
		char[] digits = noCase ? DIGITS_NOCASE : DIGITS;
		int digitsLength = digits.length;

		if (isEmptyArray(bytes)) {
			return String.valueOf(digits[0]);
		}

		StringBuilder strValue = new StringBuilder();
		int value = 0;
		int limit = Integer.MAX_VALUE >>> 8;
		int i = 0;

		do {
			while (i < bytes.length && value < limit) {
				value = (value << 8) + (0xFF & bytes[i++]);
			}

			while (value >= digitsLength) {
				strValue.append(digits[value % digitsLength]);
				value = value / digitsLength;
			}
		} while (i < bytes.length);

		if (value != 0 || strValue.length() == 0) {
			strValue.append(digits[value]);
		}

		return strValue.toString();
	}

	public static boolean isEmptyArray(Object array) {
		if (array == null) {
			return true;
		}

		if (!array.getClass().isArray()) {
			return false;
		}

		return Array.getLength(array) == 0;
	}
}
