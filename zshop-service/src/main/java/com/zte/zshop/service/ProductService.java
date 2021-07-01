package com.zte.zshop.service;

import com.github.pagehelper.PageInfo;
import com.zte.zshop.cart.ShoppingCart;
import com.zte.zshop.dto.ProductDto;
import com.zte.zshop.entity.Product;
import com.zte.zshop.params.ProductParams;
import org.apache.commons.fileupload.FileUploadException;

import java.io.OutputStream;
import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-07 14:30
 * Description:<描述>
 */
public interface ProductService {
    public void add(ProductDto productDto);

    public boolean checkName(String name);

    public PageInfo<Product> findAll(Integer pageNum, int pageSize);

    public Product findById(Integer id);

    public void modifyProduct(ProductDto productDto)throws FileUploadException;

    public void removeProduct(Integer id);

    public void getImage(String path, OutputStream out);

    public List<Product> findByParams(ProductParams productParams);

    public boolean addToCart(int id, ShoppingCart sc);

    public void removeItemFromShoppingCart(ShoppingCart sc, int id);

    public void modifyItemQuantity(ShoppingCart sc, int id, int quantity);
}
