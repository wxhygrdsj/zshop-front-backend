package com.zte.zshop.dao;

import com.zte.zshop.entity.Customer;
import com.zte.zshop.params.CustomerParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-23 11:03
 * Description:<描述>
 */
public interface CustomerDao {
    public Customer selectByLoginNameAndPass(@Param("loginName") String loginName, @Param("password") String password, @Param("isValid") Integer isValid);

    public Customer selectById(Integer id);

    public void update(Customer customer);

    public void updateImage(Customer customer);

    public Customer selectByName(String loginName);

    public void insert(Customer customer);

    public Customer selectPswByName(@Param("password") String oldpsw, @Param("loginName") String transname);

    public void updatePsw(@Param("password") String newpsw, @Param("loginName") String transname);



    public List<Customer> selectAll();

    public List<Customer> selectByParams(CustomerParam customerParam);

    public void updateStatus(@Param("id") Integer id, @Param("isValid") Integer isValid);
}
