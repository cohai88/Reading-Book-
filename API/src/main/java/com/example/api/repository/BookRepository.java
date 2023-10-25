package com.example.api.repository;

import com.example.api.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends BaseRepository<Book, Long>{
    Page<Book> findAllByDeletedFalse(Pageable pageable);

    List<Book> findAllByDeletedFalse();
    Optional<Book> findByIdAndDeletedFalse(Long id);


}
