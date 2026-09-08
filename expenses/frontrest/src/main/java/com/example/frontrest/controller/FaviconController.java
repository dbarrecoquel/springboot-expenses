package com.example.frontrest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {
    @GetMapping("favicon.ico")
    public void returnNoFavicon() {
        // Renvoie un statut 200/204 vide sans déclencher le BasicErrorController
    }
}