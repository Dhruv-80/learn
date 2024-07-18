package com.mongo.mongo.service;
import com.mongo.mongo.repository.YourModelRepository;
import com.mongo.mongo.model.YourModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YourModelService {

    @Autowired
    private YourModelRepository repository;

    public YourModel create(YourModel model) {
        return repository.save(model);
    }

    public List<YourModel> getAll() {
        return repository.findAll();
    }

    public Optional<YourModel> getById(String id) {
        return repository.findById(id);
    }

    public YourModel update(String id, YourModel model) {
        Optional<YourModel> existingModel = repository.findById(id);
        if (existingModel.isPresent()) {
            YourModel updatedModel = existingModel.get();
            updatedModel.setName(model.getName());
            updatedModel.setAge(model.getAge());
            return repository.save(updatedModel);
        }
        return null;
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}