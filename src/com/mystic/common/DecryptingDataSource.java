package com.mystic.common;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.InitializingBean;

import com.mystic.util.EncryptUtils;

/**
 * 带解密功能的数据源
 * 
 * @author zdy
 * @since 2017-04-20
 * 
 */
public class DecryptingDataSource extends BasicDataSource implements InitializingBean {

	/**
	 * 密码是否加密
	 */
	private boolean encrypted;

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	@Override
	public void afterPropertiesSet() {
		if (encrypted) {
			String password = EncryptUtils.decryptByDES(this.getPassword());
			this.setPassword(password);
		}
	}

}
