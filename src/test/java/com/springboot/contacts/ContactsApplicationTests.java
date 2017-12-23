package com.springboot.contacts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.contacts.ContactsApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactsApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
    public void test()
    {
		ContactsApplication.main(new String[]{
                "--spring.main.web-environment=true",
        });
    }
}
