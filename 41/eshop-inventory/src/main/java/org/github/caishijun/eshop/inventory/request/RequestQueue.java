package org.github.caishijun.eshop.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求内存队列
 */
public class RequestQueue {

    //内存队列
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    /**
     * 单例有很多种方式去实现：我采取绝对线程安全的一种方式
     * 静态内部类的方式，去初始化单例
     */
    private static class Singleton {
        private static RequestQueue instance;

        static {
            instance = new RequestQueue();
        }

        public static RequestQueue getInstance() {
            return instance;
        }
    }

    /**
     * 利用 jvm 的机制去保证多线程并发安全
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     * <p>
     * 所以 Singleton 中 static 块中 instance = new RequestProcessorThreadPool(); 代码只会执行一次
     *
     * @return
     */
    public static RequestQueue getInstance() {
        return RequestQueue.Singleton.getInstance();
    }

    /**
     * 添加一个内存队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue){
        this.queues.add(queue);
    }

}
