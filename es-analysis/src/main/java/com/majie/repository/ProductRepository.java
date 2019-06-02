package com.majie.repository;

import com.majie.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author majie
 * @date 2018/7/2.
 */
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
}
