package com.chason.wx.common;

/**
 * Created by caolh on 2018/2/2.
 */
public class Result<T> {

    private Byte status;//状态-1非正常，0正常
    private String msg;//null 正常；字符串 错误信息
    private int errcode;//错误码
    private T data;//数据类型

    public int getErrode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    /**
     * 正确返回：status=1，msg=null，data 数据
     * @param o
     * @return
     */
    public static Result success(Object o){
        Result result = new Result();
        byte status=1;

        result.setStatus(status);
        result.setMsg(null);
        result.setData(o);
        return result;
    }

    /**
     * 正确返回：status=1，msg，data=null 数据
     * @param str
     * @return
     */
    public static Result success(String str){
        Result result = new Result();
        byte status=1;
        result.setStatus(status);
        result.setMsg(str);
        result.setData(null);
        return result;
    }

    /**
     * 正确返回：status=1，msg=提示信息，data 数据
     * @param o
     * @param  msg
     * @return
     */
    public static Result success(Object o,String msg){
        Result result = new Result();
        byte status=1;
        result.setStatus(status);
        result.setMsg(msg);
        result.setData(o);
        return result;
    }

    /**
     * 异常返回：status=0，msg= 异常信息，data 数据为null
     * @param error
     * @return
     */
    public static Result error(String error){
        Result result = new Result();
        byte status=0;
        result.setStatus(status);
        result.setMsg(error);
        result.setData(null);
        return result;
    }


    /**
     * 异常返回：status=0，msg= 异常信息，data 数据为null
     * @param error
     * @return
     */
    public static Result error(String error,int errcode){
        Result result = new Result();
        byte status=0;
        result.setStatus(status);
        result.setMsg(error);
        result.setErrcode(errcode);
        result.setData(null);
        return result;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
