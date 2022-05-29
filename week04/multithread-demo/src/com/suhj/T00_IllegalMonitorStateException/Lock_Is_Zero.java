package com.suhj.T00_IllegalMonitorStateException;

/**
 * @author Haojie
 * @date 2022/5/27
 */
public class Lock_Is_Zero {
    Object lock = new Object();

    private void please_wait(){
        synchronized (lock){
            try {
                System.out.println("please wait!");
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I am done!");
        }
    }

    private void please_continue(){
        lock.notifyAll();
    }

    private void please_continue2(){
        synchronized (lock){
            lock.notifyAll();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Lock_Is_Zero test = new Lock_Is_Zero();
        new Thread(()-> test.please_wait()).start();
        Thread.sleep(1000);
        new Thread(() -> test.please_continue()).start();
        new Thread(() -> test.please_continue2()).start();
     }
}
