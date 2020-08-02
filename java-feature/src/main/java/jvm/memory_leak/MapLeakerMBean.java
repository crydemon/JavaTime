package jvm.memory_leak;

/*
标准MBean要求必须创建一个以MBean为后缀的接口，
接口中定义标准的getter和setter方法名，还可以定义其他可以执行的方法。
 */
public interface MapLeakerMBean {
    boolean isTerminated();
}