package com.zte.zshop.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author:helloboy
 * Date:2021-06-21 14:55
 * Description:<描述>
 */
public class Test {

    public static void main(String[] args) {
        testFtp();
    }

    private static void testFtp() {

        //创建客户端对象
        FTPClient ftp = new FTPClient();
        InputStream local=null;

        try {
            //连接ftp服务器
            ftp.connect("localhost",21);
            //登录
            ftp.login("mike","123");

            //设置上传路径
            String path="/2/3";

            //检查上传路径是否存在，如果不存在，创建一个目录
            boolean flag = ftp.changeWorkingDirectory(path);
            if(!flag){
                boolean b = ftp.makeDirectory(path);
                System.out.println("aaaa------>"+b);
            }
            //指定上传路径
            boolean c=ftp.changeWorkingDirectory(path);

            //读取本地文件
            //设置上传文件的类型，二进制文件
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            File file=  new File("d:\\a\\cat.jpg");
            local = new FileInputStream(file);

            ftp.setControlEncoding("utf-8");
            String name=file.getName();

            //完成上传
            ftp.storeFile(name,local);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                //关闭文件流
                local.close();
                //退出
                ftp.logout();
                //断开连接
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
