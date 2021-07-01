package com.zte.zshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Author:helloboy
 * Date:2021-06-16 11:03
 * Description:<描述>
 *
 */
@Controller
@RequestMapping("/backend/code")
public class CodeController {

    @RequestMapping("/image")
    //使用图形工具生成一个验证码
    //1:生成一个字符串
    //2:将该字符串生成一张图片
    public void image(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //

        //1:生成一个字符串
        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random r = new Random();
        int len =ch.length;
        int index;
        StringBuffer sb = new StringBuffer();

        //System.out.println(sb.toString());
        //2:将该字符串生成一张图片
        BufferedImage bi = new BufferedImage(68,22,BufferedImage.TYPE_INT_BGR);
        //通过缓冲区对象创建画布
        Graphics gra = bi.getGraphics();
        Color c = new Color(200,150,255);
        gra.setColor(c);
        gra.fillRect(0,0,68,22);

        for(int i=0;i<4;i++){
            index=r.nextInt(len);//随机找0-len-1的数值
            //将字符绘制成图像
            gra.setColor(new Color(r.nextInt(88),r.nextInt(188),r.nextInt(200)));
            gra.drawString(ch[index]+"",(i*15)+3,18);
            sb.append(ch[index]);
        }
        //将该字符串保存到session作用域
        req.getSession().setAttribute("picCode",sb.toString());
        //将图像输出到页面
        ImageIO.write(bi,"JPG",resp.getOutputStream());





    }

    @RequestMapping("/checkCode")
    @ResponseBody
    public Map<String,Object> checkCode(String code, HttpSession session){

        Map<String,Object> map = new HashMap<>();
        String picCode= (String) session.getAttribute("picCode");
        if(picCode.equalsIgnoreCase(code)){
            map.put("valid",true);
        }
        else{
            map.put("valid",false);
            //map.put("message","验证码错误");
        }



        return map;

    }
}
