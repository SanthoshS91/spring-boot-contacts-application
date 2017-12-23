/**
 * 
 */
package com.springboot.contacts.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.contacts.model.Contacts;
import com.springboot.contacts.repository.ContactsRepository;
import com.springboot.contacts.service.ContactsService;

/**
 * @author santhosh
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactsServiceTest {
	
	@Mock
    private ContactsRepository contactsRepository;
	
	@InjectMocks
	private ContactsService contactsService;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindAllContacts() {
		List<Contacts> contacts = new ArrayList<Contacts>();
		contacts.add(setupContact1());
		contacts.add(setupContact2());
		contacts.add(setupContact3());
		when(contactsRepository.findAll()).thenReturn(contacts);
		
		List<Contacts> cntcts = contactsService.findAllContacts();
		assertEquals(3, cntcts.size());

	}
	
	@Test
	public void testFindContactById() {
		Contacts contact = setupContact3();	
		when(contactsRepository.findOne(anyLong())).thenReturn(contact);
		Contacts cntct = contactsService.findByContactId((long) 1);
		assertTrue(cntct instanceof Contacts);
		assertEquals((long)3, cntct.getContactId());
		assertEquals("John", cntct.getFirstName());
		assertEquals("Doe", cntct.getLastName());
		assertEquals("XYZ", cntct.getCompany());
		assertEquals("abc@gmail.com", cntct.getEmail());
		assertEquals("4567890234", cntct.getWorkPhone());
		assertEquals("1123451234", cntct.getPersonalPhone());
		assertEquals("Main St", cntct.getAddressLine1());
		assertEquals("Evanston", cntct.getCity());
		assertEquals("Illinois", cntct.getState());
		assertEquals("US", cntct.getCountry());
		assertEquals("56024", cntct.getZip());
		assertEquals("img.png", cntct.getFileName());
		assertTrue(cntct.getImage() instanceof byte[]);
		assertTrue(cntct.getDob() instanceof java.sql.Date);
		assertEquals("image/png", cntct.getContentType());
		assertEquals(true, cntct.canEqual(contact));
		assertTrue(cntct.toString().contains("contactId"));
	}
	
	@Test
	public void testFindContactsByEmail() {
		List<Contacts> contacts = new ArrayList<Contacts>();
		contacts.add(setupContact2());
		contacts.add(setupContact3());	
		when(contactsRepository.findContactsByEmail(anyString())).thenReturn(contacts);
		List<Contacts> cntct = contactsService.findContactsByEmail("abc@gmail.com");
		assertEquals(2, cntct.size());
	}
	
	@Test
	public void testFindContactsByPhone() {
		List<Contacts> contacts = new ArrayList<Contacts>();
		contacts.add(setupContact2());
		when(contactsRepository.findContactsByPhone(anyString())).thenReturn(contacts);
		List<Contacts> cntct = contactsService.findContactsByPhone("1143454234");
		assertEquals(1, cntct.size());
		assertEquals("1143454234", cntct.get(0).getPersonalPhone());
	}
	
	@Test
	public void testFindContactsByEmailAndPhone() {
		List<Contacts> contacts = new ArrayList<Contacts>();
		contacts.add(setupContact3());
		when(contactsRepository.findContactsByEmailAndPhone(anyString(), anyString())).thenReturn(contacts);
		List<Contacts> cntct = contactsService.findContactsByEmailAndPhone("abc@gmail.com", "1123451234");
		assertEquals(1, cntct.size());
	}
	
	@Test
	public void testFindContactsByCity() {
		List<Contacts> contacts = new ArrayList<Contacts>();
		contacts.add(setupContact3());
		when(contactsRepository.findContactsByCity(anyString())).thenReturn(contacts);
		List<Contacts> cntct = contactsService.findContactsByCity("Evanston");
		assertEquals(1, cntct.size());
	}
	
	@Test
	public void testFindContactsByState() {
		List<Contacts> contacts = new ArrayList<Contacts>();
		contacts.add(setupContact2());
		contacts.add(setupContact3());
		when(contactsRepository.findContactsByState(anyString())).thenReturn(contacts);
		List<Contacts> cntct = contactsService.findContactsByState("Illinois");
		assertEquals(2, cntct.size());
	}
	
	@Test
	public void testSaveContact(){
		Contacts contact = setupContact3();	
		when(contactsRepository.save(contact)).thenReturn(contact);
		Contacts result = contactsService.save(contact);
		assertEquals((long)3, result.getContactId());
		assertEquals("John", result.getFirstName());
	}
	
	@Test
	public void deleteContact(){
		Contacts contact = setupContact2();	
		contactsService.deleteContact(contact);
        verify(contactsRepository, times(1)).delete(contact);
	}
	
	@Test
	public void updateContact(){
		Contacts contact = setupContact1();	
		when(contactsRepository.save(contact)).thenReturn(contact);
		Contacts result = contactsService.updateContact(contact);
		assertEquals((long)1, result.getContactId());
		assertEquals("Mike", result.getFirstName());
	}

	private Contacts setupContact3() {
		Contacts contact = new Contacts();
		contact.setContactId(3);
		String startDate="1991-02-15";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = sdf1.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());  
		contact.setFirstName("John");
		contact.setLastName("Doe");
		contact.setPersonalPhone("1123451234");
		contact.setEmail("abc@gmail.com");
		contact.setWorkPhone("4567890234");
		contact.setImage("iVBORw0KGgoAAAANSUhEUgAABGIAAAMHCAMAAABFcwHKAAAAtFBMVEUAAAAAADoAAGYAOjoAOpAAZrY6AAA6ADo6AGY6OgA6Ojo6OpA6ZrY6kJA6kLY6kNtmAABmADpmAGZmOjpmOmZmOpBmZjpmZmZmZrZmkJBmkNtmtrZmtttmtv".getBytes());
		contact.setFileName("img.png");
		contact.setContentType("image/png");
		contact.setDob(sqlStartDate);
		contact.setCompany("XYZ");
		contact.setAddressLine1("Main St");
		contact.setCity("Evanston");
		contact.setState("Illinois");
		contact.setCountry("US");
		contact.setZip("56024");
		return contact;
	}

	private Contacts setupContact2() {
		Contacts contact = new Contacts();
		contact.setContactId(2);
		String startDate="1993-02-15";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = sdf1.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());  
		contact.setFirstName("Jane");
		contact.setLastName("Doe");
		contact.setEmail("abc@gmail.com");
		contact.setPersonalPhone("1143454234");
		contact.setDob(sqlStartDate);
		contact.setCompany("ABC");
		contact.setAddressLine1("Main St");
		contact.setCity("Chicago");
		contact.setState("Illinois");
		contact.setCountry("US");
		contact.setZip("66024");
		return contact;
	}

	private Contacts setupContact1() {
		Contacts contact = new Contacts();
		contact.setContactId(1);
		String startDate="1991-05-15";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = sdf1.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());  
		contact.setFirstName("Mike");
		contact.setLastName("Ross");
		contact.setPersonalPhone("2223451234");
		contact.setDob(sqlStartDate);
		contact.setCompany("XYZ");
		contact.setAddressLine1("Glove St");
		contact.setCity("SugarLand");
		contact.setState("Texas");
		contact.setCountry("US");
		contact.setZip("56424");
		return contact;
	}
}
