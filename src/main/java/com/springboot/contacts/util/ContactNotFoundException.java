/**
 * 
 */
package com.springboot.contacts.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author santhosh
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Contact(s) does not exist in the database")
public class ContactNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -618786708093484307L;

}
