package com.zte.zshop.front.controller;

import com.zte.zshop.entity.Customer;
import com.zte.zshop.exception.LoginErrorException;
import com.zte.zshop.service.CustomerService;
import com.zte.zshop.utils.ResponseResult;
import com.zte.zshop.vo.CustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
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
        if(loginName.isEmpty()||password.isEmpty()){
            return ResponseResult.fail("登录失败");
        }
        try {
            Customer customer= customerService.login(loginName,password);

            session.setAttribute("customer",customer);
            session.setAttribute("customerName",customer.getLoginName());
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

    //注册
    //@RequestMapping("loginIn")
    //@ResponseBody
    //public ResponseResult loginIn(HttpSession session){
    //
    //}


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
        String transname= (String) session.getAttribute("customerName");
        //System.out.println(oldpsw+transname);
        Map<String,Object> map = new HashMap<>();
        boolean res=customerService.checkPsw(oldpsw,transname);
        //如果名称不存在，可用
        if(res){
            map.put("valid",true);
        }
        else{
            map.put("valid",false);
            map.put("message","密码错误");
        }
        return map;
    }


    @RequestMapping("/updatePsw")
    @ResponseBody
    public ResponseResult updatePsw(String newpsw,HttpSession session){
        String transname= (String) session.getAttribute("customerName");
        //System.out.println(newpsw+transname);
        try {
            customerService.updatePsw(newpsw,transname);
            return ResponseResult.success("修改密码成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseResult.fail(e.getMessage());
        }

    }



    @RequestMapping("/add")
    @ResponseBody
    public ResponseResult add(CustomerVO sysuserVO){
        try {
            customerService.add(sysuserVO);
            return ResponseResult.success("注册成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseResult.fail("注册失败");
        }
    }


    //@RequestMapping("/loginIn")
    //@ResponseBody
    //public Map<String,Object> checkName(String loginName){
    //    //System.out.println(loginName);
    //    Map<String,Object> map = new HashMap<>();
    //    boolean res=customerService.checkName(loginName);
    //    //如果名称不存在，可用
    //    if(res){
    //        map.put("valid",true);
    //    }
    //    else{
    //        map.put("valid",false);
    //        map.put("message","账号【"+loginName+"】已经存在");
    //    }
    //    return map;
    //}
}
