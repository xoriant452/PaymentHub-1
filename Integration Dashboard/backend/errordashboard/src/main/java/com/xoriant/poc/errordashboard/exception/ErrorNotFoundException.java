package com.xoriant.poc.errordashboard.exception;

public class ErrorNotFoundException extends Exception {
	private long id;
	public ErrorNotFoundException(long id){
		super(String.format("Error is not found with id : '%s'", id));
	}

}
