package org.mambofish.spring.data.jsondb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * @author vince
 */
@NoRepositoryBean
public interface JsonDBRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, CrudRepository<T, ID> {

}
