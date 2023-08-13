package org.mambofish.spring.data.jsondb.repository.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author vince
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =JsonDBRepositoryConfig.class)
public class RepositoryTest {

    @Autowired private ContactRepository contactRepository;

    @Test
    public void shouldSaveContact() {

        Contact contact = contactRepository.save(new Contact("jane"));
        assertEquals("jane", contact.getId());

    }

    @Test
    public void shouldFindContact() {

        contactRepository.save(new Contact("jane"));
        Contact contact = contactRepository.findById("jane").orElse(null);
        assertEquals("jane", contact.getId());

    }

    @Test
    public void shouldFindContactByName() {

        contactRepository.save(new Contact("1", "jane'name"));
        Contact contact = contactRepository.findByName("jane'name").orElse(null);
        assertEquals("1", contact.getId());

    }

    @Test
    public void shouldFindAllContactByName() {

        contactRepository.save(new Contact("1", "jane_name_list"));
        contactRepository.save(new Contact("2", "jane_name_list"));
        List<Contact> contacts = contactRepository.findAllByName("jane_name_list");
        assertEquals(2, contacts.size());

    }

    @Test
    public void shouldUpdateContact() {

        contactRepository.save(new Contact("jane"));

        Contact contact = contactRepository.findById("jane").orElse(null);
        contact.setName("jennifer");
        contactRepository.save(contact);


        contact = contactRepository.findById("jane").orElse(null);
        assertEquals("jennifer", contact.getName());

    }

    @Test
    public void shouldDeleteContact() {

        Contact contact = contactRepository.save(new Contact("jane"));
        contactRepository.delete(contact);
        assertFalse(contactRepository.existsById("jane"));
    }

    @Test
    public void shouldDeleteContactById() {

        Contact contact = contactRepository.save(new Contact("jane"));
        contactRepository.deleteById(contact.getId());
        assertFalse(contactRepository.existsById("jane"));
    }

    @Test
    public void shouldDeleteAll() {

        contactRepository.save(new Contact("jane"));
        contactRepository.save(new Contact( "pete"));

        assertTrue(contactRepository.existsById("jane"));
        assertTrue(contactRepository.existsById("pete"));

        contactRepository.deleteAll();

        assertFalse(contactRepository.existsById("jane"));
        assertFalse(contactRepository.existsById("pete"));

    }

}
