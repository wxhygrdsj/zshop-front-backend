package com.zte.zshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.zshop.cart.ShoppingCart;
import com.zte.zshop.dao.ProductDao;
import com.zte.zshop.dto.ProductDto;
import com.zte.zshop.entity.Product;
import com.zte.zshop.entity.ProductType;
import com.zte.zshop.ftp.FtpConfig;
import com.zte.zshop.ftp.FtpUtils;
import com.zte.zshop.params.ProductParams;
import com.zte.zshop.service.ProductService;
import com.zte.zshop.utils.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-07 14:50
 * Description:<描述>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private FtpConfig ftpConfig;

    @Autowired
    ProductDao productDao;
    @Override
    public void add(ProductDto productDto) {

        String fileName= StringUtils.renameFileName(productDto.getFileName());
        //String filePath= productDto.getUploadPath()+"\\"+fileName;
        String picSavePath= StringUtils.generateRandomDir(fileName);
        //保存到数据库，dto---->pojo
        Product product= new Product();
        //上传文件
        String filePath="";
        try {
            //StreamUtils.copy(productDto.getInputStream(),new FileOutputStream(filePath));
            filePath=FtpUtils.pictureUploadByCaonfig(ftpConfig,fileName,picSavePath,productDto.getInputStream());
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException("文件上传异常："+e.getMessage());
        }
        try {
            PropertyUtils.copyProperties(product,productDto);
            product.setImage(filePath);
            ProductType productType = new ProductType();
            productType.setId(productDto.getProductTypeId());
            product.setProductType(productType);
            productDao.insert(product);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("aaa"+e.getMessage());
        }



    }

    @Override
    public boolean checkName(String name) {
        Product product=productDao.selectByName(name);
        if(product!=null){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public PageInfo<Product> findAll(Integer pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products= productDao.selectAll();
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        return pageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Product findById(Integer id) {
        return productDao.selectById(id);
    }

    @Override
    public void modifyProduct(ProductDto productDto) throws FileUploadException {

        String filePath="";
        if(productDto.getFileName()!=null){

            String fileName= StringUtils.renameFileName(productDto.getFileName());
            //filePath= productDto.getUploadPath()+"\\"+fileName;
            String picSavePath=StringUtils.generateRandomDir(fileName);
            //保存到数据库，dto---->pojo
            //上传文件
            try {
                //StreamUtils.copy(productDto.getInputStream(),new FileOutputStream(filePath));
               filePath= FtpUtils.pictureUploadByCaonfig(ftpConfig,fileName,picSavePath,productDto.getInputStream());
            } catch (IOException e) {
                //e.printStackTrace();
                throw new RuntimeException("文件上传异常："+e.getMessage());
            }
        }
        Product product= new Product();
        try {
            PropertyUtils.copyProperties(product,productDto);
            if(productDto.getFileName()!=null) {
                product.setImage(filePath);
            }
            ProductType productType = new ProductType();
            productType.setId(productDto.getProductTypeId());
            product.setProductType(productType);
            productDao.update(product);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("aaa"+e.getMessage());
        }
    }

    @Override
    public void removeProduct(Integer id) {
        productDao.deleteById(id);

    }

    @Override
    public void getImage(String path, OutputStream out) {
        try {
            StreamUtils.copy(new FileInputStream(path),out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Product> findByParams(ProductParams productParams) {
        return productDao.selectByParams(productParams);
    }

    @Override
    public boolean addToCart(int id, ShoppingCart sc) {
        Product product = productDao.selectById(id);
        if(product!=null){
            sc.addProduct(product);
            return true;
        }
        return false;
    }

    @Override
    public void removeItemFromShoppingCart(ShoppingCart sc, int id) {
        sc.removeItem(id);
    }

    @Override
    public void modifyItemQuantity(ShoppingCart sc, int id, int quantity) {
        sc.updateItemQuantity(id,quantity);
    }
}
