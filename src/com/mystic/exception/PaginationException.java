package com.mystic.exception;

public class PaginationException extends RuntimeException{
	private static final long serialVersionUID = -2625736110854144241L;
	public PaginationException(String message){
		super(message);
	}
	public PaginationException(String message,Throwable err){
		super(message,err);
	}
	public PaginationException(Throwable err){
		super(err);
	}
}
