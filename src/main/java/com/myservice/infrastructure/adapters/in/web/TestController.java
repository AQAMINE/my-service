package com.myservice.infrastructure.adapters.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from public endpoint";
    }

    @GetMapping("/private/hello")
    public String privateHello() {
        return "Hello from private endpoint";
    }
}
