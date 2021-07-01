package com.zte.zshop.service;

import com.github.pagehelper.PageInfo;
import com.zte.zshop.entity.ProductType;
import com.zte.zshop.exception.ProductTypeExistsException;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-01 11:03
 * Description:<描述>
 */
public interface ProductTypeService {

    //查询所有商品类型信息
    public PageInfo<ProductType> findAll(Integer page, Integer rows);

    public void add(String productTypeName)throws ProductTypeExistsException;

    public ProductType findById(Integer id);

    public void modifyName(Integer id, String name)throws ProductTypeExistsException;

    public void removeById(Integer id);

    public void modifyStatus(Integer id);

    public List<ProductType> findByEnable(int productTypeEnable);
}
