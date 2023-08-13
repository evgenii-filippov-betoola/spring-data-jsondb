package org.mambofish.spring.data.jsondb.repository.test;

import org.mambofish.spring.data.jsondb.repository.JsonDBRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author vince
 */
public interface ContactRepository extends JsonDBRepository<Contact, String> {
    Optional<Contact> findByName(String name);
    List<Contact> findAllByName(String name);
}
