package com.example.api.resource.publicResource;

import com.example.api.model.entity.Book;
import com.example.api.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/book")

public class BookResource {
    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/lists")
    public ResponseEntity<?> listsBook(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "6") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage = bookService.findAllByDeletedFalse(pageable);

        if (!booksPage.hasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(booksPage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(required = false, name = "q") Optional<String> query) {

        List<Specification<Book>> specs = new ArrayList<>();

        if (query.isPresent() && !query.get().isEmpty()) {
            String search = "%" + query.get() + "%";

            specs.add((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), search),
                    criteriaBuilder.like(root.get("category"), search),
                    criteriaBuilder.like(root.get("author"), search)
            ));


            try {
                int publishYear = Integer.parseInt(query.get());
                specs.add((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("publishYear"), publishYear));
            } catch (NumberFormatException e) {
                // Do nothing, it's not a year
            }
        }

        Specification<Book> combinedSpec = specs.stream().reduce(Specification::and).orElse(null);

        if (combinedSpec == null) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK); // Return empty list if no criteria
        }

        List<Book> books = bookService.findAll(combinedSpec);

        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
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


}
