package com.example.api.service.impl;

import com.example.api.model.entity.Book;
import com.example.api.repository.BookRepository;
import com.example.api.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<Book> findAllByDeletedFalse(Pageable pageable) {
        return bookRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public List<Book> findAll(Specification<Book> spec) {
        return bookRepository.findAll(spec);
    }

    @Override
    public List<Book> findAllByDeletedFalse() {
        return bookRepository.findAllByDeletedFalse();
    }

    @Override
    public Optional<Book> findAllByIdAndDeletedFalse(Long id) {
        return bookRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public void deleted(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public void update(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void add(Book book) {
        bookRepository.save(book);
    }
}
