package com.zte.zshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.zshop.constants.Constant;
import com.zte.zshop.dao.ProductTypeDao;
import com.zte.zshop.entity.ProductType;
import com.zte.zshop.exception.ProductTypeExistsException;
import com.zte.zshop.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-01 11:04
 * Description:<描述>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeDao productTypeDao;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public PageInfo<ProductType> findAll(Integer page,Integer rows) {
        PageHelper.startPage(page,rows);
        List<ProductType> productTypes = productTypeDao.selectAll();
        PageInfo<ProductType> pageInfo = new PageInfo<>(productTypes);


        return pageInfo;
    }

    @Override
    public void add(String name) throws ProductTypeExistsException {

        ProductType productType=productTypeDao.selectByName(name);
        if(productType!=null){
            throw  new ProductTypeExistsException("商品类型已经存在");
        }
        productTypeDao.insert(name, Constant.PRODUCT_TYPE_ENABLE);

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public ProductType findById(Integer id) {
        return productTypeDao.selectById(id);
    }

    @Override
    public void modifyName(Integer id, String name) throws ProductTypeExistsException {
        ProductType productType=productTypeDao.selectByName(name);
        if(productType!=null){
            throw  new ProductTypeExistsException("商品类型已经存在");
        }
        productTypeDao.updateName(id,name);
    }

    @Override
    public void removeById(Integer id) {
        productTypeDao.deleteById(id);
    }

    @Override
    public void modifyStatus(Integer id) {
        ProductType productType = productTypeDao.selectById(id);
        Integer status = productType.getStatus();
        if(status==Constant.PRODUCT_TYPE_ENABLE){
            status=Constant.PRODUCT_TYPE_DISABLE;
        }
        else if(status==Constant.PRODUCT_TYPE_DISABLE){
            status=Constant.PRODUCT_TYPE_ENABLE;
        }

        productTypeDao.updateStatus(id,status);

    }

    @Override
    public List<ProductType> findByEnable(int status) {
        return productTypeDao.selectByStatus(status);
    }
}
