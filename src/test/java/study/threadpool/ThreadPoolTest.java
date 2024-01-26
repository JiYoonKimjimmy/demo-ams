package study.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ThreadPoolTest {

    @Test
    void singleThreadTest() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> System.out.println("Thread Pool Test !!"));
    }

    @Test
    void fixedThreadPoolTest() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<String> future = executorService.submit(() -> "Thread Pool Test");
        String result = future.get();
        System.out.println(result);
    }

    @Test
    void fixedThreadPoolTest2() throws Exception {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.setCorePoolSize(1);
        executor.setMaximumPoolSize(3);

        executor.submit(() -> task(1));
        executor.submit(() -> task(2));
        executor.submit(() -> task(3));

        System.out.println("first : " + executor.getPoolSize());
        System.out.println("first : " + executor.getQueue().size());

        Thread.sleep(3000);

        executor.submit(() -> task(4));

        System.out.println("second : " + executor.getPoolSize());
        System.out.println("second : " + executor.getQueue().size());

        executor.shutdown();
    }

    @Test
    void getAvailableProcessorsTest() {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    private static void task(int str) {
        try {
            Thread.sleep(1000);
            System.out.println("Hello World - " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
