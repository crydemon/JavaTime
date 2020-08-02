package concurrency.thread_pool;

public class Job implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("当前线程名称:"+Thread.currentThread().getName()+";"+"job被指执行了");
    }
}
