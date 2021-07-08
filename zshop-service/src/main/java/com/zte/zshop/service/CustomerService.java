package com.zte.zshop.service;

import com.github.pagehelper.PageInfo;
import com.zte.zshop.dto.CustomerImageDto;
import com.zte.zshop.entity.Customer;
import com.zte.zshop.exception.LoginErrorException;
import com.zte.zshop.params.CustomerParam;
import com.zte.zshop.vo.CustomerVO2;
import org.apache.commons.fileupload.FileUploadException;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-23 10:51
 * Description:<描述>
 */
public interface CustomerService {


    public Customer login(String loginName, String password)throws LoginErrorException;

    public Customer findById(Integer id);


    public void modifyCustomer(Customer customer);

    public void modifyCustomerImage(CustomerImageDto customerImageDto) throws FileUploadException;



    public boolean checkName(String loginName);

    public boolean checkPsw(String oldpsw, String transname);

    public void updatePsw(String newpsw, String transname);

    public void add(CustomerVO2 customerVO);


    public PageInfo<Customer> findAll();

    public List<Customer> findByParams(CustomerParam customerParam);

    public void modify(CustomerVO2 customerVO);

    public void modifyStatus(Integer id);




}
