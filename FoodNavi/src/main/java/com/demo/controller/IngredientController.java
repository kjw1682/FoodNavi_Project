package com.demo.controller;

import com.demo.domain.Ingredient;
import com.demo.persistence.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping("/ingredients/search")
    public List<String> searchIngredients(@RequestParam("term") String term) {
        return ingredientRepository.findByNameContainingIgnoreCase(term)
                .stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

}