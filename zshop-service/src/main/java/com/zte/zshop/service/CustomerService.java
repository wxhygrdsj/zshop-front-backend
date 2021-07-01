package com.zte.zshop.service;

import com.zte.zshop.entity.Customer;
import com.zte.zshop.exception.LoginErrorException;

/**
 * Author:helloboy
 * Date:2021-06-23 10:51
 * Description:<描述>
 */
public interface CustomerService {


    public Customer login(String loginName, String password)throws LoginErrorException;
}
