package com.elkassimi.book.controller;

import com.elkassimi.book.entity.Author;
import com.elkassimi.book.service.AuthorServ;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorControl {

    private final AuthorServ authorServ;

    public AuthorControl(AuthorServ authorServ) {
        this.authorServ = authorServ;
    }

    @GetMapping("/{id}")
    public Author findById(@PathVariable Long id) {
        return authorServ.findById(id);
    }

    @PostMapping
    public Author insert(Author author) {
        return authorServ.insert(author);
    }

    @PutMapping
    public Author update(Author author) {
        return authorServ.update(author);
    }

    @DeleteMapping
    public void delete(Author author) {
        authorServ.delete(author);
    }

    @GetMapping
    public List<Author> findAll() {
        return authorServ.findAll();
    }
}
