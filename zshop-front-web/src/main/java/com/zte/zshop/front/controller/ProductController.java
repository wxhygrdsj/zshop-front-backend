package com.zte.zshop.front.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.zte.zshop.cart.ShoppingCart;
import com.zte.zshop.cart.ShoppingCartUtils;
import com.zte.zshop.constants.Constant;
import com.zte.zshop.entity.Order;
import com.zte.zshop.entity.Product;
import com.zte.zshop.entity.ProductType;
import com.zte.zshop.params.ProductParams;
import com.zte.zshop.service.CustomerService;
import com.zte.zshop.service.OrderService;
import com.zte.zshop.service.ProductService;
import com.zte.zshop.service.ProductTypeService;
import com.zte.zshop.utils.ResponseResult;
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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:helloboy
 * Date:2021-06-17 9:14
 * Description:<描述>
 */
@Controller
@RequestMapping("front/product")
public class ProductController {

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;

    @ModelAttribute("productTypes")
    public List<ProductType> loadProductTypes(){

        List<ProductType> productTypes = productTypeService.findByEnable(Constant.PRODUCT_TYPE_ENABLE);
        return productTypes;

    }


    @RequestMapping("/main")
    public String main(ProductParams productParams,Integer pageNum, Model model){
        if(ObjectUtils.isEmpty(pageNum)){
            pageNum=Constant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum,Constant.PAGE_SIZE_FRONT);
        List<Product> products = productService.findByParams(productParams);
        PageInfo<Product> pageInfo = new PageInfo<>(products);


        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("productParams",productParams);
        return "main";
    }

    //显示图片
    @RequestMapping("/showPic")
    public void showPic(String image, OutputStream out)throws IOException {
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

    @RequestMapping("/toCart")
    public String toCart(){

        return "cart";
    }

    @RequestMapping("/toMyOrders")
    public String toMyOrders(Model model,Integer id){
        //System.out.println(id);
        List<Order> orderList=orderService.findAllOrderByCustomerId(id);
        model.addAttribute("orderList",orderList);
        return "myOrders";
    }

    @RequestMapping("/toCenter")
    public String toCenter(){

        return "center";
    }

    @RequestMapping("/toOrder")
    public String toOrder(){
        return "order";
    }
    //添加购物车
    @RequestMapping("/addToCart")
    @ResponseBody
    public ResponseResult addToCart(int id, HttpSession session){
        boolean flag=false;
        ShoppingCart sc = ShoppingCartUtils.getShoppingCart(session);
        flag=productService.addToCart(id,sc);
        if(flag){
            //添加成功，转发到订单列表页面显示
            return ResponseResult.success("放入购物车成功");
        }
        return ResponseResult.fail("放入购物车失败");

    }

    @RequestMapping("/removeItemById")
    @ResponseBody
    public ResponseResult removeItemById(int id,HttpSession session){
        ShoppingCart sc = ShoppingCartUtils.getShoppingCart(session);
        productService.removeItemFromShoppingCart(sc,id);
        //如果购物车已经空了，到空页面，否则到cart.jsp
        if(sc.isEmpty()){
            return ResponseResult.fail("购物车已空");
        }
        //重新计算商品总价
        double totalMoney = sc.getTotalMoney();
        return ResponseResult.success(totalMoney);

    }

    //更新商品数量
    @RequestMapping("/updateItemQuantity")
    @ResponseBody
    public Map<String,Object> updateItemQuantity(int id,int quantity,HttpSession session){
        ShoppingCart sc = ShoppingCartUtils.getShoppingCart(session);
        productService.modifyItemQuantity(sc,id,quantity);

        Map<String,Object> result=  new HashMap<>();
        result.put("itemMoney",sc.getProducts().get(id).getItemMoney());
        result.put("totalMoney",sc.getTotalMoney());
        return result;

    }
    //清空购物车
    @RequestMapping("/clearCart")
    @ResponseBody
    public ResponseResult clearCart(HttpSession session){
        ShoppingCart sc = ShoppingCartUtils.getShoppingCart(session);
        if(sc.isEmpty()){
            return ResponseResult.fail("购物车已经为空");
        }
        session.removeAttribute("shoppingCart");
        return ResponseResult.success("清空购物车成功");

    }
    @RequestMapping("/deleteSelected")
    @ResponseBody
    public ResponseResult deleteSelected(HttpSession session, Integer[]ids){

        ShoppingCart sc = ShoppingCartUtils.getShoppingCart(session);
        if(sc.isEmpty()){
            return ResponseResult.fail("购物车已经为空");
        }
        if(ids==null){
            return ResponseResult.fail("请勾选删除项");
        }else {
            for(Integer id:ids){
                sc.removeItem(id);
            }
            //session.setAttribute("shoppingCart",sc);
            if(sc.getTotalMoney()==0){
                return ResponseResult.success("0.00");
            }
            DecimalFormat df = new DecimalFormat("#.00");

            return ResponseResult.success(df.format(sc.getTotalMoney()));
        }


    }
    @RequestMapping("/settleSelected")
    @ResponseBody
    public ResponseResult settleSelected(HttpSession session, Integer[]ids) throws CloneNotSupportedException {

        ShoppingCart sc=new ShoppingCart();
        sc = ShoppingCartUtils.getShoppingCart(session);

        //ShoppingCart sc2= new ShoppingCart();
        ///sc2.setProducts(sc.getProducts());
        Gson gson=new Gson();
        ShoppingCart sc2=gson.fromJson(gson.toJson(sc),ShoppingCart.class);

        try {
            //PropertyUtils.copyProperties(sc2,sc);
            //id 是未被选中的
            //System.out.println("sc2:"+sc2);
            if (ids != null) {
                for(Integer id:ids){
                    System.out.println("ids:"+id);
                    sc2.removeItem(id);    //结算，留下选中的,ids 传的是未被选中的，移除
                }
            }

            if(sc2.isEmpty()){
                return ResponseResult.fail("请勾选结算项");

            }
            System.out.println("sc2:"+sc2);
            session.setAttribute("shoppingCart2",sc2);
            ShoppingCart k=(ShoppingCart)session.getAttribute("shoppingCart2");
            System.out.println("k:"+k);
        } catch (Exception e) {
            //e.printStackTrace();
        }
       /* System.out.println("移除选中ids2之前的sc:"+sc);*/

        /*for(Integer id:ids2){
            System.out.println("ids2:"+id);
            sc.removeItem(id);
        }
        System.out.println("sc:"+sc);*/
            //session.setAttribute("shoppingCart",sc);

        return ResponseResult.success("成功");

    }

}
