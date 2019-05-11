package com.wei.volicate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VolatileTest {
    public static void main(String[] args) {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(4);
        final Counter counter = new Counter();
        for(int i=0;i<=1000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    counter.inc();
                }
            }).start();
        }
        System.out.println(counter);
    }


}
