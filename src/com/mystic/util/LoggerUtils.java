package com.mystic.util;

import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtils {

	@Autowired
	private JdbcTemplate jt;

	private static final Log logger = LogFactory.getLog(LoggerUtils.class);

	public static void error(Log logger, Exception ex) {
		logger.error(ex.getMessage(), ex);
		ex.printStackTrace();
	}

	public static void error(Exception ex) {
		error(logger, ex);
	}

	public static void error(String msg, Exception ex) {
		error(logger, new Exception(msg, ex));
	}

	public static void error(Object obj) {
		logger.error(obj);
	}

	public static void info(Object obj) {
		logger.info(obj);
	}

	public static void warn(Object obj) {
		logger.warn(obj);
	}

	public static void debug(Object obj) {
		logger.debug(obj);
	}

	public void log2DB(String log, int userid) {
		try {
			jt.update(
					"insert into tblog(logid,userid,logcontent,logtime)values(?,?,?,?)",
					makeUUID().toUpperCase(), userid, log,
					new Timestamp(System.currentTimeMillis()));
		} catch (Exception ex) {
			error("log2db error :" + ex.getMessage(), ex);
		}
	}

	public static String makeUUID() {
		return UUID.randomUUID().toString();
	}

	public static final class LoggerType {
		// 删除FTP文件
		public static final String FTP_DELETEFILE = "[FTP_DELETEFILE]从FTP删除文件[%s]";
	}
}
