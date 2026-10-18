package com.elkassimi.book.service;

import com.elkassimi.book.entity.Author;
import com.elkassimi.book.repository.AuthorRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServ {

    private final AuthorRepo authorRepo;

    public AuthorServ(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public Author findById(Long id) {
        return authorRepo.findById(id).orElseThrow();
    }

    public Author insert(Author author) {
        return authorRepo.save(author);
    }

    public List<Author> insert(List<Author> authors) {
        return authorRepo.saveAll(authors);
    }

    public Author update(Author author) {return authorRepo.save(author);}

    public void delete(Author author) {authorRepo.delete(author);}

    public List<Author> findAll() {return authorRepo.findAll();}

}
