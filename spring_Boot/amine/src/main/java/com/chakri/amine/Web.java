package com.chakri.amine;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Web {

    @PostMapping("/person")
    public List<Person> getPerson(){
        return List.of(new Person("q","w","e"),new Person("q","w","e"),new Person("q","w","e"));
    }



}
