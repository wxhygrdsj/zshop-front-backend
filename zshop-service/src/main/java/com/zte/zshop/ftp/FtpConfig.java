package com.zte.zshop.ftp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author:helloboy
 * Date:2021-06-21 14:48
 * Description:<描述>
 * 初始化ftp.properties文件中的信息到该对象中
 * @Controller  @Service  @Component  @Repository含义是一样，都表示该类被spring管理，被管理的bean可以通过
 * @Autowired被其他bean使用
 */
@Component
public class FtpConfig {

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;

    @Value("${FTP_PORT}")
    private String FTP_PORT;

    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;

    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;

    @Value("${FTP_BASEPATH}")
    private String FTP_BASEPATH;

    @Value("${IMAGE_BASE_PATH}")
    private String IMAGE_BASE_PATH;

    @Value("${}")
    public String getFTP_ADDRESS() {
        return FTP_ADDRESS;
    }

    public void setFTP_ADDRESS(String FTP_ADDRESS) {
        this.FTP_ADDRESS = FTP_ADDRESS;
    }

    public String getFTP_PORT() {
        return FTP_PORT;
    }

    public void setFTP_PORT(String FTP_PORT) {
        this.FTP_PORT = FTP_PORT;
    }

    public String getFTP_USERNAME() {
        return FTP_USERNAME;
    }

    public void setFTP_USERNAME(String FTP_USERNAME) {
        this.FTP_USERNAME = FTP_USERNAME;
    }

    public String getFTP_PASSWORD() {
        return FTP_PASSWORD;
    }

    public void setFTP_PASSWORD(String FTP_PASSWORD) {
        this.FTP_PASSWORD = FTP_PASSWORD;
    }

    public String getFTP_BASEPATH() {
        return FTP_BASEPATH;
    }

    public void setFTP_BASEPATH(String FTP_BASEPATH) {
        this.FTP_BASEPATH = FTP_BASEPATH;
    }

    public String getIMAGE_BASE_PATH() {
        return IMAGE_BASE_PATH;
    }

    public void setIMAGE_BASE_PATH(String IMAGE_BASE_PATH) {
        this.IMAGE_BASE_PATH = IMAGE_BASE_PATH;
    }
}
