package com.mystic.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 * MIS 系统中共用的字符串相关的处理函数
 * @author ShiHong
 */
public class StringUtils {
	private static Log logger = LogFactory.getLog(StringUtils.class);
	
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	
	
	private final static String[] dateReg ={
		"^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])\\s(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})$",//"yyyy-MM-dd hh:mm:ss",
		"^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])\\s(0\\d{1}|1\\d{1}|2[0-3])$",//"yyyy-MM-dd hh",
		"^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$",//"yyyy-MM-dd",
		"^(\\d{4})-(0\\d{1}|1[0-2])$"//"yyyy-MM"
	}; 
	
	
	private final static Map<String, String> datePattern = new HashMap<String, String>();
	static {
		datePattern.put("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])\\s(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})$", "yyyy-MM-dd hh:mm:ss");
		datePattern.put("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])\\s(0\\d{1}|1\\d{1}|2[0-3])$", "yyyy-MM-dd hh");
		datePattern.put("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$", "yyyy-MM-dd");
		datePattern.put("^(\\d{4})-(0\\d{1}|1[0-2])$", "yyyy-MM");
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 字符串MD5加密
	 * @param origin 源字符串
	 * @return MD5加密后的大写字符串
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {

		}
		return resultString.toUpperCase();
	}
	
	/**
	 * 计算系统屏幕字符串显示长度
	 * @param text 字符串
	 * @param font 字体
	 * @return 长度
	 */
	public static double getDisplayWidth(String text, Font font) {
		if(text == null || text.trim().equals("")) {
			return -1;
		}
		Graphics g = null;
		try {
			g = new Robot().createScreenCapture(
					new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())).getGraphics();
		} catch(Exception e) {
		}  
		Graphics2D g2d = (Graphics2D)g;
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(text, font, frc);
		Rectangle2D bounds = layout.getBounds();
		double width =  bounds.getWidth();
		return width + 5;
	}
	
	/**
	 * 计算指定上下文字符串显示长度
	 * @param text 字符串
	 * @param g2d 绘制上下文
	 * @param font 字体
	 * @return 长度
	 */
	public static double getDisplayWidth(String text,
			Graphics2D g2d, Font font) {
		if(text == null || text.trim().equals("")) {
			return -1;
		}
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(text, font, frc);
		Rectangle2D bounds = layout.getBounds();
		double width =  bounds.getWidth();
		return width + 5;
	}
	/***
	 * 把对象转成json格式的字符串
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String serializeToJson(Object object) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();
		return writer.writeValueAsString(object);
	}
	
	public static <T> T parseJSON(String json, Class<T> clazz) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, clazz);
	}
	
	/**
	 * 将数组转成用分隔符连接的字符串
	 * @param array 待处理的数组
	 * @param separate 分隔符
	 * @return
	 */
	public static String arrayToString(int[] array, String separate){
		StringBuilder sb = new StringBuilder();
		for (int i : array) {
			sb.append(i).append(separate);
		}
		String temp = sb.toString();
		return "".equals(temp) ? temp : temp.substring(0,temp.length()-1); 
	}
	/**
	 * 将数组转成用分隔符连接的字符串
	 * @param array 待处理的数组
	 * @param separate 分隔符
	 * @return
	 */
	public static String arrayToString(long[] array, String separate){
		StringBuilder sb = new StringBuilder();
		for (long i : array) {
			sb.append(i).append(separate);
		}
		String temp = sb.toString();
		return "".equals(temp) ? temp : temp.substring(0,temp.length()-1); 
	}
	/**
	 * 将数组转成用分隔符连接的字符串
	 * @param array 待处理的数组
	 * @param separate 分隔符
	 * @return
	 */
	public static String arrayToString(String[] array, String separate){
		StringBuilder sb = new StringBuilder();
		for (String i : array) {
			sb.append(i).append(separate);
		}
		String temp = sb.toString();
		return "".equals(temp) ? temp : temp.substring(0,temp.length()-1); 
	}
	/**
	 * 将list转成用分隔符连接的字符串
	 * @param list 待处理的list
	 * @param separate 分隔符
	 * @return
	 * chenwenju
	 * 2012-6-27
	 */
	public static String listToString(List<String> list, String separate){
		StringBuilder sb = new StringBuilder();
		Object[] array = list.toArray();
		for (Object i : array) {
			sb.append(i.toString()).append(separate);
		}
		String temp = sb.toString();
		return "".equals(temp) ? temp : temp.substring(0,temp.length()-1).trim();
	}
	
	public static String makeUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
	
	/**
	 * 将时间转换串转换成时间对像
	 * @param pattern	时间格式 如 yyyy-MM-dd
	 * @param source	时间字符串
	 * @return
	 */
	public static Date parseDate(String pattern, String source) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(source);
	}
	
	
	/**
	 * 将传入的时间字符串自动的转成相应的时间对像
	 * @param source 时间字符串
	 * @return
	 */
	public static Date parseDate(String source) throws ParseException{
		for(String regex : dateReg){
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(source);
			if(matcher.matches()){
				return parseDate(datePattern.get(regex),source);
			}
		}
		throw new ParseException(source+" 不支持的时间格式", 0);
	}
	
	public static String getFullPinyinString(String chinese) {
		if (org.apache.commons.lang.StringUtils.isEmpty(chinese)) {
			return chinese;
		}
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if(temp == null || temp.length == 0){
						logger.debug(arr[i] + " get null for pinyin translation");
					}else{
						pybf.append(temp[0]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().trim();
	}
}
