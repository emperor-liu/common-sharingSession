/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache;

import org.apache.commons.lang3.StringUtils;

/** 
 * ClassName: Assert.java <br>
 * Description: <br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class Assert {

	public static void isArrayTrue(String message, boolean... expressions) {
		if (expressions != null) {
			for (boolean expression : expressions) {
				if (!expression) {
					throw new IllegalArgumentException(message);
				}
			}
		}
	}

	public static void isTrue(String message, boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isArrayNotNull(String message, Object... vals) {
		if (vals == null) {
			throw new IllegalArgumentException(message);
		}
		for (Object val : vals) {
			if (val == null) {
				throw new IllegalArgumentException(message);
			}
		}
	}

	public static void isNotNull(String message, Object val) {
		if (val == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isArrayNotBlank(String message, String... vals) {
		if (vals == null) {
			throw new IllegalArgumentException(message);
		}
		for (String val : vals) {
			if (StringUtils.isBlank(val)) {
				throw new IllegalArgumentException(message);
			}
		}
	}

	public static void isNotBlank(String message, String vals) {
		if (vals == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isArrayNotEmpty(String message, String... vals) {
		if (vals == null) {
			throw new IllegalArgumentException(message);
		}
		for (String val : vals) {
			if (StringUtils.isEmpty(val)) {
				throw new IllegalArgumentException(message);
			}
		}
	}

	public static void isNotEmpty(String message, String val) {
		if (val == null) {
			throw new IllegalArgumentException(message);
		}

	}

	public static <T> void isEqual(String message, Object val, Object expectV) {
		if (!val.equals(expectV)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static <T> void isNotEqual(String message, Object val, Object expectV) {
		if (val.equals(expectV)) {
			throw new IllegalArgumentException(message);
		}
	}
}
