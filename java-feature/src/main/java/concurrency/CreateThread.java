package concurrency;

import java.util.concurrent.*;

public class CreateThread {
}

class ExtendThread extends Thread {

    private int i;

    public void run() {//run()是线程类的核心方法
        for (int i = 0; i < 10; i++) {
            System.out.println(this.getName() + ":" + i);
        }
    }

    public static void main(String[] args) {
        ExtendThread t1 = new ExtendThread();
        ExtendThread t2 = new ExtendThread();
        t1.start();
        t2.start();
    }
}

/**
 * 继承Tread和实现Runnable接口的区别：
 * <p>
 * a.实现Runnable接口避免单继承局限
 * <p>
 * b.当子类实现Runnable接口，此时子类和Thread的代理模式（子类负责真实业务的操作（调用start()方法），thread负责资源调度与线程创建辅助真实业务。
 */
class ImplementRunnable implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }

    }

    public static void main(String[] args) {
        ImplementRunnable myThread = new ImplementRunnable();
        Thread thread1 = new Thread(myThread, "线程1");
        Thread thread2 = new Thread(myThread, "线程2");
        thread1.start();
        thread2.start();
    }
}

/***
 * FutureTask非常适合用于耗时的计算，主线程可以在完成自己的任务后，再去获取结果。
 * FutureTask可以确保即使调用了多次run方法，它都只会执行一次Runnable或者Callable任务，
 * 或者通过cancel取消FutureTask的执行等。
 */

class UseCallable implements Callable<String> {//Callable是一个泛型接口

    @Override
    public String call() throws Exception {//返回的类型就是传递过来的V类型
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }

        return "Hello Tom";
    }

    public static void main(String[] args) throws Exception {
        UseCallable myThread = new UseCallable();
        FutureTask<String> futureTask = new FutureTask<>(myThread);
        Thread t1 = new Thread(futureTask, "线程1");
        Thread t2 = new Thread(futureTask, "线程2");
        Thread t3 = new Thread(futureTask, "线程3");
        t1.start();
        t2.start();
        t3.start();
        System.out.println(futureTask.get());

    }
}


/***
 *  Executor框架包括：线程池，Executor，Executors，ExecutorService，CompletionService，Future，Callable等。
 *  Executor框架的最大优点是把任务的提交和执行解耦。要执行任务的人只需把Task描述清楚，然后提交即可。
 *  这个Task是怎么被执行的，被谁执行的，什么时候执行的，提交的人就不用关心了。
 *  具体点讲，提交一个Callable对象给ExecutorService（如最常用的线程池ThreadPoolExecutor），将得到一个Future对象，
 *  调用Future对象的get方法等待执行结果就好了。Executor框架的内部使用了线程池机制，它在java.util.concurrent 包下，
 *  通过该框架来控制线程的启动、执行和关闭，可以简化并发编程的操作。
 *  因此，在Java 5之后，通过Executor来启动线程比使用Thread的start方法更好，除了更易管理，效率更好（用线程池实现，节约开销）外，
 *  还有关键的一点：有助于避免this逃逸问题——如果我们在构造器中启动一个线程，因为另一个任务可能会在构造器结束之前开始执行，
 *  此时可能会访问到初始化了一半的对象用Executor在构造器中。
 ***/

class CachedThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {  //执行三个任务，那么线程池中最多创建三个线程
            executorService.execute(() -> {
                for (int j = 0; j < 5; j++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "线程被调用了。");
                }
            });
            System.out.println("************* a" + i + " *************");
        }
        Future<String> future = executorService.submit(() -> {
            for (int j = 0; j < 5; j++) {
                System.out.println(Thread.currentThread().getName() + " :" + j);
            }
            return "Callable is Used!";
        });
        System.out.println(future.get());
        executorService.shutdown();

    }
}



