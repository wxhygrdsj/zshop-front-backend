package com.zte.zshop.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Author:helloboy
 * Date:2021-06-07 15:09
 * Description:<描述>
 */
public class StringUtils {

    //sddsdsd.jpg
    public static  String renameFileName(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        //获取后缀，如.jpg
        String suffix = fileName.substring(dotIndex);
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+new Random().nextInt(100)+suffix;


    }

    public static void main(String[] args) {
        //System.out.println(renameFileName("hello.jpg"));
        System.out.println(generateRandomDir("ddddd"));
    }

    //根据文件名生成一个特定的一个二级目录
    public static String generateRandomDir(String fileName) {
        int hashCode = fileName.hashCode();
        int dir1=hashCode & 0xf;//得到1-16的值(一级目录)
        int dir2=(hashCode & 0xf0)>>4;//得到1-16的值(二级目录)
        return "/"+dir1+"/"+dir2;



    }
}
