package org.example.bizkit.Repository;

import org.example.bizkit.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findProductById(Integer id);
    List<Product> findProductsByProviderId(Integer providerId);

}
