package org.example.task5.logtime.postbeanprocessor;

import lombok.extern.slf4j.Slf4j;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.logtime.invocation.LoggingInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LogExecutionTimePostProcessor implements BeanPostProcessor {
    private Map<String, Class> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        if (shouldProxy(beanClass)) {
            map.put(beanName, beanClass);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            return createProxy(bean);
        }
        return bean;
    }

    private boolean shouldProxy(Class<?> beanClass) {
        if (beanClass.isAnnotationPresent(LogExecutionTime.class)) {
            return true;
        }

        return Arrays.stream(beanClass.getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(LogExecutionTime.class));
    }

    private Object createProxy(Object bean) {
        return Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                bean.getClass().getInterfaces(),
                new LoggingInvocationHandler(bean));
    }
}