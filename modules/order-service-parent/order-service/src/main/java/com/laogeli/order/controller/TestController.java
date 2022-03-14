package com.laogeli.order.controller;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author wang
 * @Date 2021-10-15 14:30
 **/
public class TestController {

    static AtomicInteger atomicInteger = new AtomicInteger(0);


    private static int SIZE_ = 50;

    public void increTest() {
        atomicInteger.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        TestController testController = new TestController();
        CountDownLatch countDownLatch = new CountDownLatch(SIZE_);
        for (int i = 0; i < SIZE_; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        testController.increTest();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }

            }, String.valueOf(i)).start();
        }
        countDownLatch.await();
    }
}
