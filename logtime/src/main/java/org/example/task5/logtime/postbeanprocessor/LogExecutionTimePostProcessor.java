package org.example.task5.logtime.postbeanprocessor;

import lombok.extern.slf4j.Slf4j;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.logtime.invocation.LoggingInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;
import java.util.Arrays;

@Slf4j
public class LogExecutionTimePostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        if (shouldProxy(beanClass)) {
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