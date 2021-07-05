package com.zte.zshop.service.impl;

import com.zte.zshop.constants.Constant;
import com.zte.zshop.dao.CustomerDao;
import com.zte.zshop.entity.Customer;
import com.zte.zshop.exception.LoginErrorException;
import com.zte.zshop.service.CustomerService;
import com.zte.zshop.vo.CustomerVO;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Override
    public boolean checkName(String loginName) {
        Customer customer=customerDao.selectByName(loginName);
        if(customer!=null){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPsw(String oldpsw,String transname) {
        Customer customer=customerDao.selectPswByName(oldpsw,transname);
        if(customer!=null){
            return true;
        }
        return false;
    }

    @Override
    public void add(CustomerVO sysuserVO) {
        Customer customer=new Customer();
        try {
            System.out.println("sys:"+sysuserVO.toString());

            PropertyUtils.copyProperties(customer,sysuserVO);
            customer.setRegistDate(new Date());
            customer.setIsValid(Constant.CUSTOMER_VALID);
            customer.setName(sysuserVO.getCusname());
            System.out.println("cus"+customer.toString());
            customerDao.insert(customer);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updatePsw(String newpsw, String transname) {
        customerDao.updatePsw(newpsw,transname);
    }
}
