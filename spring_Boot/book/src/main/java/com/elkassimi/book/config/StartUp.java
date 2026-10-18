package com.elkassimi.book.config;

import com.elkassimi.book.entity.Author;
import com.elkassimi.book.entity.Book;
import com.elkassimi.book.service.AuthorServ;
import com.elkassimi.book.service.BookServ;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StartUp implements CommandLineRunner {
    private final AuthorServ authorServ;
    private final BookServ bookServ;

    public StartUp(AuthorServ authorServ, BookServ bookServ) {
        this.authorServ = authorServ;
        this.bookServ = bookServ;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {

//        Book book = bookServ.findById(1L);
//        System.out.println(book.getId());
//        Author author = book.getAuthor();
////        author.getName();
//        System.out.println(author.getName());


//        Author author1 = new Author(null,"amin");
//        Author author2 = new Author(null,"ayoub");
//        Author author3 = new Author(null,"youssef");
//
//        authorServ.insert(Arrays.asList(author1,author2,author3));
//
//
//        Book book1 = new Book( null,"java JPA", 200.0,author1);
//        Book book2 = new Book( null,"spring boot ", 201.0,author2);
//        Book book3 = new Book( null,"spring", 202.0,author3);
//
//        bookServ.insert(Arrays.asList(book1,book2,book3));
    }
}
