package concurrency.thread_pool;

public class WorkTest {
    public static void main(String[] args) {
        DefaultThreadPool defaultThreadPool = new DefaultThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            if (i == 30) {
                defaultThreadPool.addWorkers(10);
            }
            Job job = new Job();
            defaultThreadPool.execute(job);
        }
    }
}
