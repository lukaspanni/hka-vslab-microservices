package de.hkamicroservices.categoryservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path="/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository repository){
        categoryRepository = repository;
    }

    @GetMapping(path="/")
    public Iterable<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @GetMapping(path="/{id}", produces = "application/json")
    public Category getCategory(@PathVariable Long id){
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @PostMapping(path="/", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category){
        return categoryRepository.save(category);
    }

    @DeleteMapping(path="/{id}", consumes = "application/json")
    public void deleteCategory(@PathVariable Long id){
        if(!categoryRepository.existsById(id)) throw new CategoryNotFoundException();
        categoryRepository.deleteById(id);
    }
}
