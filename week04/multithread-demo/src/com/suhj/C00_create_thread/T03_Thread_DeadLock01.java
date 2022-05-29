package com.suhj.C00_create_thread;

/**
 * 测试 Thread执行 wait() 方法是否会释放锁
 * @Author:suhj
 */
public class T03_Thread_DeadLock01 {

    Object lock = new Object();

    Runnable target = () ->{
        synchronized (lock){
            System.out.println( Thread.currentThread().getName()+ "开始执行......");
            try {
                 lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             System.out.println(Thread.currentThread().getName()+ "结束执行......");
        }
     };



    public static void main(String[] args) throws Exception {
        T03_Thread_DeadLock01 test = new T03_Thread_DeadLock01();
        Runnable target = test.target;
         new Thread(target).start();
        new Thread(target).start();
      }
}
