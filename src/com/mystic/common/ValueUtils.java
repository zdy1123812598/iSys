package com.mystic.common;

import java.util.Map;

public class ValueUtils {
	public static int getInt(Object obj){
		return getInt(obj,-1);
	}
	
	public static int getInt(Object obj,int defaultValue){
		int value = defaultValue;
		try{
			value = Integer.parseInt(obj.toString());
		}catch(Exception ex){}
		return value;
	}
	
	public static int getInt(Map<String, Object> map, String key, int defaultValue){
		return getInt(map.get(key), defaultValue);
	}
	
	public static int getInt(Map<String, Object> map, String key){
		return getInt(map.get(key), -1);
	}
	
	public static String getString(Object obj){
		return getString(obj,"");
	}
	
	public static String getString(Object obj,String defaultValue){
		String value = defaultValue;
		if(null == obj)
			return value;
		try{
			value = obj.toString();
		}catch(Exception ex){}
		return value;
	}
	
	public static String getString(Map<String, Object> map, String key, String defaultValue){
		return getString(map.get(key), defaultValue);
	}
	
	public static String getString(Map<String, Object> map, String key){
		return getString(map.get(key), "");
	}
}
