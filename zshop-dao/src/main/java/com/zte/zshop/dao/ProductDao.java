package com.zte.zshop.dao;

import com.zte.zshop.entity.Product;
import com.zte.zshop.params.ProductParams;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-07 14:58
 * Description:<描述>
 */
public interface ProductDao {
    public void insert(Product product);

    public Product selectByName(String name);

    public List<Product> selectAll();

    public Product selectById(Integer id);

    public void update(Product product);

    public void deleteById(Integer id);


    public List<Product> selectByParams(ProductParams productParams);
}
