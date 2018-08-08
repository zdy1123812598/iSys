package com.mystic.common;

/**
 * 结果信息
 * 
 * @author zdy
 * @since 2017-04-20
 * 
 */
public class ResultInfo {
	/**
	 * 结果状态值
	 */
	private int code;
	/**
	 * 对状态值的描述
	 */
	private String message;
	/**
	 * 数据
	 */
	private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public ResultInfo(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResultInfo() {
		this.code = 0;
		this.message = "";
	}
	
	public ResultInfo(Integer resultCode){
		this.code = resultCode;
	}
	
	public ResultInfo(Integer resultCode,Object data){
		this.code = resultCode;
		this.data = data;
	}
	
	public ResultInfo(Integer resultCode,String msg,Object data){
		this.code = resultCode;
		this.message = msg;
		this.data = data;
	}
}
