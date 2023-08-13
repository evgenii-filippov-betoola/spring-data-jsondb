package org.mambofish.spring.data.jsondb.repository;

import io.jsondb.JsonDBTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author vince
 */

@Repository
public class JsonDBRepositoryImpl<T, ID extends Serializable> implements JsonDBRepository<T, ID> {

    private final JsonDBTemplate template;
    private final Class<T> clazz;

    public JsonDBRepositoryImpl(Class<T> domainClass, JsonDBTemplate template) {
        this.clazz = domainClass;
        this.template = template;

        if (!template.collectionExists(clazz)) {
            template.createCollection(clazz);
        }
    }

    @Override
    public <S extends T> S save(S s) {
        template.upsert(s);
        return s;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(this::save);
        return iterable;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(template.findById(id, clazz));
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public Iterable<T> findAll() {
        return template.findAll(clazz);
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> iterable) {
        List<T> results = new ArrayList<>();
        iterable.forEach(ID -> {
            findById(ID)
                    .ifPresent(results::add);
        });
        return results;
    }

    @Override
    public long count() {
        return template.findAll(clazz).size();
    }

    @Override
    public void deleteById(ID id) {
        findById(id)
                .ifPresent(this::delete);
    }

    @Override
    public void delete(T t) {
        template.remove(t, t.getClass());
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends T> iterable) {
        iterable.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        deleteAll(findAll());
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        throw new RuntimeException("Sort not yet implemented");
    }

    @Override
    public Page<T> findAll(Pageable pageable) {

        List<T> results = template.findAll(clazz);

        int pageSize = pageable.getPageSize();
        long pageOffset = pageable.getOffset();
        long total = pageOffset + results.size() + (results.size() == pageSize ? pageSize : 0);

        return new PageImpl<>(results, pageable, total);
    }
}
