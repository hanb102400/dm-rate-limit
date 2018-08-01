package com.rats.guava;

public class ConsumeService {


    public void consume(){
        System.out.println("consume:" + Thread.currentThread().getName());
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
