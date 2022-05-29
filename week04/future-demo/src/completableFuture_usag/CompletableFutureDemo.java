package completableFuture_usag;

import java.util.concurrent.CompletableFuture;

/**
 * @author Haojie
 * @date 2022/5/29
 * @description 体验 CompletableFuture 的13类方法（CompletionStage 接口中所定义的）
 *              暂时忽略线程池的使用，只考虑返回结果的使用情况
 *              暂时忽略异步执行，只考虑返回结果的使用情况
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {

        System.out.println("=============================================================================");
        System.out.println("0：提交任务");
        //0：提交任务
        //0.1 提交任务 - 无返回值
        CompletableFuture future1 = CompletableFuture.runAsync(()-> System.out.println("Hello, World!"));
        //0.1 提交任务 - 有返回值
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()-> {return "Hello, World!";});
        CompletableFuture<String> future21 = CompletableFuture.supplyAsync(()-> {return "Hello, Everyone!";});


        System.out.println("=============================================================================");
        System.out.println("1：thenApply - 依赖上一个stage的结果，构建新的stage");
        //1：thenApply - 依赖上一个stage的结果，构建新的stage

        CompletableFuture<String> future3 = future2.thenApply((v) -> {System.out.println("1.1：thenApply"); return v +"It's Wonderful!";});

        //由于是异步执行，System.out.println("1.2：thenApplyAsync"); 不会立即执行
        CompletableFuture<String> future4= future2.thenApplyAsync((v) -> {System.out.println("1.2：thenApplyAsync");return v +"It's Wonderful!";});

        //注释掉，屏蔽阻塞获取任务结果对实验观察的影响

//        String outcome3 = future3.get();
//        System.out.println(outcome3);
//        String outcome4 = future4.get();
//        System.out.println(outcome4);

        System.out.println("=============================================================================");
        System.out.println("2：thenAccept - 消费上一个stage的结果，此阶段构建了一个类型void（返回值为null）的stage");
        CompletableFuture future5 = future2.thenAccept((v)->{ System.out.println("2：thenAccept"); System.out.println(v + "That's good!");});

        System.out.println("=============================================================================");
        System.out.println("3：thenRun - 等待上游stage执行完毕后，再执行给定的Runnable，此阶段构建了一个类型void（返回值为null）的stage");
        CompletableFuture future51 = future2.thenRun(()->{ System.out.println("The Word is good! and I am good!");});

        System.out.println("=============================================================================");
        System.out.println("4：thenCombine - 组合两个上游stage的结果，构建新的stage");
        CompletableFuture future6 = future2.thenCombine(future21, (w,v)->{System.out.println(w); System.out.println(v); return "done!";});

        System.out.println("=============================================================================");
        System.out.println("5：thenAcceptBoth - 消费两个上游stage的结果，此阶段构建了一个类型void（返回值为null）的stage");
        CompletableFuture future7 = future2.thenAcceptBoth(future21, (w,v)->{System.out.println(w); System.out.println(v);});

        System.out.println("=============================================================================");
        System.out.println("6：runAfterBoth - 等待两个上游stage执行完毕，再执行该阶段的Runnable，此阶段构建了一个类型void（返回值为null）的stage");
        CompletableFuture future8  = future2.runAfterBoth(future21, ()->{System.out.println("That is good Time！");});

        System.out.println("=============================================================================");
        System.out.println("7：applyToEither - 依赖两个上游stage，只要其中之一执行完毕，就获取该结果，构建新的stage");
        CompletableFuture<String> future91 = CompletableFuture.supplyAsync(()-> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "xiaoMing";});

        CompletableFuture<String> future92 = CompletableFuture.supplyAsync(()-> {return "xiaoHong";});
        CompletableFuture<String> future9 = future91.applyToEither(future92, (v)->{
            System.out.println(v + " is a good Student!");return v + " is a good Student!";});

        System.out.println("=============================================================================");
        System.out.println("8：acceptEither - 依赖两个上游stage，只要其中之一执行完毕，就消费该结果，执行给定的操作，构建一个类型为void（执行结果为null）的stage");
        CompletableFuture future10 = future91.acceptEither(future92, (v)->{
            System.out.println(v + " is a good Student!");});

        System.out.println("=============================================================================");
        System.out.println("9：runAfterEither - 依赖两个上游stage，只要其中之一执行完毕，执行给定的Runnable，构建一个类型为void（执行结果为null）的stage");
        CompletableFuture future11 = future91.runAfterEither(future92, ()->{
            System.out.println("Teacher ask: Who is a good Student?");});

        System.out.println("=============================================================================");
        System.out.println("10：thenCompose - 依赖上游stage，获取上游stage的结果，可以经过一系列处理后，返回新的stage，这个stage可以是自定义的类型");
        CompletableFuture future12 = future2.thenCompose((v)->{
            System.out.println(v); return future21;}).thenAccept((v)-> System.out.println(v));

        System.out.println("=============================================================================");
        System.out.println("11：exceptionally - 处理上一stage抛出的异常！");
        CompletableFuture<String> future13 = CompletableFuture.supplyAsync(()-> {throw new NullPointerException();});

        CompletableFuture<String> future14 = future13.exceptionally((v)->{
            System.out.println(v);
            System.out.println("处理了空指针异常！");
            return "done!";});

        System.out.println("=============================================================================");
        System.out.println("12：whenComplete - 对上一stage的补充处理，完成时可以进行一些额外的校验，如结果检查或者是异常检查，但该stage的结果仍然会传递给下一个stage");
        CompletableFuture future15 = future2.whenComplete((result,exception)->{
            System.out.println("完成时额外处理！");
            System.out.println("异常时额外处理！");
        }).thenAccept((v)-> System.out.println(v));

        System.out.println("=============================================================================");
        System.out.println("13：handle - 对上一stage的补充处理，完成时可以进行一些额外的校验，如结果检查或者是异常检查，生成新的结果并传递给下一个stage");
        CompletableFuture future16 = future2.handle((oldresult, exception) -> {String newresult = "Love & Peace";return newresult;})
                .thenAccept((v)-> System.out.println(v));

        //主线程等待 7 秒钟
        Thread.sleep(7000);

    }
}
