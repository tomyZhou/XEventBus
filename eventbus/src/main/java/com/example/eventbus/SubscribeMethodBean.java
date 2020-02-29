package com.example.eventbus;

import java.lang.reflect.Method;

/**
 *
 *   封装1个订阅方法类：
 *   @author zhoguang@unipus.cn
 *   @date  2020/2/29 16:55
 */
public class SubscribeMethodBean {
    private Object owner; //这个方法所属类的对照

    private ThreadMode threadMode;

    private Method method;

    private Object param;

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public SubscribeMethodBean(Object owner, ThreadMode threadMode, Method method, Object param) {
        this.owner = owner;
        this.threadMode = threadMode;
        this.method = method;
        this.param = param;
    }
}
