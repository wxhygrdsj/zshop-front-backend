package com.zte.zshop.front.controller;

import com.zte.zshop.dto.CustomerImageDto;
import com.zte.zshop.entity.Customer;
import com.zte.zshop.exception.LoginErrorException;
import com.zte.zshop.service.CustomerService;
import com.zte.zshop.utils.ResponseResult;
import com.zte.zshop.vo.CustomerImageVO;
import com.zte.zshop.vo.CustomerVO;
import com.zte.zshop.vo.CustomerVO2;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:helloboy
 * Date:2021-06-23 10:49
 * Description:<描述>
 */
@Controller
@RequestMapping("/front/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;



    @RequestMapping("/loginByAccount")
    @ResponseBody
    public ResponseResult loginByAccount(String loginName, String password, HttpSession session){

        try {
            Customer customer= customerService.login(loginName,password);
            session.setAttribute("customer",customer);
            //session.setAttribute("customer",customer);
            return ResponseResult.success(customer);
        } catch (LoginErrorException e) {
            e.printStackTrace();
            return ResponseResult.fail("登录失败");
        }


    }

    //退出
    @RequestMapping("/loginOut")
    @ResponseBody
    public ResponseResult loginOut(HttpSession session){
        //清空session
        session.invalidate();

        //清空session中的用户
        //session.removeAttribute("customer");
        return ResponseResult.success("退出成功");

    }
    @RequestMapping("/modify")
    @ResponseBody
    public ResponseResult modify(CustomerVO customerVO, HttpSession session){
        Customer customer= new Customer();
        try {
            PropertyUtils.copyProperties(customer,customerVO);
            customerService.modifyCustomer(customer);
            Customer customer2=customerService.findById(customerVO.getId());
            session.setAttribute("customer",customer2);

            return ResponseResult.success(customer);
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseResult.fail("修改失败");
        }

    }
    @RequestMapping("/modifyImage")

    public String modifyImage(CustomerImageVO customerImageVO,Model model,HttpSession session){
        CustomerImageDto customerImageDto = new CustomerImageDto();
        try {
            PropertyUtils.copyProperties(customerImageDto,customerImageVO);
            if(!"".equals(customerImageVO.getFile().getOriginalFilename())) {
                customerImageDto.setFileName(customerImageVO.getFile().getOriginalFilename());
                customerImageDto.setInputStream(customerImageVO.getFile().getInputStream());
                customerService.modifyCustomerImage(customerImageDto);
            }
            Customer customer=customerService.findById(customerImageVO.getId());
            session.setAttribute("customer",customer);
            model.addAttribute("successMsg","修改成功");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg","修改失败");
        }
        return "center";



    }
    @RequestMapping("/getPic")
    public void getPic(String image, OutputStream out)throws IOException {
        URL url = new URL(image);
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();

        BufferedOutputStream bos = new BufferedOutputStream(out);
        byte[] data = new byte[4096];
        int size=0;
        size = is.read(data);
        while(size!=-1){
            bos.write(data,0,size);
            size=is.read(data);
        }
        is.close();
        bos.flush();
        bos.close();
    }

    @RequestMapping("/checkName")
    @ResponseBody
    public Map<String,Object> checkName(String loginName){
        //System.out.println(loginName);
        Map<String,Object> map = new HashMap<>();
        boolean res=customerService.checkName(loginName);
        //如果名称不存在，可用
        if(res){
            map.put("valid",true);
        }
        else{
            map.put("valid",false);
            map.put("message","账号【"+loginName+"】已经存在");
        }
        return map;
    }

    @RequestMapping("/checkPsw")
    @ResponseBody
    public Map<String,Object> checkPsw(String oldpsw,HttpSession session){
        Customer customer=(Customer) session.getAttribute("customer");
        Map<String,Object> map = new HashMap<>();
        System.out.println(customer.getLoginName());
        boolean res=customerService.checkPsw(oldpsw,customer.getLoginName());

        //如果名称不存在，可用
        if(res){
            map.put("valid",true);
        }
        else{
            map.put("valid",false);
            map.put("message","原密码错误");
        }
        return map;
    }


    @RequestMapping("/updatePsw")
    @ResponseBody
    public ResponseResult updatePsw(String newpsw,HttpSession session){
        Customer customer=(Customer) session.getAttribute("customer");

        //System.out.println(newpsw+transname);
        try {
            customerService.updatePsw(newpsw,customer.getLoginName());
            return ResponseResult.success("修改密码成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseResult.fail(e.getMessage());
        }

    }



    @RequestMapping("/add")
    @ResponseBody
    public ResponseResult add(CustomerVO2 customerVO){
        try {
            customerService.add(customerVO);
            return ResponseResult.success("注册成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseResult.fail("注册失败");
        }
    }




}
