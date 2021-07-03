package com.zte.zshop.controller;

import com.github.pagehelper.PageInfo;
import com.zte.zshop.constants.Constant;
import com.zte.zshop.dto.ProductDto;
import com.zte.zshop.entity.Product;
import com.zte.zshop.entity.ProductType;
import com.zte.zshop.service.ProductService;
import com.zte.zshop.service.ProductTypeService;
import com.zte.zshop.utils.ResponseResult;
import com.zte.zshop.vo.ProductVO;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import java.util.List;
import java.util.Map;

/**
 * Author:helloboy
 * Date:2021-06-04 14:54
 * Description:<描述>
 */
@Controller
@RequestMapping("/backend/product")
public class ProductController {

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductService productService;

    //初始化下拉列表，这样保证在执行任何请求之前，完成初始化数据
    //@ModelAttribute注解表明在执行该controller的任何方法之前，执行该注解对应的方法，这个方法的返回值存入类似于request作用域中
    @ModelAttribute("productTypes")
    public List<ProductType> loadProductTypes(){
        //获取下拉列表的值
        List<ProductType> productTypes=productTypeService.findByEnable(Constant.PRODUCT_TYPE_ENABLE);
        return productTypes;
    }


    @RequestMapping("/findAll")
    public String findAll(Integer pageNum,Model model){
        if(ObjectUtils.isEmpty(pageNum)){
            pageNum=Constant.PAGE_NUM;
        }
        PageInfo<Product> pageInfo= productService.findAll(pageNum,Constant.PAGE_SIZE);
        model.addAttribute("data",pageInfo);
        return "productManager";
    }


    @RequestMapping("/add")
    public String add(ProductVO productVO, Integer pageNum,HttpSession session,Model model){
        //System.out.println(productVO);
        //获取文件上传路径，注意：这个物理路径必须是存在
        //String uploadPath=session.getServletContext().getRealPath("/WEB-INF/upload");

        ProductDto productDto = new ProductDto();
        //将vo--->dto
        //productDto.setName(productVO.getName());
        //productDto.setPrice(productVO.getPrice());
        //productDto.setProductTypeId(productVO.getProductTypeId());
        //使用bean的工具类完成bean中属性值的拷贝
        try {
            PropertyUtils.copyProperties(productDto,productVO);
            productDto.setFileName(productVO.getFile().getOriginalFilename());
            productDto.setInputStream(productVO.getFile().getInputStream());
            //productDto.setUploadPath(uploadPath);
            productService.add(productDto);
            model.addAttribute("successMsg","添加成功");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg","添加失败");
        }

        //返回列表页,转发到findAll请求
        return "forward:findAll?pageNum="+pageNum;

    }

    //ajax后套校验产品名称是否已经存在
    @RequestMapping("/checkName")
    @ResponseBody
    //返回的json格式必须包含两个元素，valid,message,当valid为false时，输出message的值
    public Map<String,Object> checkName(String name){
        Map<String,Object> map = new HashMap<>();
        boolean res=productService.checkName(name);
        //如果名称不存在，可用，否则不可用，输出结果
        if(res){
            map.put("valid",true);
        }
        else{
            //注意：这里必须是这两个key,remote.js会自动读取这两个key的值
            map.put("valid",false);
            map.put("message","商品("+name+")已经存在");
        }
        return map;

    }

    //显示修改窗口
    @RequestMapping("/findById")
    @ResponseBody
    public ResponseResult findById(Integer id){
        Product product=productService.findById(id);
        return ResponseResult.success(product);
    }

    //在修改窗口显示图片
    @RequestMapping("/showPic")
    public void showPic(String image, OutputStream out)throws IOException {
        URL url = new URL(image);
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();

        BufferedOutputStream bos = new BufferedOutputStream(out);
        byte[] data = new byte[4096];
        int size=0;
        size = is.read(data);
        while(size!=1){
            bos.write(data,0,size);
            size=is.read(data);
        }
        is.close();
        bos.flush();
        bos.close();


    }

    //修改商品
    @RequestMapping("/modify")
    public String modify(ProductVO productVO,Integer pageNum, HttpSession session,Model model){
        //System.out.println(productVO);
        //获取文件上传路径，注意：这个物理路径必须是存在
        //String uploadPath=session.getServletContext().getRealPath("/WEB-INF/upload");

        ProductDto productDto = new ProductDto();
        //将vo--->dto
        //productDto.setName(productVO.getName());
        //productDto.setPrice(productVO.getPrice());
        //productDto.setProductTypeId(productVO.getProductTypeId());
        //使用bean的工具类完成bean中属性值的拷贝
        try {
            PropertyUtils.copyProperties(productDto,productVO);
            if(!"".equals(productVO.getFile().getOriginalFilename())) {
                productDto.setFileName(productVO.getFile().getOriginalFilename());
                productDto.setInputStream(productVO.getFile().getInputStream());
                //productDto.setUploadPath(uploadPath);
            }
            productService.modifyProduct(productDto);
            model.addAttribute("successMsg","修改成功");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg","修改失败");
        }

        //返回列表页,转发到findAll请求
        return "forward:findAll?pageNum="+pageNum;

    }

    //删除商品
    @RequestMapping("/removeById")
    @ResponseBody
    public ResponseResult removeById(Integer id){
        try {
            productService.removeProduct(id);
            return ResponseResult.success("删除成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseResult.fail("删除失败");
        }

    }

}
