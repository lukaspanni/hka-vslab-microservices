package de.hkamicroservices.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;


@RestController()
@RequestMapping(path = "/products")
public class ProductController {

    private final String categoryServiceEndpoint = Objects.equals(System.getenv("CATEGORY_ENDPOINT"), "") ? System.getenv("CATEGORY_ENDPOINT") : "localhost";
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository repository) {
        productRepository = repository;
    }

    @GetMapping(path = "/")
    public Iterable<Product> getAllProducts(@RequestParam(required = false) Integer categoryId) {
        if(categoryId == null)
            return productRepository.findAll();
        return productRepository.getProductsByCategoryId(categoryId);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @PostMapping(path = "/", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        if (product.getCategoryId() != 0 && !checkCategoryExists(product.getCategoryId()))
            return ResponseEntity.badRequest().body("Category does not exist");


        var createdProduct = productRepository.save(product);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdProduct.getId()).toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    private boolean checkCategoryExists(int categoryId) {
        WebClient client = WebClient.create("http://" + categoryServiceEndpoint + ":8080/categories/");
        try {
            client.get().uri(String.valueOf(categoryId)).retrieve().bodyToMono(CategoryDto.class).block();
        } catch (WebClientResponseException wcre) {
            if (wcre.getStatusCode().equals(HttpStatus.NOT_FOUND))
                return false;
            throw wcre;
        }
        return true;
    }

    @DeleteMapping(path = "/{id}")
    public void deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) throw new ProductNotFoundException();
        productRepository.deleteById(id);
    }
}
