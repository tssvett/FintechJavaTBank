<<<<<<<< HEAD:src/main/java/org/example/task8/App.java
package org.example.task8;
========
package org.example;
>>>>>>>> fj_2024_lesson_8:src/main/java/org/example/App.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
