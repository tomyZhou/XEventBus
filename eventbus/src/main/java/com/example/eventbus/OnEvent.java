package com.example.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *   注解类
 *
 *   @author zhoguang@unipus.cn
 *   @date  2020/2/29 16:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnEvent {
    ThreadMode value() default ThreadMode.MAIN_THREAD;
}
