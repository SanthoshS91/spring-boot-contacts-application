/**
 * 
 */
package com.springboot.contacts.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.contacts.model.Contacts;
import com.springboot.contacts.service.ContactsService;
import com.springboot.contacts.util.ContactCustomError;
import com.springboot.contacts.util.ContactNotFoundException;

/**
 * @author santhosh
 * Controller marks a class as a Spring Web MVC controller and it is used to handle REST http requests for performing various functionalities such as
 * 		Create a contact record
 *		Retrieve a contact record
 *		Update a contact record
 *		Delete a contact record
 *		Search for a record by email or phone number
 *		Retrieve all records from the same state or city 
 *
 */
@RestController
@RequestMapping("/contacts")
public class ContactsController {
	
	@Autowired
	private ContactsService contactsService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	final static Logger logger = LogManager.getLogger(ContactsController.class);
	/*
	 * This method is used to create a new contact record
	 * contact - consist of all the details to create a new contact
	 * Request Method is POST
	 * returns created contact response
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createContact(@RequestPart(name="contact", required=true) String contactjson, @RequestPart("file") MultipartFile file) throws JsonParseException, JsonMappingException, IOException{
		logger.info("Inside createContact() of ContactsController");
		Contacts contact = mapper.readValue(contactjson, Contacts.class); //convert the string json and map it to contact object
		
		if(isValidPhone(contact.getWorkPhone(), contact.getPersonalPhone())) {
			ContactCustomError customError = setCustomError();			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customError);
		}
		
		setProfileImage(file, contact);	//set the uploaded profile image	
		
		this.contactsService.save(contact);
		
		return new ResponseEntity<Contacts>(contact, HttpStatus.CREATED);
	}
	
	/*
	 * This method is used to retrieve contacts by
	 * 		1. PhoneNumber (Work or Personal or Both)
	 *      2. Email
	 *      3. All contacts if none of the query parameters are present
	 * Request Method is GET
	 * returns queried contacts response
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retrieveContacts(@RequestParam(value="phoneNumber", required = false) String phoneNumber, 
			@RequestParam(value="email", required = false) String email) {
		logger.info("Inside retrieveContacts() of ContactsController");

		List<Contacts> contacts = new ArrayList<Contacts>();
		
		if(!StringUtils.isEmpty(email) && !StringUtils.isEmpty(phoneNumber)) {
			contacts = this.contactsService.findContactsByEmailAndPhone(email, phoneNumber);
		}else if(!StringUtils.isEmpty(email)) {
			contacts = this.contactsService.findContactsByEmail(email);			
		}else if(!StringUtils.isEmpty(phoneNumber)) {
			contacts = this.contactsService.findContactsByPhone(phoneNumber);
		}else {
			contacts = this.contactsService.findAllContacts();
		}
		if(contacts.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Contacts>>(contacts, HttpStatus.OK);
	}
	
	/*
	 * This method is used to retrieve a specific contact based on the contactid
	 * Request Method is GET
	 * returns queried contact response
	 */
	@RequestMapping(value="/{contactId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Contacts> retrieveContact(@PathVariable Long contactId) throws ContactNotFoundException{
		logger.info("Inside retrieveContact() of ContactsController");

		Contacts contact = this.contactsService.findByContactId(contactId);
		if(contact == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Contacts>(contact, HttpStatus.OK);
	}
	
	
	/*
	 * This method is used to delete a contact based on the contact id
	 * Request Method is DELETE
	 * returns successfully deleted message if the contact is deleted
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/{contactId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HttpEntity deleteContact(@PathVariable Long contactId) throws ContactNotFoundException {
		logger.info("Inside deleteContact() of ContactsController");

		Contacts contact = this.contactsService.findByContactId(contactId);
		if(contact == null) {
			throw new ContactNotFoundException();
		}
	    this.contactsService.deleteContact(contact);
	    ContactCustomError customError = new ContactCustomError();
		customError.setMessage("contact has been succesfully deleted");
		customError.setStatusCode("200");
	    return new ResponseEntity<>(customError,HttpStatus.OK);
	}
	
	/*
	 * This method is used to retrieve all records from the same state based on queried state
	 * Request Method is GET
	 * returns list of contacts of the same state
	 */
	@RequestMapping(value="state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Contacts>> retrieveSameStateContacts(@PathVariable String state) {
		logger.info("Inside retrieveSameStateContacts() of ContactsController");

		List<Contacts> contacts = this.contactsService.findContactsByState(state);
		if(contacts.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Contacts>>(contacts, HttpStatus.OK);
	}
	
	/*
	 * This method is used to retrieve all records from the same state based on queried city
	 * Request Method is GET
	 * returns list of contacts of the same city
	 */
	@RequestMapping(value="city/{city}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Contacts>> retrieveSameCityContacts(@PathVariable String city) {
		logger.info("Inside retrieveSameCityContacts() of ContactsController");
		
		List<Contacts> contacts = this.contactsService.findContactsByCity(city);
		if(contacts.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Contacts>>(contacts, HttpStatus.OK);
	}
	
	/*
	 * This method is used to update a record
	 * Request Method is GET
	 * returns list of contacts of the same city
	 */
	@RequestMapping(value="/{contactId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateContact(@RequestPart(name="contact", required=true) String contactjson, 
			@RequestPart("file") MultipartFile file, @PathVariable Long contactId) throws JsonParseException, JsonMappingException, IOException, ContactNotFoundException{
		logger.info("Inside updateContact() of ContactsController");

		Contacts contact = this.contactsService.findByContactId(contactId);
		if(contact == null) {
			throw new ContactNotFoundException();
		}
		contact = mapper.readValue(contactjson, Contacts.class);
		contact.setContactId(contactId);
		if(isValidPhone(contact.getWorkPhone(), contact.getPersonalPhone())) {
			ContactCustomError customError = setCustomError();			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customError);
		}
		setProfileImage(file, contact);
		this.contactsService.updateContact(contact);
		return new ResponseEntity<Contacts>(contact, HttpStatus.OK);
	}
	
	/*
	 * This method is used to set the profile image while creating or updating a contact
	 * Params: MultipartFile and Contact
	 */
	private void setProfileImage(MultipartFile file, Contacts contact) throws IOException {
		if(!file.isEmpty() && !(file.getSize() == 0)) {				
			byte[] imageBytes = file.getBytes();
	        contact.setImage(imageBytes);
			contact.setFileName(file.getOriginalFilename());
			contact.setContentType(file.getContentType());						
		}		
	}
	
	/*
	 * check if the work/personal phone is valid or not
	 */
	private boolean isValidPhone(String workPhone, String personalPhone) {
		return (!StringUtils.isEmpty(workPhone) && !workPhone.matches("[0-9]+")) || 
				(!StringUtils.isEmpty(personalPhone) && !personalPhone.matches("[0-9]+"));
	}
	
	/*
	 * set custom error for work and personal phone validation
	 */
	private ContactCustomError setCustomError() {
		ContactCustomError customError = new ContactCustomError();
		customError.setMessage("Phone number has characters or symbols in the request body");
		customError.setStatusCode("400");
		return customError;
	}
}
