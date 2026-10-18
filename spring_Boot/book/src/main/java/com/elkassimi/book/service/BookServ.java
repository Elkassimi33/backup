package com.elkassimi.book.service;

import com.elkassimi.book.entity.Author;
import com.elkassimi.book.entity.Book;
import com.elkassimi.book.repository.BookRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServ {

    private final BookRepo bookRepo;

    public BookServ(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Transactional
    public Book findById(Long id) {
        return bookRepo.findById(id).orElseThrow();
    }

    public Book insert(Book book) {
        return bookRepo.save(book);
    }

    @Transactional
    public List<Book> insert(List<Book> book) {
        return bookRepo.saveAll(book);
    }

    public   List<Book> findAll(){
        return bookRepo.findAll();
    }

    public Book update(Book book) {
        return bookRepo.save(book);
    }

    public void delete(Book book) {
        bookRepo.delete(book);
    }
}
