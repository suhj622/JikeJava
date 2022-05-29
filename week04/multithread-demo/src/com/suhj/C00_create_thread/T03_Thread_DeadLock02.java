package com.suhj.C00_create_thread;

/**
 * 测试 Thread执行 wait() 方法是否会释放锁
 * @Author:suhj
 */
public class T03_Thread_DeadLock02 {

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

    Runnable awake = ()->{
        synchronized (lock){
            lock.notifyAll();
        }
    };


    public static void main(String[] args) throws Exception {
        T03_Thread_DeadLock02 test = new T03_Thread_DeadLock02();
        Runnable target = test.target;
        Runnable awake = test.awake;
        new Thread(target).start();
        new Thread(target).start();
        Thread.sleep(5000);
         new Thread(awake).start();
    }
}
//Java异常之IllegalMonitorStateException
//其实意思就是说,也就是当前的线程不是此对象监视器的所有者。也就是要在当前线程锁定对象，才能用锁定的对象此行这些方法，需要用到synchronized ，锁定什么对象就用什么对象来执行  notify(), notifyAll(),wait(), wait(long), wait(long, int)操作，否则就会报IllegalMonitorStateException


//https://www.imooc.com/article/255866