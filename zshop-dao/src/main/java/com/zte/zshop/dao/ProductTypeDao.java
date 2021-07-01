package com.zte.zshop.dao;

import com.zte.zshop.entity.ProductType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-01 10:39
 * Description:<描述>
 */
//@Repository
public interface ProductTypeDao {

    public List<ProductType> selectAll();

    public ProductType selectByName(String name);

    public void insert(@Param("name") String name, @Param("status") int status);

    public ProductType selectById(Integer id);

    public void updateName(@Param("id") Integer id, @Param("name") String name);

    public void deleteById(Integer id);

    public void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    public List<ProductType> selectByStatus(int status);
}
