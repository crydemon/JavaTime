package concurrency.thread_pool;

/**
 * 线程池方法定义
 * 线程池的本质就是使用了一个线程安全的工作队列连接工作者线程和客户端线程。
 * 客户端线程把任务放入工作队列后便返回，而工作者线程则不端的从工作队列中取出工作并执行。
 * 当工作队列为空时，工作者线程进入WAITING状态，当有客户端发送任务过来后会通过任意一个工作者线程，
 * 随着大量任务的提交，更多的工作者线程被唤醒。
 *
 * @author hrabbit
 */
 interface ThreadPools<Job extends Runnable>{

    /**
     * 执行一个任务(Job),这个Job必须实现Runnable
     * @param job
     */
     void execute(Job job);

    /**
     * 关闭线程池
     */
     void shutdown();

    /**
     * 增加工作者线程，即用来执行任务的线程
     * @param num
     */
     void addWorkers(int num);

    /**
     * 减少工作者线程
     * @param num
     */
     void removeWorker(int num);

    /**
     * 获取正在等待执行的任务数量
     */
     int getJobSize();
}