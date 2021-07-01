package com.zte.zshop.service.impl;

import com.zte.zshop.constants.Constant;
import com.zte.zshop.dao.CustomerDao;
import com.zte.zshop.entity.Customer;
import com.zte.zshop.exception.LoginErrorException;
import com.zte.zshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author:helloboy
 * Date:2021-06-23 10:52
 * Description:<描述>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Customer login(String loginName, String password) throws LoginErrorException {
        Customer customer= customerDao.selectByLoginNameAndPass(loginName,password, Constant.CUSTOMER_VALID);
        if(customer==null){
            throw  new LoginErrorException("登录失败，用户名或密码不正确");
        }
        return customer;
    }
}
