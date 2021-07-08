package com.zte.zshop.service.impl;

import com.github.pagehelper.PageInfo;
import com.zte.zshop.constants.Constant;
import com.zte.zshop.dao.CustomerDao;

import com.zte.zshop.dto.CustomerImageDto;
import com.zte.zshop.entity.Customer;

import com.zte.zshop.exception.LoginErrorException;
import com.zte.zshop.ftp.FtpConfig;
import com.zte.zshop.ftp.FtpUtils;
import com.zte.zshop.params.CustomerParam;
import com.zte.zshop.service.CustomerService;
import com.zte.zshop.utils.StringUtils;
import com.zte.zshop.vo.CustomerVO2;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private FtpConfig ftpConfig;

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
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Customer findById(Integer id) {

        return customerDao.selectById(id);
    }

    @Override
    public void modifyCustomer(Customer customer) {
        customerDao.update(customer);
    }

    @Override
    public void modifyCustomerImage(CustomerImageDto customerImageDto)throws FileUploadException {
        String filePath="";
        if(customerImageDto.getFileName()!=null){
            String fileName= StringUtils.renameFileName(customerImageDto.getFileName());
            String picSavePath=StringUtils.generateRandomDir(fileName);

            try {
                filePath= FtpUtils.pictureUploadByCaonfig(ftpConfig,fileName,picSavePath,customerImageDto.getInputStream());
            } catch (IOException e) {
                //e.printStackTrace();
                throw new RuntimeException("文件上传异常："+e.getMessage());
            }
        }
        Customer customer= new Customer();
        try {
            PropertyUtils.copyProperties(customer,customerImageDto);
            if(customerImageDto.getFileName()!=null) {
                customer.setImage(filePath);
            }

            customerDao.updateImage(customer);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("aaa"+e.getMessage());
        }
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
    public void add(CustomerVO2 customerVO) {
        Customer customer=new Customer();
        try {
            //System.out.println("sys:"+customerVO.toString());

            PropertyUtils.copyProperties(customer,customerVO);
            customer.setRegistDate(new Date());
            customer.setIsValid(Constant.CUSTOMER_VALID);

            //System.out.println("cus"+customer.toString());
            customer.setImage("ftp://mike:123@localhost/user.jpeg");
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


    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public PageInfo<Customer> findAll() {
        List<Customer>customers=customerDao.selectAll();
        PageInfo<Customer>pageInfo=new PageInfo<>(customers);
        return pageInfo;

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Customer> findByParams(CustomerParam customerParam) {
        return customerDao.selectByParams(customerParam);
    }


    @Override
    public void modify(CustomerVO2 customerVO) {
        try{
            Customer customer=new Customer();
            PropertyUtils.copyProperties(customer,customerVO);
            customerDao.update(customer);
        }catch (Exception e){
            throw new RuntimeException("aaa"+e.getMessage());
        }
    }

    @Override
    public void modifyStatus(Integer id) {
        Customer customer=customerDao.selectById(id);
        Integer isValid = customer.getIsValid();
        if(isValid==Constant.SYSUSER_VALID){
            isValid=Constant.SYSUSER_INVALID;
        }
        else{
            isValid=Constant.SYSUSER_VALID;
        }
        customerDao.updateStatus(id,isValid);

    }




}
