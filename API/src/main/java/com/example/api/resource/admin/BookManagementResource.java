package com.example.api.resource.admin;

import com.example.api.model.entity.Book;
import com.example.api.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/admin/book")
@PreAuthorize("hasRole('ADMIN')")

public class BookManagementResource {
    private final BookService bookService;

    public BookManagementResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> listBook(@RequestParam(defaultValue = "0")int page,
                                      @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookService.findAllByDeletedFalse(pageable);

        if (!bookPage.hasContent()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookPage);
    }
    @PostMapping("/create")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        bookService.add(book);
        return ResponseEntity.ok(book);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Book> viewBookDetail(@PathVariable Long id){
        Optional<Book> bookOpt = bookService.findAllByIdAndDeletedFalse(id);
        if(bookOpt.isPresent()){
            return ResponseEntity.ok(bookOpt.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updateBook){
        Optional<Book> bookOpt = bookService.findAllByIdAndDeletedFalse(id);
        if(bookOpt.isPresent()){
            Book book = bookOpt.get();
            book.setName(updateBook.getName());
            book.setCategory(updateBook.getCategory());
            book.setAuthor(updateBook.getAuthor());
            book.setPublishYear(updateBook.getPublishYear());
            book.setImage(updateBook.getImage());
            book.setContent(updateBook.getContent());
            bookService.update(book);
            return ResponseEntity.ok(book);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Book> deletedBook(@PathVariable Long id){
        Optional<Book> bookOpt = bookService.findAllByIdAndDeletedFalse(id);
        if(bookOpt.isPresent()){
            Book book = bookOpt.get();
            book.setDeleted(true);
            bookService.deleted(book);
            return ResponseEntity.ok(book);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
