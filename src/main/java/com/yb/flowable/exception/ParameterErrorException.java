package com.yb.flowable.exception;

/**
 * 
 * @author chenzhaopeng
 *
 */
public class ParameterErrorException extends RuntimeException {
	private static final long serialVersionUID = -1587055325708762878L;

	public ParameterErrorException(){
		super();
	}

	public ParameterErrorException(String message){
		super(message);
	}
	
	public static void message(String message) {
		throw new ParameterErrorException(message);		
	}

}
