package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends CrudRepository<T, ID>, JpaSpecificationExecutor<T>,
        PagingAndSortingRepository<T, ID> {
}
