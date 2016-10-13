package com.geetest.sdk.java.web.demo;

/**
 * GeetestWeb配置文件
 *
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String geetest_id = "cdd773c38a510a0c299bd55566f8d251";
	private static final String geetest_key = "2b09e2e8db4f15f13fb8b6544fbcf63d";

	public static final String getGeetest_id() {
		return geetest_id;
	}

	public static final String getGeetest_key() {
		return geetest_key;
	}

}
