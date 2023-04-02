package alken1t.javaparsingspring.repository;

import alken1t.javaparsingspring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByLinkProduct(String linkProduct);
}