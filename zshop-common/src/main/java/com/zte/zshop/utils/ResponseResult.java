package com.zte.zshop.utils;

import com.zte.zshop.constants.Constant;

/**
 * Author:helloboy
 * Date:2021-06-03 8:41
 * Description:<描述>
 * 将返回信息封装成对象，可以反复使用
 */
public class ResponseResult {

    //状态码
    private Integer status;

    //消息
    private String message;

    //返回数据
    public Object data;

    public ResponseResult() {
    }

    public ResponseResult(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static  ResponseResult success(String message){
        return  new ResponseResult(Constant.RESPONSE_STATUS_SUCCESS,message,null);
    }
    public static  ResponseResult success(Object obj){
        return  new ResponseResult(Constant.RESPONSE_STATUS_SUCCESS,"success",obj);
    }

    public static  ResponseResult fail(String message){
        return  new ResponseResult(Constant.RESPONSE_STATUS_FAILURE,message,null);
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
