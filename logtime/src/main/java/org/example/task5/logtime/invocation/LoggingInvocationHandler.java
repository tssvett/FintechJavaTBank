package org.example.task5.logtime.invocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class LoggingInvocationHandler implements InvocationHandler {
    private final Object bean;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = method.invoke(bean, args);
        } catch (Exception e) {
            log.error("Ошибка при вызове метода {}: {}", method.getName(), e.getMessage(), e);
        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("Время выполнения метода {}: {} ms", method.getName(), elapsedTime);
        }

        return result;
    }
}