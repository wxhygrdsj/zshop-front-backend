package com.zte.zshop.controller;

import com.github.pagehelper.PageInfo;
import com.zte.zshop.constants.Constant;
import com.zte.zshop.entity.ProductType;
import com.zte.zshop.exception.ProductTypeExistsException;
import com.zte.zshop.service.ProductTypeService;
import com.zte.zshop.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author:helloboy
 * Date:2021-05-31 15:15
 * Description:<描述>
 */
@Controller
@RequestMapping("/backend/productType")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @RequestMapping("/findAll")
    public String findAll(Integer pageNum, Model model){

        if(ObjectUtils.isEmpty(pageNum)){
            pageNum= Constant.PAGE_NUM;
        }
        //完成查询列表逻辑
        PageInfo<ProductType> pageInfo = productTypeService.findAll(pageNum, Constant.PAGE_SIZE);
        model.addAttribute("data",pageInfo);
        //pageInfo.getList();
        //pageInfo.getPageNum();
        //pageInfo.getPages();
        //返回产品类型管理页面
        return "productTypeManager";
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResponseResult add(@RequestParam("name") String productTypeName){
        ResponseResult result = new ResponseResult();
        try {
            productTypeService.add(productTypeName);
            result.setStatus(Constant.RESPONSE_STATUS_SUCCESS);
            result.setMessage("添加成功");
        } catch (ProductTypeExistsException e) {
            //e.printStackTrace();
            result.setStatus(Constant.RESPONSE_STATUS_FAILURE);
            result.setMessage("商品类型已经存在");
        }


        return result;
    }

    @RequestMapping("/findById")
    @ResponseBody
    public ResponseResult findById(Integer id){
       ProductType productType= productTypeService.findById(id);
       return ResponseResult.success(productType);

    }

    @RequestMapping("/modifyName")
    @ResponseBody
    public ResponseResult modifyName(Integer id, String name){
        try {
            productTypeService.modifyName(id,name);
            return ResponseResult.success("修改商品类型成功");
        } catch (ProductTypeExistsException e) {
            //e.printStackTrace();
            return ResponseResult.fail(e.getMessage());
        }

    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public ResponseResult deleteById(Integer id){
        productTypeService.removeById(id);
        return ResponseResult.success("删除成功");
    }

    @RequestMapping("/modifyStatus")
    @ResponseBody
    public ResponseResult modifyStatus(Integer id){
        productTypeService.modifyStatus(id);
        return ResponseResult.success("修改状态成功");
    }


}
