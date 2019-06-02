package com.majie.dto;

import com.majie.entity.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDTO extends Product {

    private String highlighted;
}
