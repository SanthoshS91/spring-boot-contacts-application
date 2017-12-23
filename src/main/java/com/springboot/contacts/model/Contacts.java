/**
 * 
 */
package com.springboot.contacts.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author santhosh
 *
 */
@Entity
@Table(name="contacts")
public class Contacts implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7997610759692707624L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(updatable=false, nullable=false)
	private long contactId;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Column
	private String company;
	
	@Column
	private String email;
	
	@Column
	private Date dob;
	
	@Column(name="work_phone", length=10)
	private String workPhone;
	
	@Column(name="personal_phone", length=10)
	private String personalPhone;
	
	@Column
	private String fileName;
	
	@Lob
	@Column
	private byte[] image;
	
	@Column
	private String contentType;
	
	@Column
	private String addressLine1;
	
	@Column
	private String city;
	
	@Column
	private String state;
	
	@Column
	private String country;
	
	@Column
	private String zip;

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getPersonalPhone() {
		return personalPhone;
	}

	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String toString(){
	    return "Contacts(contactId=" + getContactId() + ", "
	    		+ "firstName=" + getFirstName() + ", "
	    		+ "lastName=" + getLastName() + ", "
	    		+ "company=" + getCompany() + ", "
	    		+ "email=" + getEmail() + ", "
	    		+ "dob=" + getDob() + ", "
	    		+ "email=" + getEmail() + ", "
	    		+ "workPhone=" + getWorkPhone() + ", "
	    		+ "personalPhone=" + getPersonalPhone() + ", "
	    		+ "addressLine1=" + getAddressLine1() + ", "
	    		+ "city=" + getCity() + ", "
	    		+ "state=" + getState() + ", "
	    		+ "country=" + getCountry() + ", "
	    		+ "zip=" + getZip() + ", "
	    		+ ")";
	  }

	public Object canEqual(Contacts contact) {
		return contact instanceof Contacts;
	}
	
}
