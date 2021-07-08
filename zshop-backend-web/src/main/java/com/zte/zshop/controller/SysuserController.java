package com.zte.zshop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.zshop.constants.Constant;
import com.zte.zshop.entity.Role;
import com.zte.zshop.entity.Sysuser;
import com.zte.zshop.exception.SysuserNotExistsException;
import com.zte.zshop.params.SysuserParam;
import com.zte.zshop.service.RoleService;
import com.zte.zshop.service.SysuserService;
import com.zte.zshop.utils.ResponseResult;
import com.zte.zshop.vo.SysuserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:helloboy
 * Date:2021-05-31 15:02
 * Description:<描述>
 */
@Controller
@RequestMapping("/backend/sysuser")
public class SysuserController {

    @Autowired
    private SysuserService sysuserService;

    @Autowired
    private RoleService roleService;

    @ModelAttribute("roles")
    public List<Role> loadRoles(){
        return roleService.findAll();
    }

    @RequestMapping("/login")
    public String login(String loginName, String password, HttpSession session, Model model){

        //完成登录逻辑
        try {
            Sysuser sysuser=sysuserService.login(loginName,password);
            session.setAttribute("sysuser",sysuser);
            //返回主界面
            return "main";
        } catch (SysuserNotExistsException e) {
            e.printStackTrace();
            model.addAttribute("errorMsg","登录异常");
            //返回登录页继续登录
            return "login";
        }

    }

    @RequestMapping("/findAll")
    public String findAll(Integer pageNum, Model model){
        if(ObjectUtils.isEmpty(pageNum)){
            pageNum= Constant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum,2);
        List<Sysuser> sysusers = sysuserService.findAll();
        PageInfo<Sysuser> pageInfo = new PageInfo<>(sysusers);
        model.addAttribute("data",pageInfo);
        return "sysuserManager";
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResponseResult add(SysuserVO sysuserVO){

        try {
            sysuserService.add(sysuserVO);
            return ResponseResult.success("添加成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseResult.fail("添加失败");
        }
    }

    //校验该名称是否已经在数据库中存在
    @RequestMapping("/checkName")
    @ResponseBody
    public Map<String,Object> checkName(String loginName){
        Map<String,Object> map = new HashMap<>();
        boolean res=sysuserService.checkName(loginName);
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

    //组合查询
    @RequestMapping("/findByParams")
    public String findByParams(SysuserParam sysuserParam, Integer pageNum, Model model){

        if(ObjectUtils.isEmpty(pageNum)){
            pageNum= Constant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum,2);
        List<Sysuser> sysusers= sysuserService.findByParams(sysuserParam);
        PageInfo<Sysuser> pageInfo = new PageInfo<>(sysusers);
        model.addAttribute("sysuserParam",sysuserParam);
        model.addAttribute("data",pageInfo);
        //返回列表页
        return "sysuserManager";
    }

    @RequestMapping("/modifyStatus")
    @ResponseBody
    public ResponseResult modifyStatus(Integer id){
        try {
            sysuserService.modifyStatus(id);
            return ResponseResult.success("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.fail("更新失败");
        }
    }

    @RequestMapping("/findById")
    @ResponseBody
    public ResponseResult findById(Integer id){
        Sysuser sysuser= sysuserService.findById(id);
        return ResponseResult.success(sysuser);

    }

    @RequestMapping("/modify")
    public String modify(SysuserVO sysuserVO,Integer pageNum,Model model){

        try {

            sysuserService.modify(sysuserVO);
            model.addAttribute("successMsg","修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg","修改失败");
        }
        return "forward:findAll?pageNum="+pageNum;
    }

    @RequestMapping("/returnlogin")
    public String returnlog(HttpSession session){
        session.removeAttribute("sysuser");
        return "login";
    }



}
