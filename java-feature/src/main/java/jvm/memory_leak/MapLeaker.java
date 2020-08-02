package jvm.memory_leak;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;


//https://www.jianshu.com/p/bdf06e2c1541
public class MapLeaker  {
    //因为FixedThreadPool的核心线程不会自动超时关闭，使用时必须在适当的时候调用shutdown()方法。
    public ExecutorService exec = Executors.newFixedThreadPool(5);
    public Map<Task, TaskStatus> taskStatus = Collections.synchronizedMap(new HashMap<Task, TaskStatus>());
    private Random random = new Random();

    private enum TaskStatus {NOT_STARTED, STARTED, FINISHED}

    private class Task implements Runnable {
        private int[] numbers = new int[random.nextInt(200)];

        public void run() {
            Thread.currentThread().setName("task");
            int[] temp = new int[random.nextInt(10000)];
            taskStatus.put(this, TaskStatus.STARTED);
            taskStatus.put(this, TaskStatus.FINISHED);
            System.out.println("ok");
        }
    }

    public Task newTask()  {
        Task t = new Task();
        taskStatus.put(t, TaskStatus.NOT_STARTED);
        exec.execute(t);
        System.out.println("task finished");
        //exec.shutdown();
        return t;
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        MapLeaker mapLeaker = new MapLeaker();
        mapLeaker.newTask();
    }
}

class MapLeakerAgent {
    public static void main(String[] args) throws MalformedObjectNameException {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("jvm:type=MapLeaker");
        MapLeaker mapLeaker = new MapLeaker();
        mapLeaker.newTask();
        try {
            mbs.registerMBean(mapLeaker, mbeanName);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }

}