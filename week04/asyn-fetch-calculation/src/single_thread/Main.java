package single_thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Haojie
 * @date 2022/5/30
 * @description 试验用单线程，异步获取计算结果的方式
 */
public class Main {


    public static Object lock = new Object();
    public static ReentrantLock reentrantLock = new ReentrantLock();
    public static Condition condition = reentrantLock.newCondition();
    public static Semaphore semaphore = new Semaphore(1);

    public static void getLock(){
        synchronized (lock){
            System.out.println("主线程获取锁了！可以获取答案了。");
        }
    }



    public static void main(String[] args) throws Exception {
        System.out.println("====================================================================");
        System.out.println("1. 使用FutureTask 异步计算-阻塞获取");
        FutureTask<Integer> futureTask = new FutureTask<>(()->sum());
        new Thread(futureTask).start();
        System.out.println(futureTask.get());

        System.out.println("====================================================================");
        System.out.println("2. 使用CompeletableFuture 异步计算-阻塞获取");
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->sum());
        //CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->sum(), Executors.newSingleThreadExecutor()); //该语句会造成死锁 以后再研究
        System.out.println(completableFuture.get());

        System.out.println("====================================================================");
        System.out.println("3. 继承Thread类，使用join同步获取");
        MyThread1 t1 = new MyThread1();
        t1.start();
        t1.join();
        System.out.println(t1.get());

        System.out.println("====================================================================");
        System.out.println("4-1. synchronized + 自旋 思路 - 继承Thread类，保证计算子线程优先执行，主线程成功获取锁之后，输出结果，并退出");
        MyThread2_1 t2 = new MyThread2_1();
        t2.start();
        while (!(t2.isAlive() && t2.isRun)){
            System.out.println("子线程t2还未启动，等待10毫秒......");
            Thread.sleep(10);
        }
        getLock();
        System.out.println(t2.get());

        System.out.println("====================================================================");
        System.out.println("4-2. synchronized + 等待 思路 - 继承Thread类，保证计算子线程优先执行，主线程成功获取锁之后，输出结果，并退出");
        MyThread2_2 t2_2 = new MyThread2_2();
        t2_2.start();
        while (!t2_2.isCompleted){
            synchronized (lock){
                lock.wait();
            }
        }
        System.out.println(t2_2.get());

        System.out.println("====================================================================");
        System.out.println("5. countDownLatch思路-继承Thread类");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MyThread3 t3 = new MyThread3(countDownLatch);
        t3.start();
        countDownLatch.await();
        System.out.println(t3.get());

        System.out.println("====================================================================");
        System.out.println("6. CyclicBarrier思路-继承Thread类");
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        MyThread4 t4 = new MyThread4(cyclicBarrier);
        t4.start();
        cyclicBarrier.await();
        System.out.println(t4.get());

        System.out.println("====================================================================");
        System.out.println("7. ReentrantLock + Condition 思路-继承Thread类");
        MyThread5 t5 = new MyThread5(reentrantLock,condition);
        t5.start();
        reentrantLock.lock();
        if (!t5.isCompleted) condition.await();
        reentrantLock.unlock();
        System.out.println(t5.get());

        System.out.println("====================================================================");
        System.out.println("8. Semaphore 思路 - 继承Thread");
        MyThread6 t6 = new MyThread6(semaphore);
        t6.start();
        while (!t6.isCompleted){
            semaphore.acquireUninterruptibly();
            semaphore.release();
        }
        System.out.println(t6.get());

        System.out.println("====================================================================");
        System.out.println("9. LockSupport 思路 - 继承Thread");
        MyThread7 t7 = new MyThread7(Thread.currentThread());
        t7.start();
        while (!t7.isCompleted){
            LockSupport.park();
        }
        System.out.println(t7.get());

        System.out.println("====================================================================");
        System.out.println("10. 自旋思路 - 继承Thread");
        MyThread8 t8 = new MyThread8();
        t8.start();
        while (!t8.isCompleted){
            System.out.println("子线程t8还未执行完成，等待10毫秒...");
            Thread.sleep(10);
        }
        System.out.println(t8.get());

    }


    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

    static final class MyThread1 extends Thread  {
        volatile int result;

        @Override
        public void run() {
            result = sum();
        }

        public int get(){
            return result;
        }
    }

    static final class MyThread2_1 extends Thread  {
        volatile int result;
        volatile boolean isRun;

        @Override
        public void run() {
            synchronized (lock){
                isRun = true;
                result = sum();
            }
        }

        public int get(){
            return result;
        }
    }

    static final class MyThread2_2 extends Thread  {
        volatile int result;
        volatile boolean isCompleted;

        @Override
        public void run() {
            synchronized (lock){
                try{
                    result = sum();
                    isCompleted = true;
                }finally {
                    lock.notify();
                }

            }
        }

        public int get(){
            return result;
        }
    }

    static final class MyThread3 extends Thread  {
        private CountDownLatch countDownLatch;
        volatile int result;

        public MyThread3(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }


        @Override
        public void run() {
            result = sum();
            countDownLatch.countDown();
        }

        public int get(){
            return result;
        }
    }

    static final class MyThread4 extends Thread  {
        private CyclicBarrier cyclicBarrier;
        volatile int result;

        public MyThread4(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }


        @Override
        public void run() {
            try{
                result = sum();
            }finally {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }

        }

        public int get(){
            return result;
        }
    }

    static final class MyThread5 extends Thread  {
        private ReentrantLock lock;
        private Condition condition;
        volatile int result;
        volatile boolean isCompleted;

        public MyThread5(ReentrantLock lock, Condition condition){
            this.lock = lock;
            this.condition = condition;
        }


        @Override
        public void run() {
            try {
                lock.lock();
                result = sum();
                condition.signal();
                lock.unlock();
            }finally {
                isCompleted = true;
            }
        }

        public int get(){
            return result;
        }
    }

    static final class MyThread6 extends Thread  {
        private Semaphore semaphore;
        volatile int result;
        volatile boolean isCompleted;

        public MyThread6(Semaphore semaphore){
            this.semaphore = semaphore;
        }


        @Override
        public void run() {
            try {
                semaphore.acquireUninterruptibly();
                result = sum();
                isCompleted = true;
            }finally {
                semaphore.release();
            }
        }

        public int get(){
            return result;
        }
    }

    static final class MyThread7 extends Thread  {
        volatile int result;
        volatile boolean isCompleted;
        private Thread waiter;

        public MyThread7(Thread waiter){
            this.waiter = waiter;
        }


        @Override
        public void run() {
            try {
                result = sum();
                isCompleted = true;
            }finally {
                LockSupport.unpark(waiter);
            }


        }

        public int get(){
            return result;
        }
    }

    static final class MyThread8 extends Thread  {
        volatile int result;
        volatile boolean isCompleted;


        @Override
        public void run() {
            result = sum();
            isCompleted = true;
        }

        public int get(){
            return result;
        }
    }

}
