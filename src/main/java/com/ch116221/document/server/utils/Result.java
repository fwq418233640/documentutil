package com.ch116221.document.server.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * Http api 接口统一返回值
 *
 * @author zhaolei
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 状态
     */
    private boolean success;

    /**
     * 提示
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回状态码
     */
    private String number;

    /**
     * 构造器 默认状态为成功 提示信息为 "成功"
     */
    public Result() {
        this.success = true;
        this.msg = "成功!";
    }

    /**
     * 静态方法获取 成功状态对象
     */
    public static <T> Result<T> success() {
        return new Result<>();
    }

    /**
     * 静态方法获取 成功状态对象 并且设置返回数据
     *
     * @param e 返回数据
     */
    public static <T> Result<T> success(T e) {
        return success("成功", e);
    }

    /**
     * 静态方法获取 成功状态对象 并且设置返回数据和消息提示
     *
     * @param e 返回数据
     * @param m 消息提示
     */
    public static <T> Result<T> success(String m, T e) {
        Result<T> result = new Result<>();
        result.setData(e);
        result.setMsg(m);
        result.setSuccess(true);
        return result;
    }

    /**
     * 静态方法获取 失败状态构造对象 并且设置消息提示
     *
     * @param m 消息提示
     */
    public static <T> Result<T> fail(String m) {
        return fail(m, null);
    }

    /**
     * 静态方法获取 失败状态构造对象
     */
    public static <T> Result<T> fail() {
        return fail("失败", null);
    }

    /**
     * 静态方法获取 失败状态构造对象 并设置消息提示 与返回数据
     *
     * @param m 消息提示
     * @param e 返回数据
     */
    public static <E> Result<E> fail(String m, E e) {
        Result<E> result = new Result<>();
        result.setSuccess(false);
        result.setMsg(m);
        result.setData(e);
        return result;
    }
}
