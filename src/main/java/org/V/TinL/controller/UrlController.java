package org.V.TinL.controller;

import lombok.extern.slf4j.Slf4j;
import org.V.TinL.service.TransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UrlController {

    // == fields ==
    private final TransformService transformService;

    // == constructors ==
    @Autowired
    public UrlController(TransformService transformService){
        this.transformService = transformService;
    }

    @GetMapping("/welcome")
    public String welcome(){
        System.out.println("Welcome Message");
        return "Welcome to TinL!";
    }

    @PostMapping("/transform2short")
    public String transformToShort(@RequestBody String longUrl){
        return transformService.longToShort(longUrl);
    }

    @PostMapping("/transform2long")
    public String transformToLong(@RequestBody String shortUrl){
        return transformService.shortToLong(shortUrl);
    }
}
