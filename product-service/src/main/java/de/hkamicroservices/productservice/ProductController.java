package de.hkamicroservices.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController()
@RequestMapping(path="/categories")
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository repository){
        productRepository = repository;
    }

    @GetMapping(path="/")
    public Iterable<Product> getAllCategories(){
        return productRepository.findAll();
    }

    @GetMapping(path="/{id}", produces = "application/json")
    public Product getProduct(@PathVariable Long id){
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @PostMapping(path="/", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addProduct(@RequestBody Product product){
        var createdProduct =  productRepository.save(product);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdProduct.getId()).toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    @DeleteMapping(path="/{id}", consumes = "application/json")
    public void deleteProduct(@PathVariable Long id){
        if(!productRepository.existsById(id)) throw new ProductNotFoundException();
        productRepository.deleteById(id);
    }
}
