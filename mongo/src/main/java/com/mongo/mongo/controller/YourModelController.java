package com.mongo.mongo.controller;

import com.mongo.mongo.model.YourModel;
import com.mongo.mongo.service.YourModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
public class YourModelController {

    @Autowired
    private YourModelService service;

    @PostMapping
    public YourModel create(@RequestBody YourModel model) {
        return service.create(model);
    }

    @GetMapping
    public List<YourModel> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<YourModel> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<YourModel> update(@PathVariable String id, @RequestBody YourModel model) {
        YourModel updatedModel = service.update(id, model);
        return updatedModel != null ? ResponseEntity.ok(updatedModel) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
