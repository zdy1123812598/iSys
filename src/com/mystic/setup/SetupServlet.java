package com.mystic.setup;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mystic.common.DecryptingDataSource;
import com.mystic.common.ResultInfo;
import com.mystic.util.EncryptUtils;
import com.mystic.util.ServletUtils;

/**
 * 初始设置
 * 
 * @author zdy
 * @since 2017-04-20
 * 
 */
public class SetupServlet extends HttpServlet {

	private static final long serialVersionUID = 5361661684471532593L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		Properties p = new Properties();
		String file = request.getSession().getServletContext()
				.getRealPath("/WEB-INF/conf.properties");
		p.load(new FileReader(file));
		String password = p.getProperty("jdbc.password");
		String url = p.getProperty("jdbc.url");
		String username = p.getProperty("jdbc.username");
		String driverClassName = p.getProperty("jdbc.driverClassName");
		String encrypted = p.getProperty("password.encrypted");
		if ("load".equals(type)) { // 加载配置
			Map<String, String> data = new HashMap<String, String>();
			data.put("url", url);
			data.put("username", username);
			data.put("password", password);
			data.put("encrypted", encrypted);
			ServletUtils.writeJsonToResponse(response, data);
		} else if ("save".equals(type)) { // 保存配置
			password = request.getParameter("password");
			url = request.getParameter("url");
			username = request.getParameter("username");
			encrypted = request.getParameter("encrypted");
			String pwd = encrypted == null ? password : EncryptUtils
					.encryptToDES(password);
			p.setProperty("jdbc.url", url);
			p.setProperty("jdbc.username", username);
			p.setProperty("jdbc.password", pwd);
			p.setProperty("password.encrypted",
					String.valueOf(encrypted != null));
			p.store(new FileWriter(file), null);
			ServletUtils.writeJsonToResponse(response,
					new ResultInfo(0, "保存成功"));
		} else if ("test".equals(type)) { // 测试连接
			try {
				DecryptingDataSource ds = new DecryptingDataSource();
				ds.setUrl(url);
				ds.setPassword(password);
				ds.setDriverClassName(driverClassName);
				ds.setUsername(username);
				ds.afterPropertiesSet();
				ds.getConnection();
				ServletUtils.writeJsonToResponse(response, new ResultInfo(0,
						"连接成功"));
			} catch (Exception e) {
				e.printStackTrace();
				ServletUtils.writeJsonToResponse(response, new ResultInfo(-1,
						"连接失败，错误原因：" + e.getMessage()));
			}
		}

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}	
	
}