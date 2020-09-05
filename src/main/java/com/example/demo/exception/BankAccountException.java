package com.example.demo.exception;

public class BankAccountException extends Exception {

	private static final long serialVersionUID = 8531462505024194729L;

	public BankAccountException(String message) {
		super(message);
	}
}
