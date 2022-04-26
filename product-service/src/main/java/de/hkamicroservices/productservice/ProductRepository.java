package de.hkamicroservices.productservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query("from Product p where p.categoryId=:categoryId")
    Iterable<Product> getProductsByCategoryId(int categoryId);
}
