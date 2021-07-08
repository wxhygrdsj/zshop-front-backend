package com.zte.zshop.cart;

import com.zte.zshop.entity.Product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:helloboy
 * Date:2021-06-24 14:58
 * Description:<描述>
 * 购物车对象，里面包含购物车明细，用于完成购物逻辑
 */
public class ShoppingCart {

    private Map<Integer,ShoppingCartItem> products = new HashMap<>();

    public Map<Integer, ShoppingCartItem> getProducts() {
        return products;
    }

    /*
        向购物车中添加一个商品
        实现逻辑：
        查看当前购物车是否有该商品明细，如果有，不新增记录，只数量+1，如果没有，在购物车中新增一条记录，数量初始化为1
     */
    public void addProduct(Product product){

        ShoppingCartItem sci = products.get(product.getId());
        if(sci==null){
            sci = new ShoppingCartItem(product);
            products.put(product.getId(),sci);

        }
        else{
            sci.increment();
        }


    }
    //查看该购物车中是否有该商品
    public boolean hasProduct(int id){
        return products.containsKey(id);
    }

    //获取购物车中的商品总数
    //逻辑：遍历购物车集合，获取所有的购物车明细，求数量之和
    public int getProductNumber() {
        int total = 0;
        for (ShoppingCartItem sci : getItems()) {
            total+=sci.getQuantity();
        }
        return total;

    }
    //获取购物车中所有明细的集合
    public Collection<ShoppingCartItem> getItems(){
        return products.values();
    }

    //判断购物车是否为空
    public boolean isEmpty(){
        return products.isEmpty();
    }

    //获取购物车中所有商品的总价
    public double getTotalMoney(){
        double total=0;
        for (ShoppingCartItem sci:getItems()){
            total+=sci.getItemMoney();
        }
        return total;
    }


    //清空购物车
    public void clear(){
        products.clear();
    }

    //移除指定id的购物明细
    public void removeItem(int id){
        products.remove(id);
    }

    //修改指定购物明细的数量
    public void updateItemQuantity(int id,int quantity){
        ShoppingCartItem sci = products.get(id);
        if(sci!=null){
            sci.setQuantity(quantity);
        }
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "products=" + products +
                '}';
    }
}
