package com.wei.volicate;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    //private AtomicInteger count=new AtomicInteger(0);
    private volatile int count=0;
    public void inc() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

       // int andIncrement = count.getAndIncrement();
        count++;
        System.out.println("dayin"+count);
    }

    @Override
    public String toString() {
        return "Counter{" +
                "count=" + count +
                '}';
    }
}