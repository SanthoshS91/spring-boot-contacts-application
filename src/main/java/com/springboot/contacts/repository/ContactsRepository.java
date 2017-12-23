/**
 * 
 */
package com.springboot.contacts.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.contacts.model.Contacts;

/**
 * @author santhosh
 *
 */
@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Serializable>{
		
	@Query(value="SELECT * FROM contacts c WHERE c.email = :email", nativeQuery=true)
	List<Contacts> findContactsByEmail(@Param("email") String email);
	
	@Query(value="SELECT * FROM contacts c WHERE (c.work_phone = :phoneNumber or c.personal_phone = :phoneNumber)", nativeQuery=true)
	List<Contacts> findContactsByPhone(@Param("phoneNumber") String phoneNumber);
	
	@Query(value="SELECT * FROM contacts c WHERE (c.email = :email and (c.work_phone = :phoneNumber or c.personal_phone = :phoneNumber))", nativeQuery=true)
	List<Contacts> findContactsByEmailAndPhone(String email, String phoneNumber);

	@Query(value="SELECT * FROM contacts c WHERE c.state = :state", nativeQuery=true)
	List<Contacts> findContactsByState(@Param("state") String state);

	@Query(value="SELECT * FROM contacts c WHERE c.city = :city", nativeQuery=true)
	List<Contacts> findContactsByCity(@Param("city") String city);

}
