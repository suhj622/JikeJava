package future_usage;

import java.util.concurrent.*;

/**
 * @author Haojie
 * @date 2022/5/29
 * @description 1. 使用FutureTask执行Future的方法 2.使用Completable执行Future的方法
 */
public class FutureDemo {


    /**
     * @descritption 使用FutureTask执行Future的方法 - 阻塞执行
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void futureTaskForFutureMethod1() throws ExecutionException, InterruptedException {
        FutureTask<String> future = new FutureTask<String>(()-> {return "Hello, World";});
        new Thread(future).start();
        System.out.println(future.get());
    }

    /**
     * @description 使用 FutureTask 执行 Future 的方法 - 超时执行
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void futureTaskForFutureMethod2() throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> future = new FutureTask<String>(()-> {return "Hello, World";});
        new Thread(future).start();
        System.out.println(future.get(1, TimeUnit.SECONDS));
    }

    /**
     * @description 使用 FutureTask 执行 Future 的方法 - 超时执行 - 抛出超时异常
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void futureTaskForFutureMethod3() throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> future = new FutureTask<String>(()-> { Thread.sleep(2000); return "Hello, World";});
        new Thread(future).start();
        System.out.println(future.get(1, TimeUnit.SECONDS));
    }

    /**
     * @description 使用FutureTask执行Future的方法 - 阻塞执行
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void completableFutureForFutureMethod1() throws ExecutionException, InterruptedException {
        Future<String> future = CompletableFuture.supplyAsync(()-> { return "Hello, World";});
        System.out.println(future.get());
    }

    /**
     * @description 使用 FutureTask 执行 Future 的方法 - 超时执行 -- 能正常返回结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void completableFutureForFutureMethod2() throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> future = CompletableFuture.supplyAsync(()-> { return "Hello, World";});
        System.out.println(future.get(1, TimeUnit.SECONDS));
    }

    /**
     * @description 使用 FutureTask 执行 Future 的方法 - 超时执行 -- 能正常返回结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void completableFutureForFutureMethod3() throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> future = CompletableFuture.supplyAsync(()-> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello, World";});
        System.out.println(future.get(1, TimeUnit.SECONDS));
    }



    public static void main(String[] args) throws Exception {

        System.out.println("===============================================================================");
        System.out.println("==== 1.1 ~ 1.3 是试验：创建 FaskTask 的实例，执行接口 Future 的方法 ======");
        System.out.println("==== 1.1 使用FutureTask执行Future的方法 - 阻塞执行 ======");
        futureTaskForFutureMethod1();
        System.out.println("==== 1.2 使用 FutureTask 执行 Future 的方法 - 超时执行 -- 能正常返回结果 ======");
        futureTaskForFutureMethod2();
        System.out.println("==== 1.3 使用 FutureTask 执行 Future 的方法 - 超时执行 -- 抛出超时异常 ======");
        try {
            futureTaskForFutureMethod3();
        } catch (TimeoutException e){
            System.out.println("超时了！");
        }

        System.out.println("===============================================================================");
        System.out.println("==== 2.1 ~ 2.3 是试验：创建 CompletableFuture 的实例，执行接口 Future 的方法 ======");
        System.out.println("==== 2.1 使用FutureTask执行Future的方法 - 阻塞执行 ======");
        completableFutureForFutureMethod1();
        System.out.println("==== 2.2 使用 FutureTask 执行 Future 的方法 - 超时执行 -- 能正常返回结果 ======");
        completableFutureForFutureMethod2();
        System.out.println("==== 2.3 使用 FutureTask 执行 Future 的方法 - 超时执行 -- 抛出超时异常 ======");
        try {
            completableFutureForFutureMethod3();
        } catch (TimeoutException e){
            System.out.println("超时了！");
        }

    }
}
