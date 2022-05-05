package com.example.study.asm;

/**
 * Created by chenyy on 2022/5/5.
 */

public class OptimizedThreadAsm {
    public void test() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("ChenYy");
            }
        };
        new Thread(runnable,"thread name").start();
    }
}
