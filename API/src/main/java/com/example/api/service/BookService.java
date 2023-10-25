package com.example.api.service;

import com.example.api.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Page<Book> findAllByDeletedFalse(Pageable pageable);
    List<Book> findAll(Specification<Book> spec);

    List<Book> findAllByDeletedFalse();
    Optional<Book> findAllByIdAndDeletedFalse(Long id);

    void deleted(Book book);
    void update(Book book);
    void add(Book book);
}
