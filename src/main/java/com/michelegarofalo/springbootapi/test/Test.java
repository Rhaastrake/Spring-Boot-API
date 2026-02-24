package com.michelegarofalo.springbootapi.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin ("*")

@RestController
@RequestMapping ("test")
public class Test {

    @GetMapping ("printTest")
    public static String printTest() {

        System.out.println(">> printTest method called");

        return "Print Test Result";
    }
}
