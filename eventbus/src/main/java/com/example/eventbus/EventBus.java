package com.example.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 *   通过读取注解获取方法，通过反射来调用
 *   @author zhoguang@unipus.cn
 *   @date  2020/2/29 17:31
 */
public class EventBus {

    private Map<String, List<SubscribeMethodBean>> eventMap = new HashMap<>();

    private static EventBus eventBus;

    //单例，双重校验锁来保证线程安全
    public static EventBus getDefault() {
        if (eventBus == null) {
            synchronized (EventBus.class) {
                if (eventBus == null) {
                    eventBus = new EventBus();
                }
            }
        }
        return eventBus;
    }

    /**
     * 注册监听
     *
     * @param object
     */
    public void register(Object object) {

        //注册当前类，把当前类带有注解的方法找出来。
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            OnEvent annotation = method.getAnnotation(OnEvent.class);
            if (annotation == null) {
                continue;
            } else {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length > 1) {
                    throw new RuntimeException("只能接收一个参数");
                }
                ThreadMode threadMode = annotation.value();
                SubscribeMethodBean bean = new SubscribeMethodBean(object, threadMode, method, paramTypes[0]);
                List<SubscribeMethodBean> tempList = eventMap.get(paramTypes[0].getSimpleName());
                if (tempList == null) {
                    tempList = new ArrayList<>();
                    tempList.add(bean);
                    eventMap.put(paramTypes[0].getSimpleName(), tempList);
                } else {
                    tempList.add(bean);
                    eventMap.put(paramTypes[0].getSimpleName(), tempList);
                }

            }
        }

    }

    public void unregister(Object object) {
        for (String eventName : eventMap.keySet()) {
            List<SubscribeMethodBean> list = eventMap.get(eventName);

            if (list == null || list.size() == 0) return;

            Iterator<SubscribeMethodBean> iterator = list.iterator();
            while (iterator.hasNext()) {
                SubscribeMethodBean methodBean = iterator.next();
                System.out.println(methodBean.getOwner() + ":" + object);
                if (methodBean.getOwner() == object) {
                    //删除当前类中的所有方法
                    iterator.remove();
                }
            }

            if (list != null && list.size() == 0) {
                eventMap.remove(eventName);
            }
        }
    }


    /**
     * 发出通知
     *
     * @param event
     */
    public void postEvent(Object event) {
        List<SubscribeMethodBean> list = eventMap.get(event.getClass().getSimpleName());
        if (list != null && list.size() > 0) {
            for (SubscribeMethodBean bean : list) {
                try {

                    invokeMethod(event, bean);

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void invokeMethod(final Object event, final SubscribeMethodBean bean) throws InvocationTargetException, IllegalAccessException {

        switch (bean.getThreadMode()) {
            case MAIN_THREAD: //主线程接收
                //如果是在主线程中发出的消息
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    invoke(bean, event);
                } else {
                    /**
                     * 需要刷新UI时的Handle实例化：
                     *
                     *         在主线程中： Handler handler = new Handler();
                     *
                     *         在其他线程中： Handler handler = new Handler(Looper.getMainLooper());
                     *
                     */
                    Handler handler = new Handler(Looper.getMainLooper()); //主线程的handler
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invoke(bean, event);
                        }
                    });
                }

                break;
            case BACKGROUND: //接受者是子线程
                if (Looper.myLooper() == Looper.getMainLooper()) { //发布者是主线程
                    /**
                     *  当子线程给主线程发时不需要创建Looper，因为主线程默认会调用该方法
                     *  当主线程给子线程发送消息时，子线程需要创建Looper
                     */
                    Looper.prepare();
                    new Handler().post(new Runnable() { //子线程的handler
                        @Override
                        public void run() {
                            //运行在子线程中
                            invoke(bean, event);
                        }
                    });
                    //启动Looper
                    Looper.loop();
                } else { //发布者也是子线程，接收者也是子线程，直接调用就可以了，不用切换线程。
                    invoke(bean, event);
                }
                break;
        }

    }

    private void invoke(SubscribeMethodBean bean, Object event) {
        try {
            bean.getMethod().invoke(bean.getOwner(), event);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
