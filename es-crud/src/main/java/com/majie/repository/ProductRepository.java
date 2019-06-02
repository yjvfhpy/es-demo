package com.majie.repository;

import com.majie.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 */
public interface ProductRepository extends ElasticsearchCrudRepository<Product, String> {

    void deleteByName(String name);

    List<Product> findByName(String name);
}
