package com.elkassimi.book.controller;


import com.elkassimi.book.entity.Book;
import com.elkassimi.book.repository.BookRepo;
import com.elkassimi.book.service.BookServ;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookControl {

    private final BookServ bookServ;

    public BookControl(BookServ bookServ) {
        this.bookServ = bookServ;
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable Long id) {
        return bookServ.findById(id);
    }

    @PostMapping
    public Book insert(Book book) {
        return bookServ.insert(book);
    }

    @PutMapping
    public Book update(Book book) {
        return bookServ.update(book);
    }

    @DeleteMapping
    public void delete(Book book) {
        bookServ.delete(book);
    }

    @GetMapping
    public List<Book> findAll() {
        return bookServ.findAll();
    }
}
