/**
 * 
 */
package com.springboot.contacts.util;

/**
 * @author santhosh
 *
 */
public class ContactCustomError {
	
	private String message;
	
	private String statusCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
