package com.zte.zshop.cart;

import javax.servlet.http.HttpSession;

/**
 * Author:helloboy
 * Date:2021-06-25 8:57
 * Description:<描述>
 */
public class ShoppingCartUtils {

    /**
     * 逻辑：
     * 从session作用域中获取购物车对象，若session中没有该对象,创建一个新的对象，放入到session作用域，若有，直接返回
     * (单例模式)
     */
    public static ShoppingCart getShoppingCart(HttpSession session){
       ShoppingCart sc= (ShoppingCart) session.getAttribute("shoppingCart");
       if(sc==null){
           sc = new ShoppingCart();
           session.setAttribute("shoppingCart",sc);
       }
       return sc;

    }
    public static ShoppingCart getShoppingCart2(HttpSession session){
        ShoppingCart sc= (ShoppingCart) session.getAttribute("shoppingCart2");
        if(sc==null){
            sc = new ShoppingCart();
            session.setAttribute("shoppingCart2",sc);
        }
        return sc;

    }

}
