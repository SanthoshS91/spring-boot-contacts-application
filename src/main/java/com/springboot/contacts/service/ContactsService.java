/**
 * 
 */
package com.springboot.contacts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.contacts.model.Contacts;
import com.springboot.contacts.repository.ContactsRepository;

/**
 * @author santhosh
 *
 */
@Service
public class ContactsService {
	
	@Autowired
    private ContactsRepository contactsRepository;
	
	public Contacts save(Contacts contact) {
	    return this.contactsRepository.save(contact);
	}
	
	public List<Contacts> findAllContacts() {
	    return this.contactsRepository.findAll();
	}
	
	public Contacts findByContactId(Long contactId) {
	    Contacts contact = this.contactsRepository.findOne(contactId);
	    return contact;
	}
	
	public void deleteContact(Contacts contact) {
	    this.contactsRepository.delete(contact);
	}

	public List<Contacts> findContactsByEmail(String email) {
		return this.contactsRepository.findContactsByEmail(email);
	}

	public List<Contacts> findContactsByPhone(String phoneNumber) {
		return this.contactsRepository.findContactsByPhone(phoneNumber);
	}

	public List<Contacts> findContactsByEmailAndPhone(String email, String phoneNumber) {
		return this.contactsRepository.findContactsByEmailAndPhone(email, phoneNumber);
	}

	public List<Contacts> findContactsByState(String state) {
		return this.contactsRepository.findContactsByState(state);
	}

	public List<Contacts> findContactsByCity(String city) {
		return this.contactsRepository.findContactsByCity(city);
	}

	public Contacts updateContact(Contacts contact) {
		return this.contactsRepository.save(contact);
	}

}
