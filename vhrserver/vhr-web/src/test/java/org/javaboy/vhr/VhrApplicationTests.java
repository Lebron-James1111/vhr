package org.javaboy.vhr;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SpringBootTest
class VhrApplicationTests {


    volatile static Person p = new Person("aaa");
    static ThreadLocal<Person> threadLocal = new ThreadLocal<>();

    @Test
    void contextLoads() throws InterruptedException {

        new Thread(() -> {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(p.name);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(p.name);
            p.name = "bbb";
        }).start();
    }

    @Test
    public void test2() {
        new Thread(() -> {
            threadLocal.set(new Person("aaa"));
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(threadLocal.get().name);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadLocal.set(new Person("bbb"));
        }).start();
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {
        //Runnbale方法
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable方式");
            }
        });

        t.run();
        t.run();
        t.run();

        t.start();

        //Callable方法
        FutureTask futureTask = new FutureTask<>(new Callable() {
            @Override
            public Object call() throws Exception {
                return 100;
            }
        });
        new Thread(futureTask).start();

        System.out.println(futureTask.get());

    }

    @Test
    public void test4() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        notify();
        lock.unlock();
    }

}

class Person {
    String name;

    public Person(String name) {
        this.name = name;
    }
}
