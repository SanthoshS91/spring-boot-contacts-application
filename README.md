# Java Spring-boot Coding - Santhosh Subramanian #

Objective
=========
Develop a RESTful API that would allow a web or mobile front-end to:
		● Create a contact record
		● Retrieve a contact record
		● Update a contact record
		● Delete a contact record
		● Search for a record by email or phone number
		● Retrieve all records from the same state or city
The contact record should represent the following information: name, company, profile image, email,
birthdate, phone number (work, personal) and address .
Also please provide a unit test for at least one of the endpoints you create.

Classes
===========
1. ContactsController.java - Controller for all the above contact record functionalities
2. ContacsService.java - Service layer to call the respective Repositories
3. All the classes under the test package(src.test.java) - Junit/Spring Integration Test Cases
4. ContactsRepository.java - Added methods for searching for a record by email or phone number and retrieving all records from the same state or city
5. Contacts.java - Model class for creating, retrieving, updating, and deleting a contact record.
6. ContactCustomError.java - Custom error message for invalid work/personal phone
7. ContactNotFoundException.java - "Contact(s) does not exist in the database" message for invalid retrive contact urls

Assumptions
============
1. Work and Personal PhoneNumbers should not contain any special charaters and should be numeric.
2. I have not considered extension for work and personal phone numbers.
3. Image upload - Any type of document can be uploaded in my current solution. I have not restricted to specific image types

How to compile and run the application?
======================================
1. Import the package from the GoogleDrive/Dropbox.
2. Goto the repository path /contacts/ (one that has the pom.xml) from the command line.
3. Use "mvn:pacakge" to create an executable jar.
4. using the "mvn spring-boot:run" command to run the application.

OR

==
            
1. Import the package from the git.
2. Goto the repository path /contacts/ from the command line.
3. Give the following commands
	a. mvn clean install
4. Import the workspace in STS.
5. Right click on the contacts application and run as spring-boot

Requirements
============
### Task ###
Develop a RESTful API that would allow a web or mobile front-end to:

#### Criteria 1: Create a contact record. ####
Use the below url to run this criteria

POST: http://localhost:8080/contacts

RequestParam:
(Text) contact: {"firstName": "Santy", "lastName": "Subbu", "company": "ABC", "email": "santhosh@gmail.com", "personalPhone": "2224561211", "workPhone": "", "dob": "1991-02-15", "addressLine1": "518 Lee St.", "city": "Evanston", "state": "Illinois", "country": "US", "zip": "60202" } 
(file) file: ImageFile.png

#### Criteria 2: Retrieve a contact record. ####
Use the below url to run this criteria

GET: http://localhost:8080/contacts/{contactId}

#### Criteria 3: Update a contact record. ####
Use the below url to run this criteria

PUT: http://localhost:8080/contacts/{contactId}

RequestParam:
(Text) contact: {"firstName": "Santy", "lastName": "Subramanian", "company": "ABC", "email": "santhosh@gmail.com", "personalPhone": "2224561211", "workPhone": "", "dob": "1991-02-15", "addressLine1": "518 Lee St.", "city": "Evanston", "state": "Illinois", "country": "US", "zip": "60202" } 
(file) file: ImageFile.png

Need to send all the field names as part of the request even if we are modifying one field.

#### Criteria 4: Delete a contact record. ####
Use the below url to run this criteria

DELETE: http://localhost:8080/contacts/{contactId}

#### Criteria 5: Search for a record by email or phone number. ####
Use the below url to run this criteria

GET: http://localhost:8080/contacts - Retrieves all the stored contacts.

GET: http://localhost:8080/contacts?email=santhosh@gmail.com - search for a specific contact based on the given email.

GET: http://localhost:8080/contacts?phoneNumber=2224561211 - search for a specific contact (on both personal and work phone number) based on the given phone number.

GET: http://localhost:8080/contacts?phoneNumber=2224561211&email=santhosh@gmail.com - search for a specific contact based on the given phone number and email.

#### Criteria 6: Retrieve all records from the same state. ####
Use the below url to run this criteria

GET: http://localhost:8080/contacts/state/{state}

#### Criteria 7: Retrieve all records from the same city. ####
Use the below url to run this criteria

GET: http://localhost:8080/contacts/city/{city}


How to run the suite of automated tests?
========================================
1. Import the workspace in STS 
2. Goto the test package (src.test.java)
3. Right click on the pacakge and "Run as Junit cases".
4. Install EClEmma Plugin from Eclipse MarketPlace.
5. Right click on the same package and select "coverage as junit" to view the overall coverage.


Technical Information
=====================
 * Java 8.x
 * Maven 3.x
 * Spring Framework 4.x
 * Spring Boot 1.5.6
 * Hibernate
 * JPA
 * H2 database
 * JUnit 4.x
 * Mockito 2.x
 * Spring Integration Tests
 

Attachments
===========
 contact.json and image file.
 
 
Deployment steps for Cloud Foundry
==================================
 1. Copy the attached manifest.yml to the contacts project folder(/contacts) (that has pom.xml).
 2. Open Cloud Foundry console using cf-cli and navigate to the contacts project folder (that has pom.xml and manifest.yml).
 3. Login to an account using "cf login -a https://api.run.pivotal.io" with the user's email and password. Select the space where you want to deploy your application in Cloud Foundry.
 4. Do "mvn pacakage" on the project folder and get the latest jar, if you don't have it.
 5. Use "cf push" that will use the manifest.yml file to deploy the application in Cloud Foundry.
 6. You can see the instance running from the attached PivotalCloudDeployment.png screenshot.
 7. Use the below urls, which is same as above except for the domain name, to execute the necessary functionalities.
 	POST: https://spring-boot-coding.cfapps.io/contacts/
 	GET: https://spring-boot-coding.cfapps.io/contacts/{contactId}
 	GET: https://spring-boot-coding.cfapps.io/contacts/
 	GET: https://spring-boot-coding.cfapps.io/contacts?email={emailId}
 	GET: https://spring-boot-coding.cfapps.io/contacts?phoneNumber{phoneNumber}
 	PUT: https://spring-boot-coding.cfapps.io/contacts/{contactId}
 	DELETE: https://spring-boot-coding.cfapps.io/contacts/{contactId}

Use the below paramters for POST and PUT call.
 	
RequestParam:
(Text) contact: {"firstName": "Santy", "lastName": "Subramanian", "company": "ABC", "email": "santhosh@gmail.com", "personalPhone": "2224561211", "workPhone": "", "dob": "1991-02-15", "addressLine1": "518 Lee St.", "city": "Evanston", "state": "Illinois", "country": "US", "zip": "60202" } 
(file) file: ImageFile.png	