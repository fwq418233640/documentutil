package com.ch.base;

import lombok.Data;

import java.io.Serializable;

/**
 * Http api 接口统一返回值
 *
 * @author zhaolei
 */
@Data
public class Msg<E> implements Serializable {

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
    private E data;

    /**
     * 返回状态码
     */
    private String number;

    /**
     * 构造器 默认状态为成功 提示信息为 "成功"
     */
    public Msg() {
        this.success = true;
        this.msg = "成功!";
    }

    /**
     * 静态方法获取 成功状态对象
     */
    public static Msg sucess() {
        return new Msg();
    }

    /**
     * 静态方法获取 成功状态对象 并且设置返回数据
     *
     * @param e 返回数据
     */
    public static <E> Msg<E> sucess(E e) {
        return sucess("成功", e);
    }

    /**
     * 静态方法获取 成功状态对象 并且设置返回数据和消息提示
     *
     * @param e 返回数据
     * @param m 消息提示
     */
    public static <E> Msg<E> sucess(String m, E e) {
        Msg<E> msg = new Msg<>();
        msg.setData(e);
        msg.setMsg(m);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 静态方法获取 失败状态构造对象 并且设置消息提示
     *
     * @param m 消息提示
     */
    public static <E> Msg<E> fail(String m) {
        return fail(m, null);
    }

    /**
     * 静态方法获取 失败状态构造对象
     */
    public static <E> Msg<E> fail() {
        return fail("失败", null);
    }

    /**
     * 静态方法获取 失败状态构造对象 并设置消息提示 与返回数据
     *
     * @param m 消息提示
     * @param e 返回数据
     */
    public static <E> Msg<E> fail(String m, E e) {
        Msg<E> msg = new Msg<>();
        msg.setSuccess(false);
        msg.setMsg(m);
        msg.setData(e);
        return msg;
    }
}
