package com.suhj.C00_create_thread;

/**
 * 测试线程在同步块中sleep之后是否会释放锁
 */
public class T01_Thread_Sleep_Not_Release_Lock {

    Runnable target = () ->{
        synchronized (this){
            System.out.println( Thread.currentThread().getName()+ "开始执行......");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+ "结束执行......");
        }
    };

    public static void main(String[] args) {
        T01_Thread_Sleep_Not_Release_Lock test = new T01_Thread_Sleep_Not_Release_Lock();
        Runnable target = test.target;
        new Thread(target).start();
        new Thread(target).start();
    }
}
