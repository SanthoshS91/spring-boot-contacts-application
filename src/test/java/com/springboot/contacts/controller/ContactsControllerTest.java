/**
 * 
 */
package com.springboot.contacts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.springboot.contacts.controller.ContactsController;
import com.springboot.contacts.model.Contacts;
import com.springboot.contacts.service.ContactsService;

/**
 * @author santhosh
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ContactsController.class)
@AutoConfigureMockMvc(secure=false)
public class ContactsControllerTest {
	
	private MockMvc mockMvc;
	
	Contacts contact;
	
	@Autowired
    private WebApplicationContext wac;
	
	@Autowired
    private ContactsController controller;
	
	@MockBean
    private ContactsService contactsServiceMocks;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        contact = setupContact3();

	}
	
	@Test
    public void controllerInitialization() throws Exception {
        assertThat(controller).isNotNull();
    }
	
	@Test
    public void testListOrders() throws Exception {
    	assertThat(this.contactsServiceMocks).isNotNull();
    }
	
	@Test
    public void testGetContactById() throws Exception {

        when(contactsServiceMocks.findByContactId((long)3)).thenReturn(contact);

        mockMvc.perform(get("/contacts/{contactId}", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print());

        verify(contactsServiceMocks, times(1)).findByContactId((long)3);
        verifyNoMoreInteractions(contactsServiceMocks);
    }
	
	@Test
    public void testGetContactByIdNotExists() throws Exception {

        when(contactsServiceMocks.findByContactId((long)2)).thenReturn(null);

        mockMvc.perform(get("/contacts/{contactId}", 2))
        .andExpect(status().is4xxClientError())
                .andDo(print());

        verify(contactsServiceMocks, times(1)).findByContactId((long)2);
        verifyNoMoreInteractions(contactsServiceMocks);
    }
	
	@Test
	public void testGetAllContacts() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
		contList.add(contact);
        when(contactsServiceMocks.findAllContacts()).thenReturn(contList);

        MvcResult result = mockMvc.perform(get("/contacts"))
        .andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andDo(print()).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseAsString=mockResponse.getContentAsString();
        assertTrue(responseAsString.contains("contactId"));
	}
	
	@Test
	public void testGetAllContactsWithEmptyList() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
        when(contactsServiceMocks.findAllContacts()).thenReturn(contList);

        MvcResult result = mockMvc.perform(get("/contacts"))
        .andExpect(status().isNoContent())
		.andDo(print()).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseAsString=mockResponse.getContentAsString();
        assertFalse(responseAsString.contains("contactId"));
	}
	
	@Test
	public void testGetAllContactsByPhone() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
		contList.add(contact);
        when(contactsServiceMocks.findContactsByPhone(anyString())).thenReturn(contList);

        MvcResult result = mockMvc.perform(get("/contacts?phoneNumber=1123451234"))
		.andExpect(status().isOk())
        .andDo(print()).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseAsString=mockResponse.getContentAsString();
        assertTrue(responseAsString.contains("1123451234"));
	}
	
	@Test
	public void testGetAllContactsByEmail() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
		contList.add(contact);
        when(contactsServiceMocks.findContactsByEmail(anyString())).thenReturn(contList);

        MvcResult result = mockMvc.perform(get("/contacts?email=abc@gmail.com"))
        		.andExpect(status().isOk())
        		.andDo(print()).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseAsString=mockResponse.getContentAsString();
        assertTrue(responseAsString.contains("abc@gmail.com"));
	}
	
	@Test
	public void testGetAllContactsByEmailAndPhone() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
		contList.add(contact);
        when(contactsServiceMocks.findContactsByEmailAndPhone(anyString(), anyString())).thenReturn(contList);

        MvcResult result = mockMvc.perform(get("/contacts?phoneNumber=1123451234&email=abc@gmail.com"))
        		.andExpect(status().isOk())
        		.andDo(print()).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseAsString=mockResponse.getContentAsString();
        assertTrue(responseAsString.contains("abc@gmail.com"));
        assertTrue(responseAsString.contains("1123451234"));

	}
	
	@Test
	public void testGetAllContactsByState() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
		contList.add(setupContact2());
		contList.add(contact);
        when(contactsServiceMocks.findContactsByState(anyString())).thenReturn(contList);

        MvcResult result = mockMvc.perform(get("/contacts/state/{state}", "Illinois"))
        		.andExpect(status().isOk())
        		.andDo(print()).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseAsString=mockResponse.getContentAsString();
        assertTrue(mockResponse.getStatus() == 200);
        assertTrue(responseAsString.contains("Illinois"));

	}
	
	@Test
	public void testGetAllContactsByStateWithEmptyReturnList() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
        when(contactsServiceMocks.findContactsByState(anyString())).thenReturn(contList);

        mockMvc.perform(get("/contacts/state/{state}", "Texas"))
        		.andExpect(status().isNoContent())
        		.andDo(print()).andReturn();
	}
	
	@Test
	public void testGetAllContactsByCity() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
		contList.add(setupContact2());
		contList.add(contact);
        when(contactsServiceMocks.findContactsByCity(anyString())).thenReturn(contList);

        MvcResult result = mockMvc.perform(get("/contacts/city/{city}", "Evanston"))
        		.andExpect(status().isOk())
        		.andDo(print()).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseAsString=mockResponse.getContentAsString();
        assertTrue(responseAsString.contains("Evanston"));

	}
	
	@Test
	public void testGetAllContactsByCityWithEmptyReturnList() throws Exception{
		List<Contacts> contList = new ArrayList<Contacts>();
        when(contactsServiceMocks.findContactsByCity(anyString())).thenReturn(contList);

        mockMvc.perform(get("/contacts/city/{city}", "SugarLand"))
        		.andExpect(status().isNoContent())
        		.andDo(print()).andReturn();
	}
	

    @Test
    public void testDeleteContact() throws Exception {

        when(contactsServiceMocks.findByContactId(anyLong())).thenReturn(contact);
        doNothing().when(contactsServiceMocks).deleteContact(contact);

        mockMvc.perform(delete("/contacts/{contactId}", (long) 1))
                .andExpect(status().isOk());

        verify(contactsServiceMocks, times(1)).findByContactId(anyLong());
        verify(contactsServiceMocks, times(1)).deleteContact(any());
        verifyNoMoreInteractions(contactsServiceMocks);
    }
    
    @Test
    public void testDeleteContactThatDoesNotExists() throws Exception {
        when(contactsServiceMocks.findByContactId(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/contacts/{contactId}", (long) 2))
                .andExpect(status().isNotFound());

        verify(contactsServiceMocks, times(1)).findByContactId(anyLong());
        
    }
    
    @Test
    public void testCreateContact() throws Exception {
        
        String jsonString = "{\"firstName\": \"Santy\", \"lastName\": \"Subbu\", \"company\": \"ABC\", \"email\": \"santhosh@gmail.com\", \"personalPhone\": \"2224561211\", \"workPhone\": \"\", \"dob\": \"1991-02-15\", \"addressLine1\": \"518 Lee St.\", \"city\": \"Evanston\", \"state\": \"Illinois\", \"country\": \"US\", \"zip\": \"60202\" }";
        
        MockMultipartFile imgFile = new MockMultipartFile("file", "img.png", "image/png", "some image".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("contact", "contact.json", "", jsonString.getBytes());

    	
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/contacts")
                .file(imgFile)
                .file(jsonFile))
        		.andExpect(status().is(201))
        		.andDo(print());
    }
    
    @Test
    public void testCreateContactInvalidPhone() throws Exception {
        
        String jsonString = "{\"firstName\": \"Santy\", \"lastName\": \"Subbu\", \"company\": \"ABC\", \"email\": \"santhosh@gmail.com\", \"personalPhone\": \"222-456-1211\", \"workPhone\": \"\", \"dob\": \"1991-02-15\", \"addressLine1\": \"518 Lee St.\", \"city\": \"Evanston\", \"state\": \"Illinois\", \"country\": \"US\", \"zip\": \"60202\" }";

        MockMultipartFile imgFile = new MockMultipartFile("file", "img.png", "image/png", "some image".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("contact", "contact.json", "", jsonString.getBytes());

    	
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/contacts")
                .file(imgFile)
                .file(jsonFile))
        		.andExpect(status().is(400))
        		.andDo(print());
    }
    
    @Test
    public void testCreateContactInvalidWorkPhone() throws Exception {
        
        String jsonString = "{\"firstName\": \"Santy\", \"lastName\": \"Subbu\", \"company\": \"ABC\", \"email\": \"santhosh@gmail.com\", \"personalPhone\": \"\", \"workPhone\": \"223-455-667\", \"dob\": \"1991-02-15\", \"addressLine1\": \"518 Lee St.\", \"city\": \"Evanston\", \"state\": \"Illinois\", \"country\": \"US\", \"zip\": \"60202\" }";

        MockMultipartFile imgFile = new MockMultipartFile("file", "img.png", "image/png", "some image".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("contact", "contact.json", "", jsonString.getBytes());

    	
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/contacts")
                .file(imgFile)
                .file(jsonFile))
        		.andExpect(status().is(400))
        		.andDo(print());
    }
    
    @Test
    public void testUpdateContactNullContent() throws Exception {
        
        String jsonString = "{\"firstName\": \"Santy\", \"lastName\": \"Subbu\", \"company\": \"ABC\", \"email\": \"santhosh@gmail.com\", \"personalPhone\": \"2224561211\", \"workPhone\": \"\", \"dob\": \"1991-02-15\", \"addressLine1\": \"518 Lee St.\", \"city\": \"Evanston\", \"state\": \"Illinois\", \"country\": \"US\", \"zip\": \"60202\" }";

        MockMultipartFile imgFile = new MockMultipartFile("file", "img.png", "image/png", "some image".getBytes());
       MockMultipartFile jsonFile = new MockMultipartFile("contact", "contact.json", "", jsonString.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/contacts/{contactId}", (long)1);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                .file(imgFile)
                .file(jsonFile))
                .andExpect(status().isNotFound()).andDo(print());
    }
    
    @Test
    public void testUpdateContact() throws Exception {
        
        String jsonString = "{\"firstName\": \"Santy\", \"lastName\": \"Subbu\", \"company\": \"ABC\", \"email\": \"santhosh@gmail.com\", \"personalPhone\": \"2224561211\", \"workPhone\": \"\", \"dob\": \"1991-02-15\", \"addressLine1\": \"518 Lee St.\", \"city\": \"Evanston\", \"state\": \"Illinois\", \"country\": \"US\", \"zip\": \"60202\" }";

        MockMultipartFile imgFile = new MockMultipartFile("file", "img.png", "image/png", "some image".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("contact", "contact.json", "", jsonString.getBytes());

        when(contactsServiceMocks.findByContactId((long)1)).thenReturn(contact);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("/contacts/{contactId}", (long)1);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                .file(imgFile)
                .file(jsonFile))
                .andExpect(status().isOk()).andDo(print());
        
        verify(contactsServiceMocks, times(1)).findByContactId((long)1);

    }
    
    @Test
    public void testUpdateContactInvalidPhone() throws Exception {
        
        String jsonString = "{\"firstName\": \"Santy\", \"lastName\": \"Subbu\", \"company\": \"ABC\", \"email\": \"santhosh@gmail.com\", \"personalPhone\": \"222-456-1211\", \"workPhone\": \"\", \"dob\": \"1991-02-15\", \"addressLine1\": \"518 Lee St.\", \"city\": \"Evanston\", \"state\": \"Illinois\", \"country\": \"US\", \"zip\": \"60202\" }";

        MockMultipartFile imgFile = new MockMultipartFile("file", "img.png", "image/png", "some image".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("contact", "contact.json", "", jsonString.getBytes());

        when(contactsServiceMocks.findByContactId((long)1)).thenReturn(contact);
        
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("/contacts/{contactId}", (long)1);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });    	
        mockMvc.perform(builder
                .file(imgFile)
                .file(jsonFile))
        		.andExpect(status().is(400))
        		.andDo(print()).andReturn();
        
        verify(contactsServiceMocks, times(1)).findByContactId((long)1);
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

}
