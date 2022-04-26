package de.hkamicroservices.categoryservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


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
    public ResponseEntity<?> addCategory(@RequestBody Category category){
        var createdCategory =  categoryRepository.save(category);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCategory.getId()).toUri();
        return ResponseEntity.created(location).body(createdCategory);
    }

    @DeleteMapping(path="/{id}")
    public void deleteCategory(@PathVariable Long id){
        if(!categoryRepository.existsById(id)) throw new CategoryNotFoundException();
        categoryRepository.deleteById(id);
    }
}
