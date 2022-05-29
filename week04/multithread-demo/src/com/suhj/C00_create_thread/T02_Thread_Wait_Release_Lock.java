package com.suhj.C00_create_thread;

/**
 * 测试 Thread执行 wait() 方法是否会释放锁
 * @Author:suhj
 */
public class T02_Thread_Wait_Release_Lock {

    Object lock = new Object();

    Runnable target = () ->{
        synchronized (lock){
            System.out.println( Thread.currentThread().getName()+ "开始执行......");
            try {
                lock.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+ "结束执行......");
        }
     };

    public static void main(String[] args) {
        T02_Thread_Wait_Release_Lock test = new T02_Thread_Wait_Release_Lock();
        Runnable target = test.target;
        new Thread(target).start();
        new Thread(target).start();
    }
}
